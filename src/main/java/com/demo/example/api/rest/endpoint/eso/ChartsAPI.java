package com.demo.example.api.rest.endpoint.eso;

import com.demo.api.rest.base.rest.BaseREST;
import com.demo.client.http.HttpClient;
import com.demo.shared.configuration.Config;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

public class ChartsAPI extends BaseREST {

    public static final String URL = "/copdemo/api/v1/obsBlocks/%s/findingCharts/%s";
    private HttpClient client = new HttpClient(Config.getPropertyOrElse("api.base_url_demo", ""));

    public CloseableHttpResponse deleteChart(String obId, String chartNumber, String token) {
        String bearer = String.format("Bearer %s", token);
        Header[] headers = {new BasicHeader("Authorization", bearer), new BasicHeader("Content-Type", "application/json")};
        return client.doDelete(String.format(URL, obId, chartNumber), headers);
    }
}
