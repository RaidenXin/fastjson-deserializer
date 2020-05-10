package com.alibaba.fastjson.parser.deserializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:25 2020/4/17
 * @Modified By:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface JsonDeserializer {
    /**
     * 注意这里只能放无参构造函数的修改器 否则会创建失败
     * @return
     */
    Class<? extends DeserializerValueMutator>[] valueMutators() default {};
}
