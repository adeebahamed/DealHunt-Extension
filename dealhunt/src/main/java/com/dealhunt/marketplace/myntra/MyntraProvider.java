package com.dealhunt.marketplace.myntra;

import com.dealhunt.dto.MarketplaceSearchQuery;
import com.dealhunt.dto.ProductCandidate;
import com.dealhunt.marketplace.MarketplaceProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyntraProvider implements MarketplaceProvider {

    @Override
    public String getMarketplace() {
        return "Myntra";
    }

    @Override
    public List<ProductCandidate> search(
            MarketplaceSearchQuery query) {

        return new ArrayList<>();
    }
}