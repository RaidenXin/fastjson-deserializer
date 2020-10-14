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
 * @Modified By: 自定义fastjson 解析配置类 这个类是核心
 */
public class CustomizeParserConfig extends ParserConfig {

    /**
     * 值修改器数组 可以将类修改器放入其中使用
     */
    private DeserializerValueMutator[] valueMutators;

    public CustomizeParserConfig(){
    }

    /**
     * 有参构造方法 可以通过该方法 将类修改器放入其中
     * @param valueMutators
     */
    public CustomizeParserConfig(DeserializerValueMutator... valueMutators){
        super();
        this.valueMutators = valueMutators;
    }

    /**
     * 创建一个属性反序列化处理类
     * @param mapping
     * @param beanInfo
     * @param fieldInfo
     * @return
     */
    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, //
                                                     JavaBeanInfo beanInfo, //
                                                     FieldInfo fieldInfo) {
        //获取要反序列化的model 的class
        Class<?> clazz = beanInfo.clazz;
        //获取要反序列化属性的 class
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
            //将源码中的 ArrayListTypeFieldDeserializer 替换成 我们自定义的 CustomizeArrayListTypeFieldDeserializer 处理器
            return new CustomizeArrayListTypeFieldDeserializer(clazz, fieldInfo, valueMutators);
        }
        //将源码中的 DefaultFieldDeserializer 替换成 我们自定义的 CustomizeDefaultFieldDeserializer 处理器
        return new CustomizeDefaultFieldDeserializer(mapping, clazz, fieldInfo, valueMutators);
    }

    /**
     * 创建一个 javaBean 反序列化处理器
     * @param clazz
     * @param type
     * @return
     */
    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        //获取要反序列化类上的标识注解
        JsonDeserializer jsonDeserializer = clazz.getAnnotation(JsonDeserializer.class);
        //如果不存在就走原逻辑
        if (jsonDeserializer != null){
            //获取注解中的反序列化值处理器
            Class<? extends DeserializerValueMutator>[] classes = jsonDeserializer.valueMutators();
            if (classes != null && classes.length > 0){
                DeserializerValueMutator[] mutators = new DeserializerValueMutator[classes.length];
                int size = 0;
                for (Class<? extends DeserializerValueMutator> c : classes) {
                    try {
                        DeserializerValueMutator mutator = c.newInstance();
                        mutators[size] = mutator;
                        size++;
                    } catch (Exception e) {
                        //如果创建失败了就忽略掉这次错误
                    }
                }
                if (size > 0){
                    //判断原来是否有值 如果有 就合并成一组
                    if (valueMutators != null){
                        DeserializerValueMutator[] newValueMutators = new DeserializerValueMutator[size + valueMutators.length];
                        System.arraycopy(valueMutators, 0, newValueMutators, 0, valueMutators.length);
                        System.arraycopy(mutators, 0, newValueMutators, valueMutators.length, size);
                        this.valueMutators = newValueMutators;
                    }else {
                        this.valueMutators = new DeserializerValueMutator[size];
                        System.arraycopy(mutators, 0, valueMutators, 0, size);
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
