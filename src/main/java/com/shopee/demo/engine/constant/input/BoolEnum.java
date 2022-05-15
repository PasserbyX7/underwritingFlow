package com.shopee.demo.engine.constant.input;

import com.shopee.demo.infrastructure.constant.CommonEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author li.cao
 * @since 2022-02-23 15:29:38
 */
@Getter
@AllArgsConstructor
public enum BoolEnum implements CommonEnum {
    /**
     * yes
     */
    YES(1, "Y"),
    /**
     * no
     */
    NO(0, "N");

    private Integer code;
    private String name;

    public static BoolEnum of(boolean flag) {
        return flag ? YES : NO;
    }

    public static BoolEnum of(Integer code) {
        return CommonEnum.of(code, BoolEnum.class);
    }

    public static BoolEnum of(String name) {
        return CommonEnum.of(name, BoolEnum.class);
    }

    @Override
    public String toString() {
        return name;
    }
}
