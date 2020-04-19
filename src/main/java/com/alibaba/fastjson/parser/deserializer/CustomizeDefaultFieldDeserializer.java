package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 0:13 2020/4/18
 * @Modified By:
 */
public class CustomizeDefaultFieldDeserializer extends DefaultFieldDeserializer {

    private Map<String, String> languageConfig;

    public CustomizeDefaultFieldDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo, Map<String, String> languageConfig) {
        super(config, clazz, fieldInfo);
        this.languageConfig = languageConfig;
    }

    public void setValue(Object object, Object value) {
        if (value == null //
                && fieldInfo.fieldClass.isPrimitive()) {
            return;
        } else if (fieldInfo.fieldClass == String.class
                && fieldInfo.format != null
                && fieldInfo.format.equals("trim")){
            value = ((String) value).trim();
        }
        value = DeserializerUtils.deserializer(value, languageConfig);
        try {
            Method method = fieldInfo.method;
            if (method != null) {
                if (fieldInfo.getOnly) {
                    if (fieldInfo.fieldClass == AtomicInteger.class) {
                        AtomicInteger atomic = (AtomicInteger) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicInteger) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicLong.class) {
                        AtomicLong atomic = (AtomicLong) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicLong) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicBoolean.class) {
                        AtomicBoolean atomic = (AtomicBoolean) method.invoke(object);
                        if (atomic != null) {
                            atomic.set(((AtomicBoolean) value).get());
                        }
                    } else if (Map.class.isAssignableFrom(method.getReturnType())) {
                        Map map = (Map) method.invoke(object);
                        if (map != null) {
                            if (map == Collections.emptyMap()
                                    || map.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) method.invoke(object);
                        if (collection != null && value != null) {
                            if (collection == Collections.emptySet()
                                    || collection == Collections.emptyList()
                                    || collection.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            collection.clear();
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    method.invoke(object, value);
                }
            } else {
                final Field field = fieldInfo.field;

                if (fieldInfo.getOnly) {
                    if (fieldInfo.fieldClass == AtomicInteger.class) {
                        AtomicInteger atomic = (AtomicInteger) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicInteger) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicLong.class) {
                        AtomicLong atomic = (AtomicLong) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicLong) value).get());
                        }
                    } else if (fieldInfo.fieldClass == AtomicBoolean.class) {
                        AtomicBoolean atomic = (AtomicBoolean) field.get(object);
                        if (atomic != null) {
                            atomic.set(((AtomicBoolean) value).get());
                        }
                    } else if (Map.class.isAssignableFrom(fieldInfo.fieldClass)) {
                        Map map = (Map) field.get(object);
                        if (map != null) {
                            if (map == Collections.emptyMap()
                                    || map.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }
                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) field.get(object);
                        if (collection != null && value != null) {
                            if (collection == Collections.emptySet()
                                    || collection == Collections.emptyList()
                                    || collection.getClass().getName().startsWith("java.util.Collections$Unmodifiable")) {
                                // skip
                                return;
                            }

                            collection.clear();
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    if (field != null) {
                        field.set(object, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new JSONException("set property error, " + clazz.getName() + "#" + fieldInfo.name, e);
        }
    }
}
