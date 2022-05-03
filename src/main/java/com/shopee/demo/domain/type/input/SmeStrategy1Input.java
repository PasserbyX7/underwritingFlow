package com.shopee.demo.domain.type.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmeStrategy1Input implements StrategyInput {
    private String token;
}
