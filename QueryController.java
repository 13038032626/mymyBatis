package com.example.demo;

import com.alibaba.fastjson2.JSONArray;
import com.example.demo.param.SqlParamConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class QueryController {

    @Autowired
    SelectEngine engine;
    @PostMapping("/query")
    public List query(@RequestBody(required = false) SqlParamConvert param, @RequestParam String aClass) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return engine.query(param,aClass);
    }
    /**
     * 参数规范
     * 1. tableName:String 数据库表名
     * 2. wheres:where[]
     *    where:
     *      1. whereType:枚举类 SIMPLE_EQUALS RANGE_EQUALS
     *      2. RangeType:枚举类 IN, BETWEEN, LESS_THAN, MORE_THAN, LESS_THAN_EQUALS, MORE_THAN_EQUALS
     *      3. name: 字段名
     *      4. value:字段值 -> RangeType类型不同，传入value格式不同
     *      5. type: MySQLDataType枚举类 TINYINT...
     * 3. fields:String[] 查询的目标字段
     * 4. limitHasOffset:boolean 下一个字段格式是A,B还是A
     * 5. limit:String
     */
}
