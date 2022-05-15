CREATE TABLE `underwriting_flow_tab` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `underwriting_id` varchar(128) NOT NULL COMMENT '授信唯一id',
    `underwriting_type` varchar(16) NOT NULL COMMENT '授信类型',
    `flow_status` varchar(16) NOT NULL COMMENT '授信flow状态',
    `current_strategy` varchar(16) NULL COMMENT '当前授信策略',
    `strategy_status` varchar(16) NULL COMMENT '当前授信状态',
    `suspend_data_source` varchar(16) NULL COMMENT '当前策略不可用数据源',
    `error_msg` varchar(128) NULL COMMENT '当前策略错误信息',
    `strategy_input` text NULL COMMENT '策略输入',
    `strategy_output` text NULL COMMENT '策略输出',
    `creator` varchar(64) NULL COMMENT '创建人',
    `create_date` bigint(20) unsigned NULL COMMENT '创建时间',
    `modifier` varchar(64) NULL COMMENT '修改人',
    `modify_date` bigint(20) unsigned NULL COMMENT '修改时间',
    `version` int(11) NULL COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `key_underwriting_id` (underwriting_id)
) ENGINE = InnoDB DEFAULT CHARSET = UTF8MB4;

CREATE TABLE `underwriting_flow_log_tab` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `underwriting_flow_id` bigint(20) unsigned NOT NULL COMMENT '授信flow id',
    `flow_status` varchar(16) NOT NULL COMMENT '授信flow状态',
    `current_strategy` varchar(16) NULL COMMENT '当前授信策略',
    `strategy_status` varchar(16) NULL COMMENT '当前授信状态',
    `suspend_data_source` varchar(16) NULL COMMENT '当前策略不可用数据源',
    `terminal_reason` varchar(128) NULL COMMENT '当前策略终止理由',
    `strategy_input` text NULL COMMENT '策略输入',
    `strategy_output` text NULL COMMENT '策略输出',
    `creator` varchar(64) NULL COMMENT '创建人',
    `create_date` bigint(20) unsigned NULL COMMENT '创建时间',
    `modifier` varchar(64) NULL COMMENT '修改人',
    `modify_date` bigint(20) unsigned NULL COMMENT '修改时间',
    `version` int(11) NULL COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `key_flow_id_create_date` (underwriting_flow_id, create_date)
) ENGINE = InnoDB DEFAULT CHARSET = UTF8MB4;

CREATE TABLE `sme_underwriting_tab` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `underwriting_id` varchar(128) NOT NULL COMMENT '授信唯一id',
    `request_date` bigint(20) unsigned NULL COMMENT '授信创建时间',
    `request_expire_date` bigint(20) unsigned NULL COMMENT '授信超时时间',
    `smeData` text NULL COMMENT 'sme数据',
    `creator` varchar(64) NULL COMMENT '创建人',
    `create_date` bigint(20) unsigned NULL COMMENT '创建时间',
    `modifier` varchar(64) NULL COMMENT '修改人',
    `modify_date` bigint(20) unsigned NULL COMMENT '修改时间',
    `version` int(11) NULL COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = UTF8MB4;