package com.dealhunt.marketplace;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.dealhunt.dto.ProductCandidate;

@Component
public class ProductResultMapper {

    public ProductCandidate create(
            String marketplace,
            String title,
            String url,
            BigDecimal price) {

        ProductCandidate product =
                new ProductCandidate();

        product.setMarketplace(marketplace);
        product.setTitle(title);
        product.setUrl(url);
        product.setPrice(price);
        product.setSalePrice(price);
        product.setAvailable(true);

        return product;
    }
}