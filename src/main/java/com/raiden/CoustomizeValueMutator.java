package com.raiden;

import com.alibaba.fastjson.parser.deserializer.DeserializerValueMutator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:15 2020/5/1
 * @Modified By:
 */
public class CoustomizeValueMutator implements DeserializerValueMutator {

    private Map<String, String> dataSource;

    public CoustomizeValueMutator(Map<String, String> dataSource){
        this.dataSource = dataSource;
    }
    @Override
    public Object process(Object object, Annotation[] annotations, String name, Object value) {
        if (value instanceof ArrayList){
            List list = new ArrayList();
            for (Object o : (ArrayList) value){
                list.add(DeserializerUtils.deserializer(o, dataSource));
            }
            return list;
        }
        return DeserializerUtils.deserializer(value, dataSource);
    }
}
