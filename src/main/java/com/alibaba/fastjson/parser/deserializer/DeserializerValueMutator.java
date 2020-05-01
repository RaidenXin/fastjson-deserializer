package com.alibaba.fastjson.parser.deserializer;

import java.lang.annotation.Annotation;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:01 2020/5/1
 * @Modified By:
 */
public interface DeserializerValueMutator {

    Object process(Object object, Annotation[] annotations, String name, Object value);
}
