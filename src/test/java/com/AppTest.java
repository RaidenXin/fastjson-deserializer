package com;

import com.alibaba.fastjson.parser.deserializer.CustomizeJSON;
import com.raiden.CoustomizeValueMutator;
import com.raiden.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;


/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:39 2020/1/28
 * @Modified By:
 */
public class AppTest {

    @Test
    public void testFastJosn() {
        String userStr = "{\n" +
                "\t\"id\":\"I18nKey:20200411001\",\n" +
                "\t\"name\":\"I18nKey:张三\",\n" +
                "\t\"student\":\"I18nKey:高三三班\",\n" +
                "\t\"contents\":[\"I18nKey:1\",\"I18nKey:2\"]\n" +
                "}";
        Map<String, String> en = new HashMap<>();
        en.put("3", "zhangsan");
        en.put("4", "20200411001");
        en.put("5", "Class three in grade three");
        en.put("1", "Hello");
        en.put("2", "Welcome home");
        Map<String, String> zh = new HashMap<>();
        zh.put("3", "张三");
        zh.put("4", "20200411001");
        zh.put("5", "高三三班");
        zh.put("1", "你好");
        zh.put("2", "欢迎回家");
        CoustomizeValueMutator zhCoustomizeValueMutator = new CoustomizeValueMutator(zh);

        User user = CustomizeJSON.parseObject(userStr, User.class, zhCoustomizeValueMutator);
        System.err.println(user);
        String string = "{\n" +
                "\t\"users\": [{\n" +
                "\t\t\"id\": \"I18nKey:4\",\n" +
                "\t\t\"name\": \"I18nKey:3\",\n" +
                "\t\t\"student\": \"I18nKey:5\",\n" +
                "\t\t\"url\": \"www.baidu.com\",\n" +
                "\t\t\"contents\": [\"I18nKey:1\", \"I18nKey:2\"]\n" +
                "\t}],\n" +
                "\t\"memberId\":\"2020\"\n" +
                "}";
        CoustomizeValueMutator enCoustomizeValueMutator = new CoustomizeValueMutator(en);
        Administration administration = CustomizeJSON.parseObject(string, Administration.class, enCoustomizeValueMutator);
        System.err.println(administration);
    }
}