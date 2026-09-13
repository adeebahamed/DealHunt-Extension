package com.dealhunt.marketplace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dealhunt.dto.ProductCandidate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShoppingResultParser {

    private final ObjectMapper objectMapper;

    public ShoppingResultParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ProductCandidate> parse(
            String marketplace,
            String json) {

        List<ProductCandidate> products =
                new ArrayList<>();

        if (json == null || json.isBlank()) {
            return products;
        }

        try {
            JsonNode root =
                    objectMapper.readTree(json);

            JsonNode results =
                    root.path("shopping_results");

            if (!results.isArray()) {
                return products;
            }

            for (JsonNode item : results) {

                String title =
                        item.path("title")
                                .asText("");

                String source =
                        item.path("source")
                                .asText("");

                String link =
                        item.path("link")
                                .asText("");

                String priceText =
                        item.path("extracted_price")
                                .asText("");

                if (title.isBlank()) {
                    continue;
                }

                if (source.isBlank()) {
                    source = marketplace;
                }

                BigDecimal price =
                        parsePrice(priceText);

                ProductCandidate product =
                        new ProductCandidate();

                product.setMarketplace(source);
                product.setTitle(title);
                product.setUrl(link);
                product.setPrice(price);
                product.setSalePrice(price);
                product.setAvailable(true);

                products.add(product);
            }

        } catch (Exception e) {

            System.out.println(
                    "Shopping result parsing error: "
                            + e.getMessage());
        }

        return products;
    }

    private BigDecimal parsePrice(
            String priceText) {

        if (priceText == null ||
                priceText.isBlank()) {

            return null;
        }

        try {
            return BigDecimal.valueOf(
                    Double.parseDouble(priceText));
        } catch (Exception e) {
            return null;
        }
    }
}