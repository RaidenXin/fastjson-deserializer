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

    public CustomizeParserConfig(){
    }

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
            Class<? extends DeserializerValueMutator>[] classes = i18N.valueMutators();
            if (classes != null && classes.length > 0){
                List<DeserializerValueMutator> mutators = new ArrayList<>(classes.length);
                for (Class<? extends DeserializerValueMutator> c : classes) {
                    try {
                        DeserializerValueMutator mutator = c.newInstance();
                        mutators.add(mutator);
                    } catch (Exception e) {
                        //如果创建失败了就忽略掉这次错误
                    }
                }
                int size = mutators.size();
                if (size > 0){
                    //判断原来是否有值 如果有 就合并成一组
                    if (valueMutators != null){
                        DeserializerValueMutator[] newValueMutators = new DeserializerValueMutator[size + valueMutators.length];
                        System.arraycopy(valueMutators, 0, newValueMutators, 0, valueMutators.length);
                        for (int i = 0; i < size; i++) {
                            newValueMutators[valueMutators.length + i] = mutators.get(i);
                        }
                        this.valueMutators = newValueMutators;
                    }else {
                        this.valueMutators = mutators.toArray(new DeserializerValueMutator[]{});
                    }
                }
            }
            if (valueMutators != null){
                return new JavaBeanDeserializer(this, clazz);
            }
        }
        return super.createJavaBeanDeserializer(clazz, type);
    }
}
