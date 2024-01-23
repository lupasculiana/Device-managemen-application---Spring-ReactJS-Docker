package com.utcn.app.config;

import org.springframework.stereotype.Component;

@Component
public class UrlConfig {
    private final String userMicroserviceUrl = "http://customer-microservice-container:8081/api/v1/customers/";

    public String getUserMicroserviceUrl() {
        return this.userMicroserviceUrl;
    }
}