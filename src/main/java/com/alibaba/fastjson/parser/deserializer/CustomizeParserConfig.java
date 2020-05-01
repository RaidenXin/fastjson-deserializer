package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 21:52 2020/4/17
 * @Modified By:
 */
public class CustomizeParserConfig extends ParserConfig {

    private DeserializerValueMutator[] valueMutators;

    public CustomizeParserConfig(DeserializerValueMutator[] valueMutators){
        super();
        this.valueMutators = valueMutators;
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
            return new CustomizeArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo, valueMutators);
        }
        return new CustomizeDefaultFieldDeserializer(mapping, clazz, fieldInfo, valueMutators);
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        JsonDeserializer i18N = clazz.getAnnotation(JsonDeserializer.class);
        if (i18N != null){
            return new JavaBeanDeserializer(this, clazz);
        }
        return super.createJavaBeanDeserializer(clazz, type);
    }
}
