package com.dealhunt.marketplace.flipkart;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.marketplace.MarketplaceProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlipkartProvider implements MarketplaceProvider {

    @Override
    public String getMarketplace() {
        return "Flipkart";
    }

    @Override
    public List<ProductCandidate> search(
            MarketplaceSearchQuery query) {

        return new ArrayList<>();
    }
}