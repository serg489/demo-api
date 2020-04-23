package com.demo.api.rest.payload;


import com.demo.api.rest.payload.json.JsonProvider;

public class PayloadProvider {

    private JsonProvider json = new JsonProvider();

    public JsonProvider asJson() {
        return json;
    }
}
