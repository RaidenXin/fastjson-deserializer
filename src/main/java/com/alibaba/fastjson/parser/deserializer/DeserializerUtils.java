package com.alibaba.fastjson.parser.deserializer;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 0:15 2020/4/18
 * @Modified By:
 */
public final class DeserializerUtils {

    public static final String deserializer(Object value, Map<String, String> languageConfig){
        String result = null;
        if (value.getClass() == String.class && StringUtils.contains((result = ((String) value).trim()), CustomizeStaticConfig.I18N_KEY)){
            int indexOf = result.indexOf(CustomizeStaticConfig.I18N_KEY) + CustomizeStaticConfig.I18N_KEY.length();
            String key = StringUtils.substring(result, indexOf);
            result = languageConfig.getOrDefault(key, result);
        }
        return result;
    }
}
