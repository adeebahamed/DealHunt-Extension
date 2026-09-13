package com.dealhunt.dto;

public class MarketplaceSearchQuery {

    private String marketplace;
    private String searchText;

    public MarketplaceSearchQuery() {
    }

    public MarketplaceSearchQuery(
            String marketplace,
            String searchText) {

        this.marketplace = marketplace;
        this.searchText = searchText;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}