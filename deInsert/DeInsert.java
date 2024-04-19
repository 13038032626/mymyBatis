package com.example.demo.deInsert;

import com.example.demo.param.MySQLDataType;
import com.example.demo.param.RangeType;
import com.example.demo.param.Where;
import com.example.demo.param.WhereType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeInsert {

    //从where即可进行校验，而不是在genSQL解析出类型和value后才校验 --> 解耦！
    private static final Set<String> BLACKLIST = new HashSet<>(Arrays.asList(
            "#",
            "--",
            "union",
            "'",
            "information_schema.schemata",
            "information_schema.tables",
            "information_schema.columns",
            "schema_name",
            "table_name",
            "column_name",
            "table_schema"));

    public static boolean isInsert(Where where) {
        String value = where.getValue();

        // 检查是否存在敏感字符
        for (String black : BLACKLIST) {
            if (value.contains(black)) {
                return false; // 存在敏感字符，校验失败
            }
        }
        //没必要根据具体类型再进行复杂的校验，后期解析时又要进行一遍，在后期catch解析异常即可
        return true;
    }
}
