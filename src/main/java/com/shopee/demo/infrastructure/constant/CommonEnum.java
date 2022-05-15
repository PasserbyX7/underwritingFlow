package com.shopee.demo.infrastructure.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Li Cao
 * @since 2021-11-28 21:01:10
 */
public interface CommonEnum {

    Integer getCode();

    String getName();

    public static <T extends CommonEnum> T of(Integer code, Class<T> enumClass) {
        if (code == null) {
            return null;
        }
        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(code, e.getCode())) {
                return e;
            }
        }
        return null;
    }

    public static <T extends CommonEnum> T of(String name, Class<T> enumClass) {
        for (T e : enumClass.getEnumConstants()) {
            if (Objects.equals(name, e.getName())) {
                return e;
            }
        }
        return null;
    }

    public static <T extends CommonEnum> List<String> getValues(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(T::getName)
                .collect(Collectors.toList());
    }
}
