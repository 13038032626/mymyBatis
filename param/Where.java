package com.example.demo.param;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Where {

    WhereType whereType;
    RangeType rangeType;
    String name;
    String value;
    MySQLDataType type;

    /*
    //TODO： 我的做法是预编译吗？
    //很明显不是 —— 预编译指DBMS知道SQL的轮廓，即使有注入信息，轮廓也不被破坏。
    校验的核心：字符串类型的校验
    前端传入的，主要是char/varchar/enum/date/datetime/year 用于where筛选
    不会有text...
    - 思路：按照类型确定黑白名单
        - 黑名单：一旦包括常见SQL注入的特征字符，拒接执行
            #
            --
            '
        - 白名单：只有包括对应类型的某个特征字符 / 属于对应类型，允许执行
            不同类型不同白名单？
     */
    private static final Set<String> BLACKLIST = new HashSet<>();

    static {
        // 添加黑名单特征字符
        BLACKLIST.add("#");
        BLACKLIST.add("--");
        BLACKLIST.add("'");
    }

    public boolean checkSQLInsertion() {
        StringBuilder sb = new StringBuilder();

        for (String blackWord : BLACKLIST) {
            if (value.contains(blackWord)) {
                return false;
            }
        }

        // 根据 type 指定的数据类型，确定 value 是否符合对应的类型
        switch (type) {
            case TINYINT:
                // 如果 type 指定为 tinyint 类型，检查 value 是否为合法的整数，并且范围在 [-128, 127] 之间
                try {
                    int intValue = Integer.parseInt(value);
                    // value 是合法的整数，并且在 [-128, 127] 范围内，校验通过，返回 true
                    return intValue >= -128 && intValue <= 127;
                } catch (NumberFormatException e) {
                    // value 不是合法的整数，校验不通过，返回 false
                    return false;
                }
            case SMALLINT:
            case INT:
            case BIGINT:
                // 如果 type 指定为整数类型，检查 value 是否为合法的整数
                try {
                    Long.parseLong(value);
                    // value 是合法的整数，校验通过，返回 true
                    return true;
                } catch (NumberFormatException e) {
                    // value 不是合法的整数，校验不通过，返回 false
                    return false;
                }
            case DOUBLE:
            case FLOAT:
            case DECIMAL:
                // 如果 type 指定为浮点数类型，检查 value 是否为合法的浮点数
                try {
                    Double.parseDouble(value);
                    // value 是合法的浮点数，校验通过，返回 true
                    return true;
                } catch (NumberFormatException e) {
                    // value 不是合法的浮点数，校验不通过，返回 false
                    return false;
                }
            case DATE:
                if (value.length() > 10) {
                    return false;
                }
                if (!value.contains("-") ||
                        !value.contains("/") ||
                        !value.contains(".")) {
                    return false;
                }
                //业务判断 ，好像不应该在这里，应该在前端，或者service...
                //如果年份不合理，月份不存在...
            case TIME:
                if (value.length() > 10) {
                    return false;
                }
                return value.contains(":");
            case DATETIME:
            case YEAR:
            case CHAR,VARCHAR:
                //按照字符串类型，也理应能够排序 / 筛选 / between...
                //TODO：暂时没用更强的过滤方案
        }
        return false;
    }

}
