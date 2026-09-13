package com.dealhunt.marketplace;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;

import java.util.List;

public interface MarketplaceProvider {

    String getMarketplace();

    List<ProductCandidate> search(
            MarketplaceSearchQuery query
    );
}