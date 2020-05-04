package com.raiden;

import com.alibaba.fastjson.parser.deserializer.CustomizeStaticConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 0:15 2020/4/18
 * @Modified By:
 */
public final class DeserializerUtils {

    public static final Object deserializer(Object value, Map<String, String> languageConfig){
        String result = null;
        if (value.getClass() == String.class && StringUtils.contains((result = ((String) value).trim()), CustomizeStaticConfig.I18N_KEY)){
            int indexOf = result.indexOf(CustomizeStaticConfig.I18N_KEY) + CustomizeStaticConfig.I18N_KEY.length();
            String key = StringUtils.substring(result, indexOf);
            return languageConfig.getOrDefault(key, result);
        }
        return value;
    }
}
