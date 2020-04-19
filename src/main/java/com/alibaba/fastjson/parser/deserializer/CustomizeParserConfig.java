package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 21:52 2020/4/17
 * @Modified By:
 */
public class CustomizeParserConfig extends ParserConfig {

    private Map<String, String> languageConfig;

    public CustomizeParserConfig(Map<String, String> languageConfig){
        super();
        this.languageConfig = languageConfig;
        putDeserializer(String.class, new CustomizeStringCodec(languageConfig));
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, //
                                                     JavaBeanInfo beanInfo, //
                                                     FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;

        Class<?> deserializeUsing = null;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            deserializeUsing = annotation.deserializeUsing();
            if (deserializeUsing == Void.class) {
                deserializeUsing = null;
            }
        }

        if (deserializeUsing == null && (fieldClass == List.class || fieldClass == ArrayList.class)) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new CustomizeDefaultFieldDeserializer(mapping, clazz, fieldInfo, languageConfig);
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        JsonDeserializer i18N = clazz.getAnnotation(JsonDeserializer.class);
        if (i18N != null){
            return new JavaBeanDeserializer(this, clazz);
        }
        return super.createJavaBeanDeserializer(clazz, type);
    }
}
