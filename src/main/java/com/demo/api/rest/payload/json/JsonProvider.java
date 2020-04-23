package com.demo.api.rest.payload.json;

import com.demo.shared.utils.JsonHelper;
import com.demo.shared.utils.Template;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JsonProvider.class);

    public JsonNode payload(String templatePath, Map<String, Map<String, String>> params) {
        JsonNode template = Template.readTemplate(templatePath);
        JsonHelper.updateNode(template, params);

        LOG.info(String.format("Template: %s", template.toString()));
        return template;
    }

    public JsonNode payload(JsonNode basic, JsonNode params) {
        return JsonHelper.merge(basic, params);
    }
}
