package com.dealhunt.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dealhunt.dto.DealSearchResponse;
import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.NormalizedProduct;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.dto.ProductMatchResult;
import com.dealhunt.dto.ProductSearchRequest;
import com.dealhunt.marketplace.MarketplaceSearchService;

@Service
public class DealSearchService {

    private final MarketplaceSearchService marketplaceSearchService;
    private final ProductMatchingService productMatchingService;

    public DealSearchService(
            MarketplaceSearchService marketplaceSearchService,
            ProductMatchingService productMatchingService) {

        this.marketplaceSearchService =
                marketplaceSearchService;

        this.productMatchingService =
                productMatchingService;
    }

    public DealSearchResponse search(
            ProductSearchRequest product) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("           DEALHUNT DEAL SEARCH");
        System.out.println("==========================================");

        if (product == null) {

            System.out.println(
                    "ERROR: Product request is null.");

            return new DealSearchResponse();
        }

        System.out.println(
                "Product name : " +
                        safe(product.getName()));

        System.out.println(
                "Brand        : " +
                        safe(product.getBrand()));

        System.out.println(
                "Model        : " +
                        safe(product.getModel()));

        System.out.println(
                "Variant      : " +
                        safe(product.getVariant()));

        System.out.println(
                "Category     : " +
                        safe(product.getCategory()));

        System.out.println(
                "Price        : " +
                        safe(product.getPrice()));

        System.out.println("------------------------------------------");

        String searchText =
                buildSearchText(product);

        System.out.println(
                "Generated search text: " +
                        searchText);

        List<MarketplaceSearchQuery> queries =
                buildMarketplaceQueries(
                        searchText);

        System.out.println(
                "Marketplace queries created: " +
                        queries.size());

        List<ProductCandidate> candidates =
                marketplaceSearchService.search(
                        queries);

        if (candidates == null) {
            candidates = new ArrayList<>();
        }

        System.out.println("------------------------------------------");

        System.out.println(
                "TOTAL MARKETPLACE CANDIDATES: " +
                        candidates.size());

        for (ProductCandidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            System.out.println(
                    safe(candidate.getMarketplace())
                            + " | "
                            + safe(candidate.getTitle())
                            + " | "
                            + safePrice(
                                    candidate.getPrice()));
        }

        System.out.println("------------------------------------------");

        if (candidates.isEmpty()) {

            System.out.println(
                    "NO PRODUCTS WERE RETURNED " +
                            "FROM MARKETPLACES.");

            System.out.println(
                    "==========================================");

            return createResponse(
                    product,
                    new ArrayList<>());
        }

        NormalizedProduct normalizedProduct =
                createNormalizedProduct(product);

        List<ProductCandidate> matchedProducts =
                new ArrayList<>();

        for (ProductCandidate candidate :
                candidates) {

            if (candidate == null) {
                continue;
            }

            try {

                ProductMatchResult matchResult =
                        productMatchingService.match(
                                normalizedProduct,
                                candidate);

                if (matchResult == null) {

                    candidate.setMatched(false);
                    candidate.setMatchScore(0.0);

                    candidate.setMatchReason(
                            "No matching result returned.");

                    matchedProducts.add(candidate);

                    continue;
                }

                boolean matched =
                        matchResult.isMatched();

                double score =
                        matchResult.getScore();

                String reason =
                        matchResult.getReason();

                candidate.setMatched(matched);
candidate.setMatchScore(score);
candidate.setMatchReason(reason);

System.out.println(
        "MATCH RESULT: "
                + safe(candidate.getTitle())
                + " | matched="
                + matched
                + " | score="
                + score
                + " | reason="
                + safe(reason));

if (matched) {
    matchedProducts.add(candidate);
}

            } catch (Exception e) {

                System.out.println(
                        "Matching error for "
                                + safe(candidate.getTitle())
                                + ": "
                                + e.getMessage());

                candidate.setMatched(false);
                candidate.setMatchScore(0.0);

                candidate.setMatchReason(
                        "Matching service error.");

                matchedProducts.add(candidate);
            }
        }

        System.out.println("------------------------------------------");

        System.out.println(
                "PRODUCTS AFTER MATCHING: "
                        + matchedProducts.size());

        List<ProductCandidate> finalResults =
                removeDuplicates(
                        matchedProducts);

        calculateDealInformation(
                product,
                finalResults);

        finalResults.sort(
                Comparator.comparing(
                        ProductCandidate::getDealRank,
                        Comparator.nullsLast(
                                Comparator.naturalOrder())));

        DealSearchResponse response =
                createResponse(
                        product,
                        finalResults);

        System.out.println("------------------------------------------");

        System.out.println(
                "FINAL RESULTS: "
                        + finalResults.size());

        if (response.getBestDeal() != null) {

            ProductCandidate bestDeal =
                    response.getBestDeal();

            System.out.println(
                    "BEST DEAL: "
                            + safe(bestDeal.getMarketplace())
                            + " | "
                            + safe(bestDeal.getTitle())
                            + " | "
                            + safePrice(
                                    bestDeal.getPrice()));

        } else {

            System.out.println(
                    "BEST DEAL: NONE");
        }

        System.out.println(
                "==========================================");

        return response;
    }

    private NormalizedProduct createNormalizedProduct(
            ProductSearchRequest product) {

        NormalizedProduct normalized =
                new NormalizedProduct();

        normalized.setName(
                product.getName());

        normalized.setBrand(
                product.getBrand());

        normalized.setModel(
                product.getModel());

        normalized.setVariant(
                product.getVariant());

        normalized.setCategory(
                product.getCategory());

        return normalized;
    }

    private String buildSearchText(
            ProductSearchRequest product) {

        List<String> parts =
                new ArrayList<>();

        String name =
                clean(product.getName());

        String brand =
                clean(product.getBrand());

        String model =
                clean(product.getModel());

        String variant =
                clean(product.getVariant());

        String category =
                clean(product.getCategory());

        if (!brand.isBlank() &&
                !containsIgnoreCase(
                        name,
                        brand)) {

            parts.add(brand);
        }

        if (!name.isBlank()) {
            parts.add(name);
        }

        if (!model.isBlank() &&
                !containsIgnoreCase(
                        name,
                        model)) {

            parts.add(model);
        }

        if (!variant.isBlank() &&
                !containsIgnoreCase(
                        name,
                        variant)) {

            parts.add(variant);
        }

        if (!category.isBlank() &&
                !containsIgnoreCase(
                        name,
                        category)) {

            parts.add(category);
        }

        return String.join(
                " ",
                parts);
    }

    private List<MarketplaceSearchQuery>
    buildMarketplaceQueries(
            String searchText) {

        List<MarketplaceSearchQuery> queries =
                new ArrayList<>();

        queries.add(
                new MarketplaceSearchQuery(
                        "Amazon",
                        searchText));

        queries.add(
                new MarketplaceSearchQuery(
                        "Flipkart",
                        searchText));

        queries.add(
                new MarketplaceSearchQuery(
                        "Myntra",
                        searchText));

        queries.add(
                new MarketplaceSearchQuery(
                        "Meesho",
                        searchText));

        return queries;
    }

    private List<ProductCandidate>
    removeDuplicates(
            List<ProductCandidate> products) {

        Map<String, ProductCandidate> unique =
                new LinkedHashMap<>();

        for (ProductCandidate product :
                products) {

            if (product == null) {
                continue;
            }

            String marketplace =
                    clean(product.getMarketplace());

            String title =
                    clean(product.getTitle());

            String key =
                    marketplace.toLowerCase()
                            + "|"
                            + title.toLowerCase();

            if (!unique.containsKey(key)) {

                unique.put(
                        key,
                        product);
            }
        }

        return new ArrayList<>(
                unique.values());
    }

    private void calculateDealInformation(
            ProductSearchRequest searchedProduct,
            List<ProductCandidate> products) {

        if (products == null ||
                products.isEmpty()) {

            return;
        }

        BigDecimal searchedPrice =
                parsePrice(
                        searchedProduct.getPrice());

        for (ProductCandidate product :
                products) {

            BigDecimal price =
                    product.getSalePrice();

            if (price == null) {
                price = product.getPrice();
            }

            if (price == null) {

                product.setAvailable(false);

                continue;
            }

            product.setAvailable(true);

            if (searchedPrice != null) {

                BigDecimal savings =
                        searchedPrice.subtract(price);

                if (savings.compareTo(
                        BigDecimal.ZERO) > 0) {

                    product.setSavings(
                            savings);

                } else {

                    product.setSavings(
                            BigDecimal.ZERO);
                }
            }

            BigDecimal originalPrice =
                    product.getOriginalPrice();

            if (originalPrice != null &&
                    originalPrice.compareTo(
                            BigDecimal.ZERO) > 0 &&
                    price.compareTo(
                            originalPrice) < 0) {

                BigDecimal discount =
                        originalPrice
                                .subtract(price)
                                .multiply(
                                        BigDecimal.valueOf(100))
                                .divide(
                                        originalPrice,
                                        2,
                                        RoundingMode.HALF_UP);

                product.setDiscountPercentage(
                        discount.doubleValue());
            }
        }

        List<ProductCandidate> ranked =
                new ArrayList<>();

        for (ProductCandidate product :
                products) {

            if (product.getPrice() != null &&
                    product.isAvailable()) {

                ranked.add(product);
            }
        }

ranked.sort(
        Comparator
                .comparing(
                        ProductCandidate::getMatchScore,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
                .thenComparing(
                        ProductCandidate::getPrice,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
);

        int rank = 1;

        for (ProductCandidate product :
                ranked) {

            product.setDealRank(rank++);
        }
    }

    private DealSearchResponse createResponse(
            ProductSearchRequest product,
            List<ProductCandidate> results) {

        DealSearchResponse response =
                new DealSearchResponse();

        response.setSearchedProduct(
                product);

        response.setResults(
                results);

        response.setBestDeal(
                findBestDeal(results));

        return response;
    }
private ProductCandidate findBestDeal(
        List<ProductCandidate> products) {

    if (products == null || products.isEmpty()) {
        return null;
    }

    return products.stream()
            .filter(product -> product != null)
            .filter(product -> product.isAvailable())
            .filter(product -> product.getPrice() != null)
            .max(
                    Comparator
                            .comparing(
                                    ProductCandidate::getMatchScore,
                                    Comparator.nullsFirst(
                                            Comparator.naturalOrder()
                                    )
                            )
                            .thenComparing(
                                    ProductCandidate::getPrice,
                                    Comparator.nullsLast(
                                            Comparator.reverseOrder()
                                    )
                            )
            )
            .orElse(null);
}


    private BigDecimal parsePrice(
            String price) {

        if (price == null ||
                price.isBlank()) {

            return null;
        }

        try {

            String cleaned =
                    price.replaceAll(
                            "[^0-9.]",
                            "");

            if (cleaned.isBlank()) {
                return null;
            }

            return new BigDecimal(cleaned);

        } catch (Exception e) {

            return null;
        }
    }

    private boolean containsIgnoreCase(
            String text,
            String value) {

        if (text == null ||
                value == null) {

            return false;
        }

        return text
                .toLowerCase()
                .contains(
                        value.toLowerCase());
    }

    private String clean(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .replaceAll(
                        "\\s+",
                        " ");
    }

    private String safe(
            String value) {

        if (value == null ||
                value.isBlank()) {

            return "";
        }

        return value;
    }

    private String safePrice(
            BigDecimal price) {

        if (price == null) {
            return "";
        }

        return price.toString();
    }
}