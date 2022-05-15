package com.shopee.demo.engine.type.engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.shopee.demo.infrastructure.utils.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author li.cao
 * @since 2022-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EngineOutput {

    private JsonNode root;

    @JsonIgnore
    public JsonNode getInput() {
        return root.get("DAJSONDocument").get("OUTPUT");
    }

    public String convertToJson() {
        return root.toString();
    }

    public static EngineOutput of(String content) {
        return EngineOutput.of(JsonUtils.readTree(content));
    }

}
