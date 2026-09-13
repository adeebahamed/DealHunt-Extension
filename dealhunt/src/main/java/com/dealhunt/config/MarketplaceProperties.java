package com.dealhunt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dealhunt.marketplace")
public class MarketplaceProperties {
    private String searchApiUrl;
    private String searchApiKey;
    public String getSearchApiUrl() {
        return searchApiUrl;
    }
    public void setSearchApiUrl(String searchApiUrl) {
        this.searchApiUrl = searchApiUrl;
    }
    public String getSearchApiKey() {
        return searchApiKey;
    }

    public void setSearchApiKey(String searchApiKey) {
        this.searchApiKey = searchApiKey;
    }
}