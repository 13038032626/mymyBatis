package com.example.demo;

import com.example.demo.param.SqlParamConvert;
import com.example.demo.param.Where;
import com.example.demo.param.WhereType;

import java.util.HashSet;
import java.util.Set;

public class GenSql {

    public static final Set<String> NUMBER_TYPES = new HashSet<>();

    static {
        NUMBER_TYPES.add("tinyint");
        NUMBER_TYPES.add("smallint");
        NUMBER_TYPES.add("int");
        NUMBER_TYPES.add("bigint");
        NUMBER_TYPES.add("double");
        NUMBER_TYPES.add("float");
        NUMBER_TYPES.add("decimal");

    }

    public static String produce(SqlParamConvert params) {
        /*
        项目经理（海华）：
        - 传入参数：
            1. 目的属性
            2. dataSource 数据库名
            3. 表名
            4. wheres
            5. limit
            6. order
         */
        String tableName = params.getTableName();
        Where[] wheres = params.getWheres();
        boolean limitHasOffset = params.isLimitHasOffset();
        String limit = params.getLimit();
        String[] fields = params.getFields();

        StringBuilder fieldsInSql = new StringBuilder();
//        for (String str:fields) {
//            fieldsInSql.append(str).append(",");
//        }
        for (int i = 0; i < fields.length; i++) {
            fieldsInSql.append(fields[i]);
            if (i != fields.length - 1) {
                fieldsInSql.append(",");
            }
        }

        StringBuilder wheresInSql = new StringBuilder();
        for (int i = 0; i < wheres.length; i++) {
            Where where = wheres[i];
            String name = where.getName();
            WhereType whereType = where.getWhereType();
            if (WhereType.SIMPLE_EQUALS.equals(whereType)) {
                //是简单的 a等于a‘
                boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));

                wheresInSql.append(
                        intOrChar ? String.format(" %s=%s", name, where.getValue()) :
                                String.format(" %s='%s'", name, where.getValue())
                );
            } else if (WhereType.RANGE_EQUALS.equals(whereType)) {
                //是范围类型 a in range / a < b / a > c
                switch (where.getRangeType()) {
                    case IN -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String[] ins = where.getValue().split(",");
                        String tempStr = intOrChar ?
                                String.format("%s in (%s)", where.getName(), String.join(",", ins)) :
                                String.format("%s in ('%s')", where.getName(), String.join(",", ins));
                        wheresInSql.append(tempStr);
                    }
                    case BETWEEN -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String[] betweens = where.getValue().split(",");
                        String tempStr = intOrChar ?
                                String.format("%s between %s and %s", where.getName(), betweens[0], betweens[1]) :
                                String.format("%s between '%s' and '%s'", where.getName(), betweens[0], betweens[1]);
                        wheresInSql.append(tempStr);
                    }
                    case LESS_THAN -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String value = where.getValue();
                        String tempStr = intOrChar ?
                                String.format("%s < %s", where.getName(), value) :
                                String.format("%s < '%s'", where.getName(), value);
                        wheresInSql.append(tempStr);
                    }
                    case MORE_THAN -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String value = where.getValue();
                        String tempStr = intOrChar ?
                                String.format("%s > %s", where.getName(), value) :
                                String.format("%s > '%s'", where.getName(), value);
                        wheresInSql.append(tempStr);
                    }
                    case LESS_THAN_EQUALS -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String value = where.getValue();
                        String tempStr = intOrChar ?
                                String.format("%s <= %s", where.getName(), value) :
                                String.format("%s <= '%s'", where.getName(), value);
                        wheresInSql.append(tempStr);
                    }
                    case MORE_THAN_EQUALS -> {
                        boolean intOrChar = NUMBER_TYPES.stream().anyMatch(t -> where.getType().name().startsWith(t));
                        String value = where.getValue();
                        String tempStr = intOrChar ?
                                String.format("%s >= %s", where.getName(), value) :
                                String.format("%s >= '%s'", where.getName(), value);
                        wheresInSql.append(tempStr);
                    }
                }
            }
            if (i != wheres.length - 1) {
                wheresInSql.append(" and ");
            }
        }
        String limitStr = "";
        if (limitHasOffset) {
            String[] limitCondition = limit.split(",");
            //原本无论有无offset，直接拼在limit后即可，但是为了检测无注入行为，选择细分场景
            limitStr = String.format("%s,%s", limitCondition[0], limitCondition[1]);
        } else {
            limitStr = String.format("%s", limit);
        }

        StringBuilder sql = new StringBuilder();
        return sql.append("select ")
                .append(fieldsInSql)
                .append(" from ")
                .append(tableName)
                .append("".equals(wheresInSql.toString()) ? "" : " where")
                .append(wheresInSql)
                .append("".equals(limitStr) ? "" : " limit ")
                .append(limitStr)
                .toString();
    }
}
