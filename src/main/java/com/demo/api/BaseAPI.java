package com.demo.api;


import com.demo.api.rest.payload.PayloadProvider;
import com.demo.client.http.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAPI.class);

    protected PayloadProvider payload = new PayloadProvider();
    protected HttpClient client;

    protected JsonNode initTemplate(String templatePath, Map<String, Map<String, String>> params) {
        JsonNode template = payload.asJson().payload(templatePath, params);
        LOG.info(String.format("Template: %s", template.toString()));
        return template;
    }

    protected void setAuth(String token) {
        client.setHeader(HttpHeaders.AUTHORIZATION, token);
    }

}
