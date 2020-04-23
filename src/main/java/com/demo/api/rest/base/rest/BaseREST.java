package com.demo.api.rest.base.rest;


import com.demo.api.BaseAPI;
import com.demo.api.rest.payload.PayloadProvider;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BaseREST extends BaseAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BaseREST.class);

    protected JsonNode payload(String path, Map<String, Map<String, String>> params) {
        return new PayloadProvider().asJson().payload(path, params);
    }

    protected JsonNode payload(String path) {
        return new PayloadProvider().asJson().payload(path, new HashMap<>());
    }

}
