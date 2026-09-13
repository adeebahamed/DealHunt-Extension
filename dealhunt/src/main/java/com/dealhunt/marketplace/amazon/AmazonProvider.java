package com.dealhunt.marketplace.amazon;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.marketplace.MarketplaceProvider;

@Component
public class AmazonProvider implements MarketplaceProvider {

    private final AmazonDataSource dataSource;

    public AmazonProvider(
            AmazonDataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public String getMarketplace() {
        return "Amazon";
    }

    @Override
    public List<ProductCandidate> search(
            MarketplaceSearchQuery query) {

        return dataSource.fetchProducts(query);
    }
}