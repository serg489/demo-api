package com.demo.shared.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    private static final Logger log = LoggerFactory.getLogger(JsonHelper.class);
    ObjectMapper mapper = new ObjectMapper();

    public static List<JsonNode> parseJsonNodeList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<JsonNode> newNode = null;
        try {
            newNode = mapper.readValue(json, List.class);
        } catch (IOException e) {
            log.warn(String.format("Can't parse string to List<JsonNode>: %s", json), e);
        }
        return newNode;
    }

    public static JsonNode parseJsonNode(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = null;
        try {
            newNode = mapper.readTree(json);
        } catch (IOException e) {
            log.warn(String.format("Can't parse string to JsonNode: %s", json), e);
        }
        return newNode;
    }

    public static JsonNode parseJsonNode(CloseableHttpResponse response) {
        try {
            return parseJsonNode(response.getEntity().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode parseJsonNode(InputStream stream) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode newNode = null;
        try {
            newNode = mapper.readTree(stream);
        } catch (IOException e) {
            log.warn("Can't read data from input stream, it can be empty", e);
        }
        return newNode;
    }

    public static JsonNode updateNode(JsonNode node, Map<String, Map<String, String>> extraOptions) {
        ObjectNode editableNode = (ObjectNode) node;

        extraOptions.forEach((root, map) -> {
            JsonNode childNode = editableNode.at(root);
            ObjectNode editableChildNode = (ObjectNode) childNode;

            map.forEach(editableChildNode::put);
        });

        return node;
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject()) {
                merge(jsonNode, updateNode.get(fieldName));
            } else {
                if (mainNode instanceof ObjectNode) {
                    // Overwrite field
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).put(fieldName, value);
                }
            }
        }
        return mainNode;
    }
}
