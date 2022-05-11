package com.shopee.demo.engine.type.engine;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;

/**
 * @author li.cao
 * @since 2022-04-22
 */
@AllArgsConstructor(staticName = "of")
public class EngineOutput {

    private final JsonNode root;

    public JsonNode getInput() {
        return root.get("DAJSONDocument").get("OUTPUT");
    }

    public String convertToJson() {
        return root.toString();
    }

}
