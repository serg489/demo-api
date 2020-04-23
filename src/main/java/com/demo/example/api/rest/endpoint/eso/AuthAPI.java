package com.demo.example.api.rest.endpoint.eso;

import com.demo.api.rest.base.rest.BaseREST;
import com.demo.client.http.HttpClient;
import com.demo.shared.configuration.Config;
import com.demo.shared.utils.JsonHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AuthAPI extends BaseREST {

    private static final Logger LOG = LoggerFactory.getLogger(AuthAPI.class);
    public static final String URL = "/copdemo/api/login";
    private HttpClient client = new HttpClient(Config.getPropertyOrElse("api.base_url_demo", ""));

    public String getToken(String username, String password) {
        Map<String, Map<String, String>> extraOptions = new HashMap<>();
        Map<String, String> rootItems = new HashMap<>();
        rootItems.put("username", username);
        rootItems.put("password", password);
        extraOptions.put("", rootItems);

        String pathToTemplate = getClass().getResource("/api/eso/payloads/UserDataPayload.json").getPath();
        JsonNode node = payload(pathToTemplate, extraOptions);

        CloseableHttpResponse response = null;
        try {
            response = client.doPostwithParams(URL, node);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonNode jsonBody = JsonHelper.parseJsonNode(response);
        String token = jsonBody.get("access_token").asText();
        LOG.info(String.format("Token is %s", token));

        return token;
    }
}
