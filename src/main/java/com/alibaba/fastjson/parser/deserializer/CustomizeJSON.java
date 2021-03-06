package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.lang.reflect.Type;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:12 2020/5/1
 * @Modified By: 反序列化工具类
 */
public final class CustomizeJSON {

    public static <T> T parseObject(String input,Type clazz,DeserializerValueMutator... valueMutators) {
        return JSON.parseObject(input, clazz, new CustomizeParserConfig(valueMutators));
    }

    public static <T> T parseObject(String json, Type type,DeserializerValueMutator[] valueMutators, Feature... features) {
        return JSON.parseObject(json, type, new CustomizeParserConfig(valueMutators), features);
    }

    public static <T> T parseObject(String json, Type type,Feature... features) {
        return JSON.parseObject(json, type, new CustomizeParserConfig(), features);
    }

    public static <T> T parseObject(String json, Type type) {
        return JSON.parseObject(json, type, new CustomizeParserConfig());
    }
}
