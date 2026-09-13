package com.dealhunt.marketplace.amazon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.marketplace.ExternalSearchClient;
import com.dealhunt.marketplace.MarketplaceDataSource;
import com.dealhunt.marketplace.ShoppingResultParser;

@Component
public class AmazonDataSource
        implements MarketplaceDataSource {

    private final ExternalSearchClient searchClient;
    private final ShoppingResultParser resultParser;

    public AmazonDataSource(
            ExternalSearchClient searchClient,
            ShoppingResultParser resultParser) {

        this.searchClient = searchClient;
        this.resultParser = resultParser;
    }

    @Override
    public String getMarketplace() {
        return "Amazon";
    }

    @Override
    public List<ProductCandidate> fetchProducts(
            MarketplaceSearchQuery query) {

        List<ProductCandidate> products =
                new ArrayList<>();

        if (query == null ||
                query.getSearchText() == null ||
                query.getSearchText().isBlank()) {

            System.out.println(
                    "Amazon query is empty.");

            return products;
        }

        try {

            System.out.println(
                    "Amazon search text: " +
                            query.getSearchText());

            String response =
                    searchClient.search(
                            query.getSearchText());

            products =
                    resultParser.parse(
                            "Amazon",
                            response);

            System.out.println(
                    "Amazon parsed products: " +
                            products.size());

        } catch (Exception e) {

            System.out.println(
                    "Amazon data source error: " +
                            e.getMessage());
        }

        return products;
    }
}