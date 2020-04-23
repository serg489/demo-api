package com.demo.client.http;

import com.demo.shared.utils.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private static CloseableHttpClient client = HttpClients.createDefault();
    private static ObjectMapper objectMapper = new ObjectMapper();

    protected String baseUrl = "";

    private List<Header> headersToAdd = new ArrayList<>();

    public HttpClient() {

    }

    public HttpClient(String baseUrl) {
        headersToAdd.add(new BasicHeader("Accept", "*/*"));
        headersToAdd.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        this.baseUrl = baseUrl;
    }

    public CloseableHttpResponse doGet(String url) {
        String fullUrl = buildUrl(url);
        HttpRequestBase httpGet = new HttpGet(fullUrl);

        return invoke(httpGet);
    }

    public CloseableHttpResponse doPost(String url, String body) {
        return doPost(url, JsonHelper.parseJsonNode(body), null);
    }

    public CloseableHttpResponse doPost(String url, JsonNode body) {
        return doPost(url, body, null);
    }

    public CloseableHttpResponse doPost(String url, String body, Header[] headers) {
        return doPost(url, JsonHelper.parseJsonNode(body), headers);
    }

    public CloseableHttpResponse doPost(String url, JsonNode body, Header[] headers) {
        String fullUrl = buildUrl(url);
        HttpPost httpPost = new HttpPost(fullUrl);

        try {
            httpPost.setEntity(new ByteArrayEntity(objectMapper.writeValueAsBytes(body)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (headers != null && headers.length > 0) {
            Stream.of(headers).forEach(httpPost::addHeader);
        }

        return invoke(httpPost);
    }

    public CloseableHttpResponse doDelete(String url, Header[] headers) {
        String fullUrl = buildUrl(url);
        HttpDelete httpPost = new HttpDelete(fullUrl);

        if (headers != null && headers.length > 0) {
            Stream.of(headers).forEach(httpPost::addHeader);
        }
        return invoke(httpPost);
    }

    public CloseableHttpResponse doPostwithParams(String url, JsonNode body) throws UnsupportedEncodingException {
        String fullUrl = buildUrl(url);
        HttpPost httpPost = new HttpPost(fullUrl);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", body.get("username").asText()));
        nameValuePairs.add(new BasicNameValuePair("password",body.get("password").asText()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        return invoke(httpPost);
    }

    public CloseableHttpResponse doPostUploadFile(String url, File file, Header[] headers) {
        String fullUrl = buildUrl(url);
        HttpPost httpPost = new HttpPost(fullUrl);

        httpPost.setEntity(new FileEntity(file.getAbsoluteFile()));

        if (headers != null && headers.length > 0) {
            Stream.of(headers).forEach(httpPost::addHeader);
        }

        return invoke(httpPost);
    }

    public void setBaseUrl(String newUrl) {
        this.baseUrl = newUrl;
    }

    public void setHeader(String header, String value) {
        headersToAdd.add(new BasicHeader(header, value));
    }

    private String buildUrl(String url) {
        return baseUrl + url;
    }

    private CloseableHttpResponse invoke(HttpRequestBase http) {
        headersToAdd.forEach(hdr -> http.addHeader(hdr));

        try {
            CloseableHttpResponse response = client.execute(http);
            if (http instanceof HttpPost) {
                InputStream inputStream = ((HttpPost) http).getEntity().getContent();
                String payload = IOUtils.toString(inputStream, Charset.defaultCharset()).trim().replaceAll(" +", " ");

                HttpEntity respBody = response.getEntity();
                String responseBody = StreamUtils.copyToString(respBody.getContent(), Charset.defaultCharset());
                response.setEntity(new StringEntity(responseBody));
                LOG.info(String.format("Sending request:\n[%s] %s => [%s] \nPayload:\n%s \nResponse:\n%s", http.getMethod(), http.getURI(), response.getStatusLine(), payload, responseBody));
            } else {
                LOG.info(String.format("Sending request:\n[%s] %s => [%s]", http.getMethod(), http.getURI(), response.getStatusLine()));
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException("HTTP request failed: " + http.toString(), e);
        }
    }
}
