// ==========================================
// DEALHUNT CONTENT SCRIPT
// ==========================================

console.log("DealHunt content script loaded.");


// ==========================================
// GET PRODUCT INFORMATION FROM PAGE
// ==========================================

function getProductInformation() {

    const product = {

        name: "",

        brand: "",

        model: "",

        variant: "",

        category: "",

        price: "",

        url: window.location.href,

        imageUrl: ""
    };


    // ======================================
    // PRODUCT NAME
    // ======================================

    product.name =
        getProductName();


    // ======================================
    // BRAND
    // ======================================

    product.brand =
        getProductBrand();


    // ======================================
    // PRICE
    // ======================================

    product.price =
        getProductPrice();


    // ======================================
    // CATEGORY
    // ======================================

    product.category =
        getProductCategory();


    // ======================================
    // IMAGE
    // ======================================

    product.imageUrl =
        getProductImage();


    // ======================================
    // DEBUG
    // ======================================

    console.log(
        "DealHunt detected product:",
        product
    );


    return product;
}


// ==========================================
// PRODUCT NAME
// ==========================================

function getProductName() {

    const selectors = [

        '[itemprop="name"]',

        'meta[property="og:title"]',

        'meta[name="twitter:title"]',

        'h1',

        '[class*="product-title"]',

        '[class*="productTitle"]',

        '[class*="product_name"]',

        '[class*="productName"]'
    ];


    for (const selector of selectors) {

        const element =
            document.querySelector(selector);


        if (!element) {
            continue;
        }


        const value =
            element.getAttribute("content") ||
            element.textContent;


        if (value && value.trim()) {

            return value.trim();
        }
    }


    return document.title || "";
}


// ==========================================
// BRAND
// ==========================================

function getProductBrand() {

    const selectors = [

        '[itemprop="brand"]',

        'meta[property="product:brand"]',

        'meta[name="brand"]',

        '[class*="brand"]',

        '[class*="Brand"]'
    ];


    for (const selector of selectors) {

        const element =
            document.querySelector(selector);


        if (!element) {
            continue;
        }


        const value =
            element.getAttribute("content") ||
            element.textContent;


        if (value && value.trim()) {

            return value.trim();
        }
    }


    return "";
}


// ==========================================
// PRICE
// ==========================================

// ==========================================
// PRICE
// ==========================================

function getProductPrice() {

    // ------------------------------------------
    // 1. STRUCTURED PRICE DATA
    // ------------------------------------------

    const structuredSelectors = [

        '[itemprop="price"]',

        'meta[property="product:price:amount"]',

        'meta[itemprop="price"]',

        '[data-price]',

        '[data-testid*="price"]'

    ];


    for (const selector of structuredSelectors) {

        const elements =
            document.querySelectorAll(selector);


        for (const element of elements) {

            const value =
                element.getAttribute("content") ||
                element.getAttribute("data-price") ||
                element.textContent;


            if (!value) {
                continue;
            }


            const cleaned =
                value
                    .replace(/,/g, "")
                    .trim();


            // Accept structured numeric prices.
            // But reject obvious rating-like values.
            const match =
                cleaned.match(
                    /(?:₹|Rs\.?|INR)\s*(\d+(?:\.\d+)?)/i
                );


            if (match) {

                return match[1];
            }


            // If structured data contains only a number,
            // accept it unless it looks like a rating.
            if (
                /^\d+(?:\.\d+)?$/.test(cleaned) &&
                Number(cleaned) > 10
            ) {

                return cleaned;
            }
        }
    }


    // ------------------------------------------
    // 2. VISIBLE CURRENCY PRICES
    // ------------------------------------------

    const priceSelectors = [

        '[class*="price"]',

        '[class*="Price"]',

        '[class*="selling"]',

        '[class*="Selling"]',

        '[class*="amount"]',

        '[class*="Amount"]',

        '[class*="cost"]',

        '[class*="Cost"]'

    ];


    for (const selector of priceSelectors) {

        const elements =
            document.querySelectorAll(selector);


        for (const element of elements) {

            const value =
                element.textContent ||
                element.getAttribute("content") ||
                "";


            if (!value.trim()) {
                continue;
            }


            // IMPORTANT:
            // Require an actual currency marker here.
            // This prevents values such as 4.1
            // from being interpreted as the price.

            const match =
                value
                    .replace(/,/g, "")
                    .match(
                        /(?:₹|Rs\.?|INR)\s*(\d+(?:\.\d+)?)/i
                    );


            if (match) {

                const price =
                    Number(match[1]);


                if (price > 0) {

                    return match[1];
                }
            }
        }
    }


    return "";
}


// ==========================================
// CATEGORY
// ==========================================

function getProductCategory() {

    const selectors = [

        '[itemprop="category"]',

        'meta[property="product:category"]',

        'meta[name="category"]',

        '[class*="breadcrumb"]',

        '[class*="Breadcrumb"]'
    ];


    for (const selector of selectors) {

        const element =
            document.querySelector(selector);


        if (!element) {
            continue;
        }


        const value =
            element.getAttribute("content") ||
            element.textContent;


        if (value && value.trim()) {

            return value.trim();
        }
    }


    return "";
}


// ==========================================
// PRODUCT IMAGE
// ==========================================

function getProductImage() {

    const selectors = [

        'meta[property="og:image"]',

        'meta[name="twitter:image"]',

        '[itemprop="image"]',

        'img'
    ];


    for (const selector of selectors) {

        const element =
            document.querySelector(selector);


        if (!element) {
            continue;
        }


        const value =
            element.getAttribute("content") ||
            element.getAttribute("src") ||
            element.getAttribute("data-src");


        if (value && value.trim()) {

            return value.trim();
        }
    }


    return "";
}


// ==========================================
// MESSAGE FROM POPUP
// ==========================================

chrome.runtime.onMessage.addListener(
    (message, sender, sendResponse) => {

        if (!message || !message.type) {
            return;
        }


        // --------------------------------------
        // GET PRODUCT
        // --------------------------------------

        if (message.type === "GET_PRODUCT") {

            const product =
                getProductInformation();


            sendResponse({
                success: true,
                product: product
            });


            return true;
        }
    }
);