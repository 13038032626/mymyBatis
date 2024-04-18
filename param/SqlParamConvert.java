package com.example.demo.param;

import lombok.Data;

import java.util.Arrays;

@Data
public class SqlParamConvert {
    private Where[] wheres;
    private String tableName;
    private String[] fields;
    private boolean limitHasOffset;
    private String limit;

    @Override
    public String toString() {
        return "SqlParamConvert{" +
                "wheres=" + Arrays.toString(wheres) +
                ", tableName='" + tableName + '\'' +
                ", fields=" + Arrays.toString(fields) +
                ", limitHasOffset=" + limitHasOffset +
                ", limit='" + limit + '\'' +
                '}';
    }
}
