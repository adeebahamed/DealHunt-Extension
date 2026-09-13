package com.dealhunt.marketplace;

import java.util.List;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;

public interface MarketplaceDataSource {

    String getMarketplace();

    List<ProductCandidate> fetchProducts(
            MarketplaceSearchQuery query
    );
}