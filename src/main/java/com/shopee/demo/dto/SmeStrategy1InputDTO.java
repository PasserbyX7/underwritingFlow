package com.shopee.demo.dto;

import com.shopee.demo.strategy.StrategyInput;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmeStrategy1InputDTO implements StrategyInput {
    private String token;
}
