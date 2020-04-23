package com.demo.shared.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Template {

    public static JsonNode readTemplate(String filePath) {
        File file = new File(filePath);
        InputStream targetStream = null;

        try {
            targetStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return JsonHelper.parseJsonNode(targetStream);
    }


}
