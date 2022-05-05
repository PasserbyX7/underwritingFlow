package com.shopee.demo.infrastructure.data;

import lombok.Data;

@Data
public class BaseDO {
    private Long id;

    private Long createDate;

    private Long modifyDate;

    private String creator;

    private String modifier;

    private Integer version;
}
