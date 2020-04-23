package com.demo.example.api.rest.endpoint.eso;

import com.demo.api.rest.base.rest.BaseREST;
import com.demo.client.http.HttpClient;
import com.demo.shared.configuration.Config;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import java.io.File;

public class ContainersAPI extends BaseREST {

    public static final String URL = "/copdemo/api/v1/containers/%s/items";
    private HttpClient client = new HttpClient(Config.getPropertyOrElse("api.base_url_demo", ""));

    public CloseableHttpResponse createObservingBlock(String containerId, String token) {
        String pathToTemplate = getClass().getResource("/api/eso/payloads/ObPayload.json").getPath();
        JsonNode node = payload(pathToTemplate);
        String bearer = String.format("Bearer %s", token);
        Header[] headers = {new BasicHeader("Authorization", bearer), new BasicHeader("Content-Type", "application/json")};
        return client.doPost(String.format(URL, containerId), node.toString(), headers);
    }

    public CloseableHttpResponse uploadChartsOB(String ob, File file, String token) {
        String bearer = String.format("Bearer %s", token);
        Header[] headers = {new BasicHeader("Authorization", bearer),new BasicHeader("Content-Type", "image/jpeg" ), new BasicHeader("Content-Disposition", "inline; filename="+file.getName())};
        return client.doPostUploadFile(String.format("/copdemo/api/v1/obsBlocks/%s/findingCharts", ob), file, headers);
    }
}
