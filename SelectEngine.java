package com.example.demo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.param.SqlParamConvert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

@Component
public class SelectEngine {
    public <T> List<T> query(SqlParamConvert sqlParam, String tClass) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("sqlParam = " + sqlParam);
        String sql = GenSql.produce(sqlParam);
        ResultSet rs = prepareResultSet(sql);


        List<T> ans = new ArrayList<>();
        Class<?> c = Class.forName("com.example.demo.test.User");
        Field[] fields = c.getDeclaredFields();
        String[] targetFields = sqlParam.getFields();
        Set<String> set = new HashSet<>();
        Collections.addAll(set, targetFields);


        System.out.println("fields.length = " + fields.length);
        while (rs.next()) {
            T t = (T) c.getDeclaredConstructor().newInstance();
            for (Field f : fields) {
                String[] split = f.toString().split("\\.");
                String target = split[split.length - 1];
                if (!set.contains(target)) {
                    continue;
                }
                String value = rs.getString(target);
                System.out.println("f.toString() = " + f);
                f.setAccessible(true);
                f.set(t, convertFieldFromString(f, value));
            }
            ans.add(t);
        }

        return ans;
    }

    public ResultSet prepareResultSet(String sql) throws ClassNotFoundException, SQLException {
        System.out.println("sql = " + sql);
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection com = DriverManager.getConnection("jdbc:mysql://192.168.56.10/demo", "root", "root");
        Statement stat = com.createStatement();
        ResultSet rs = stat.executeQuery(sql);

//        stat.close();
//        com.close();
        return rs;
    }

    public Object convertFieldFromString(Field f, String value) {
        Class<?> type = f.getType();
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.isEnum()) {
            return value;
        } else {
            return value;
        }
    }
}
