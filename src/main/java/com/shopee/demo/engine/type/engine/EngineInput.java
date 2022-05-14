package com.shopee.demo.engine.type.engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author li.cao
 * @since 2022-04-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EngineInput {

    private JsonNode root;

    @JsonIgnore
    public JsonNode getInput() {
        return root.get("DAJSONDocument").get("INPUT");
    }

    public String convertToJson() {
        return root.toString();
    }

}
