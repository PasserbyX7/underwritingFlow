package com.shopee.demo.engine.type.engine;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;

/**
 * @author li.cao
 * @since 2022-04-21
 */
@AllArgsConstructor(staticName = "of")
public class EngineInput {

    private final JsonNode root;

    public JsonNode getInput() {
        return root.get("DAJSONDocument").get("INPUT");
    }

    public String convertToJson() {
        return root.toString();
    }

}
