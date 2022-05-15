package com.shopee.demo.engine.type.strategy.sme.input.strategy1;

import com.shopee.demo.engine.constant.input.BoolEnum;
import com.shopee.demo.engine.type.strategy.StrategyInput;
import com.shopee.demo.infrastructure.config.JsonEnumCodeSerializerConfig;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Data
public class SmeStrategy1CreditRiskInput implements StrategyInput {

    @JsonProperty("uw")
    private UnderwritingInfo underwritingInfo;

    @JsonProperty("corp1")
    private CorpInfo corpInfo;

    @JsonProperty("guar1")
    private List<GuarInfo> guarInfoList;

    @Data
    public static class UnderwritingInfo {

        @JsonProperty("underwriting_request_id")
        private String underwritingRequestId;

    }

    @Data
    public static class CorpInfo {

        @JsonProperty("sme_blacklist_flag")
        @JsonSerialize(using = JsonEnumCodeSerializerConfig.class)
        private BoolEnum blackListFlag;

        @JsonProperty("maribank_overdue_count")
        private Integer overdueCount;

        @JsonProperty("maribank_overdue_60_count")
        private Integer overdue60Count;

        @JsonProperty("maribank_max_overdue_day")
        private Integer maxOverdueDay;

    }

    @Data
    public static class GuarInfo {

        @JsonProperty("blacklist")
        @JsonSerialize(using = JsonEnumCodeSerializerConfig.class)
        private BoolEnum blackListFlag;

    }
}
