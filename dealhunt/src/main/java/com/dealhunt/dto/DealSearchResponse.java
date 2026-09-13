package com.dealhunt.dto;

import java.util.ArrayList;
import java.util.List;

public class DealSearchResponse {

    private ProductSearchRequest searchedProduct;

    private List<ProductCandidate> results =
            new ArrayList<>();

    private ProductCandidate bestDeal;

    public DealSearchResponse() {
    }

    public ProductSearchRequest getSearchedProduct() {
        return searchedProduct;
    }

    public void setSearchedProduct(
            ProductSearchRequest searchedProduct) {

        this.searchedProduct = searchedProduct;
    }

    public List<ProductCandidate> getResults() {
        return results;
    }

    public void setResults(
            List<ProductCandidate> results) {

        this.results = results;
    }

    public ProductCandidate getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(
            ProductCandidate bestDeal) {

        this.bestDeal = bestDeal;
    }
}