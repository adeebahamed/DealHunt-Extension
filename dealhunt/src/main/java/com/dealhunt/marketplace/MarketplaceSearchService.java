package com.dealhunt.marketplace;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketplaceSearchService {

    private final List<MarketplaceProvider> providers;

    public MarketplaceSearchService(
            List<MarketplaceProvider> providers) {

        this.providers = providers;
    }

    public List<ProductCandidate> search(
            List<MarketplaceSearchQuery> queries) {

        List<ProductCandidate> products =
                new ArrayList<>();

        if (queries == null ||
                queries.isEmpty()) {

            return products;
        }

        for (MarketplaceSearchQuery query : queries) {

            if (query == null) {
                continue;
            }

            MarketplaceProvider provider =
                    findProvider(
                            query.getMarketplace()
                    );

            if (provider == null) {
                continue;
            }

            try {

                List<ProductCandidate> results =
                        provider.search(query);

                if (results != null) {
                    products.addAll(results);
                }

            } catch (Exception exception) {

                System.out.println(
                        "Error searching "
                                + provider.getMarketplace()
                );

                System.out.println(
                        exception.getMessage()
                );
            }
        }

        return products;
    }

    private MarketplaceProvider findProvider(
            String marketplace) {

        if (marketplace == null ||
                marketplace.isBlank()) {

            return null;
        }

        return providers.stream()
                .filter(provider ->
                        provider.getMarketplace()
                                .equalsIgnoreCase(
                                        marketplace
                                )
                )
                .findFirst()
                .orElse(null);
    }
}