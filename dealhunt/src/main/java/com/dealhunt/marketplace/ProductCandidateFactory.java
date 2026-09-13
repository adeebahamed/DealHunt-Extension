package com.dealhunt.marketplace;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.dealhunt.dto.ProductCandidate;

@Component
public class ProductCandidateFactory {

    public ProductCandidate create(
            String marketplace,
            String title,
            String brand,
            String model,
            String variant,
            BigDecimal price,
            BigDecimal originalPrice,
            String url,
            String imageUrl) {

        ProductCandidate candidate =
                new ProductCandidate();

        candidate.setMarketplace(
                marketplace
        );

        candidate.setTitle(
                clean(title)
        );

        candidate.setBrand(
                clean(brand)
        );

        candidate.setModel(
                clean(model)
        );

        candidate.setVariant(
                clean(variant)
        );

        candidate.setPrice(
                price
        );

        candidate.setSalePrice(
                price
        );

        candidate.setOriginalPrice(
                originalPrice
        );

        candidate.setUrl(
                clean(url)
        );

        candidate.setImageUrl(
                clean(imageUrl)
        );

        candidate.setAvailable(
                price != null
        );

        return candidate;
    }

    private String clean(String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .replaceAll("\\s+", " ");
    }
}