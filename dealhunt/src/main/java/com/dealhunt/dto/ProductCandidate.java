package com.dealhunt.dto;

import java.math.BigDecimal;

public class ProductCandidate {

    private String marketplace;
    private String title;
    private String brand;
    private String model;
    private String variant;

    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;

    private Double discountPercentage;
    private BigDecimal savings;

    private Integer dealRank;

    private String url;
    private String imageUrl;

    private boolean available;

    private Boolean matched;
    private Double matchScore;
    private String matchReason;

    private Boolean aiMatched;
    private Double aiConfidence;
    private String aiReason;

    private NormalizedProduct normalizedProduct;

    public ProductCandidate() {
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getSavings() {
        return savings;
    }

    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }

    public Integer getDealRank() {
        return dealRank;
    }

    public void setDealRank(Integer dealRank) {
        this.dealRank = dealRank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public Boolean getAiMatched() {
        return aiMatched;
    }

    public void setAiMatched(Boolean aiMatched) {
        this.aiMatched = aiMatched;
    }

    public Double getAiConfidence() {
        return aiConfidence;
    }

    public void setAiConfidence(Double aiConfidence) {
        this.aiConfidence = aiConfidence;
    }
    public String getAiReason() {
        return aiReason;
    }
    public void setAiReason(String aiReason) {
        this.aiReason = aiReason;
    }

    public NormalizedProduct getNormalizedProduct() {
        return normalizedProduct;
    }

    public void setNormalizedProduct(
            NormalizedProduct normalizedProduct) {

        this.normalizedProduct = normalizedProduct;
    }
}