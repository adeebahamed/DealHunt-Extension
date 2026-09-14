// ==========================================
// DEALHUNT BACKGROUND SERVICE WORKER
// ==========================================

const BACKEND_URL =
    "http://localhost:8080/api/deals/search";


// ==========================================
// MESSAGE LISTENER
// ==========================================

chrome.runtime.onMessage.addListener(
    (message, sender, sendResponse) => {

        if (!message || !message.type) {
            return;
        }

        // --------------------------------------
        // SEARCH DEALS
        // --------------------------------------

        if (message.type === "SEARCH_DEALS") {

            searchDeals(message.product)
                .then(data => {

                    sendResponse({
                        success: true,
                        data: data
                    });

                })
                .catch(error => {

                    console.error(
                        "DealHunt backend error:",
                        error
                    );

                    sendResponse({
                        success: false,
                        error: error.message ||
                            "Could not connect to DealHunt backend."
                    });
                });

            // Keep message channel open
            // while fetch is running.
            return true;
        }
    }
);


// ==========================================
// CALL SPRING BOOT BACKEND
// ==========================================

async function searchDeals(product) {

    if (!product) {
        throw new Error(
            "No product information received."
        );
    }

    console.log(
        "DealHunt: sending product to backend:",
        product
    );


    const requestBody = {

        name: product.name || "",

        brand: product.brand || "",

        model: product.model || "",

        variant: product.variant || "",

        category: product.category || "",

        price:
            product.price != null
                ? String(product.price)
                : "",

        url: product.url || "",

        imageUrl: product.imageUrl || ""
    };


    console.log(
        "DealHunt: request body:",
        requestBody
    );


    const response = await fetch(
        BACKEND_URL,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(requestBody)
        }
    );


    if (!response.ok) {

        throw new Error(
            `Backend returned HTTP ${response.status}`
        );
    }


    const data =
        await response.json();


    console.log(
        "DealHunt: backend response:",
        data
    );


    return data;
}


// ==========================================
// EXTENSION INSTALLED
// ==========================================

chrome.runtime.onInstalled.addListener(() => {

    console.log(
        "DealHunt extension installed."
    );
});