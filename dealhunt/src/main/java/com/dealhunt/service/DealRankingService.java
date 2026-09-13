package com.dealhunt.service;

import com.dealhunt.dto.ProductCandidate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class DealRankingService {

    public List<ProductCandidate> rank(
            List<ProductCandidate> products) {

        if (products == null ||
                products.isEmpty()) {

            return products;
        }

        products.sort(
                Comparator
                        .comparing(
                                ProductCandidate::getPrice,
                                Comparator.nullsLast(
                                        BigDecimal::compareTo
                                )
                        )
        );

        int rank = 1;

        for (ProductCandidate product : products) {

            product.setDealRank(rank++);

            calculateSavings(product);
            calculateDiscount(product);
        }

        return products;
    }

    public ProductCandidate findBestDeal(
            List<ProductCandidate> products) {

        if (products == null ||
                products.isEmpty()) {

            return null;
        }

        return products.stream()
                .filter(ProductCandidate::isAvailable)
                .filter(product ->
                        product.getPrice() != null)
                .filter(product ->
                        Boolean.TRUE.equals(
                                product.getMatched()
                        ))
                .min(
                        Comparator.comparing(
                                ProductCandidate::getPrice
                        )
                )
                .orElse(null);
    }

    private void calculateSavings(
            ProductCandidate product) {

        if (product == null ||
                product.getOriginalPrice() == null ||
                product.getPrice() == null) {

            return;
        }

        BigDecimal savings =
                product.getOriginalPrice()
                        .subtract(
                                product.getPrice()
                        );

        if (savings.compareTo(
                BigDecimal.ZERO) > 0) {

            product.setSavings(savings);

        } else {

            product.setSavings(
                    BigDecimal.ZERO
            );
        }
    }

    private void calculateDiscount(
            ProductCandidate product) {

        if (product == null ||
                product.getOriginalPrice() == null ||
                product.getPrice() == null) {

            return;
        }

        if (product.getOriginalPrice()
                .compareTo(BigDecimal.ZERO) <= 0) {

            return;
        }

        BigDecimal difference =
                product.getOriginalPrice()
                        .subtract(
                                product.getPrice()
                        );

        double discount =
                difference
                        .divide(
                                product.getOriginalPrice(),
                                4,
                                java.math.RoundingMode.HALF_UP
                        )
                        .doubleValue()
                        * 100;

        product.setDiscountPercentage(
                Math.max(0, discount)
        );
    }
}