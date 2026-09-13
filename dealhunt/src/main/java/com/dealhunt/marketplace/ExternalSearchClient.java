package com.dealhunt.marketplace;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.dealhunt.config.MarketplaceProperties;

@Component
public class ExternalSearchClient {

    private final RestClient restClient;
    private final MarketplaceProperties properties;

    public ExternalSearchClient(
            RestClient restClient,
            MarketplaceProperties properties) {

        this.restClient = restClient;
        this.properties = properties;
    }

    public String search(String query) {

        System.out.println(
                "==========================================");

        System.out.println(
                "SERPAPI SEARCH");

        System.out.println(
                "Query: " + query);

        System.out.println(
                "API URL: " +
                        properties.getSearchApiUrl());

        System.out.println(
                "API key configured: " +
                        (properties.getSearchApiKey() != null &&
                         !properties.getSearchApiKey().isBlank()));

        System.out.println(
                "==========================================");

        String response =
                restClient
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("serpapi.com")
                                .path("/search")
                                .queryParam(
                                        "engine",
                                        "google_shopping")
                                .queryParam(
                                        "q",
                                        query)
                                .queryParam(
                                        "location",
                                        "India")
                                .queryParam(
                                        "hl",
                                        "en")
                                .queryParam(
                                        "gl",
                                        "in")
                                .queryParam(
                                        "api_key",
                                        properties.getSearchApiKey())
                                .build())
                        .retrieve()
                        .body(String.class);

        System.out.println(
                "SerpApi response received: " +
                        (response != null));

        if (response != null) {

            System.out.println(
                    "Response length: " +
                            response.length());
        }

        return response;
    }
}