package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.annotation.JSONField;

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
}
