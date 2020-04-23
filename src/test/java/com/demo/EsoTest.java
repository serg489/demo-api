package com.demo;

import com.demo.api.rest.base.test.BaseRestTest;
import com.demo.example.api.rest.endpoint.eso.AuthAPI;
import com.demo.example.api.rest.endpoint.eso.ChartsAPI;
import com.demo.example.api.rest.endpoint.eso.ContainersAPI;
import com.demo.shared.utils.JsonHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EsoTest extends BaseRestTest {

    private static final String LOGIN = "52052";
    private static final String PASSWORD = "tutorial";
    private static final String CONTAINER_ID = "1448455";
    private static final String TOKEN = new AuthAPI().getToken(LOGIN, PASSWORD);

    private static final Logger LOG = LoggerFactory.getLogger(EsoTest.class);
    private ContainersAPI containersAPI = new ContainersAPI();
    private ChartsAPI chartsAPI = new ChartsAPI();

    @Test
    public void deleteTest() throws IOException {
        LOG.info("1. Create an observing block");
        CloseableHttpResponse response = containersAPI.createObservingBlock(CONTAINER_ID, TOKEN);
        JsonNode jsonBody = JsonHelper.parseJsonNode(response);
        final String obId = jsonBody.get("obId").asText();

        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        File file = File.createTempFile("temp", ".jpeg");
        ImageIO.write(bufferedImage, "jpeg", file);

        CloseableHttpResponse response1 = containersAPI.uploadChartsOB(obId, file, TOKEN);

        LOG.info("2. Attach it to chart");
        chartsAPI.deleteChart(obId, "1", TOKEN);
    }
}
