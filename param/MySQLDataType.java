package com.example.demo.param;

public enum MySQLDataType {
    // 数值类型
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    BIGINT,
    FLOAT,
    DOUBLE,
    DECIMAL,

    // 日期和时间类型
    DATE,
    TIME,
    DATETIME,
    TIMESTAMP,
    YEAR,

    // 字符串类型
    CHAR,
    VARCHAR,
    BINARY,
    VARBINARY,
    BLOB,
    TINYBLOB,
    MEDIUMBLOB,
    LONGBLOB,
    TEXT,
    TINYTEXT,
    MEDIUMTEXT,
    LONGTEXT,

    // 枚举和集合类型
    ENUM,
    SET;
}
