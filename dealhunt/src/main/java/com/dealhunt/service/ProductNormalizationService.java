package com.dealhunt.service;

import com.dealhunt.dto.NormalizedProduct;
import com.dealhunt.dto.ProductSearchRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductNormalizationService {

    public NormalizedProduct normalize(
            ProductSearchRequest request) {

        NormalizedProduct product =
                new NormalizedProduct();

        if (request == null) {
            return product;
        }

        product.setName(
                clean(request.getName())
        );

        product.setBrand(
                clean(request.getBrand())
        );

        product.setModel(
                clean(request.getModel())
        );

        product.setVariant(
                clean(request.getVariant())
        );

        product.setCategory(
                clean(request.getCategory())
        );

        return product;
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