package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 21:52 2020/5/1
 * @Modified By: 替代fastjson中 集合对象反序列化处理类
 */
public class CustomizeArrayListTypeFieldDeserializer extends FieldDeserializer{

    private final Type itemType;
    private int itemFastMatchToken;
    private DeserializerValueMutator[] valueMutators;
    private ObjectDeserializer deserializer;

    public CustomizeArrayListTypeFieldDeserializer(Class<?> clazz, FieldInfo fieldInfo,DeserializerValueMutator[] valueMutators){
        super(clazz, fieldInfo);
        this.valueMutators = valueMutators;
        Type fieldType = fieldInfo.fieldType;
        if (fieldType instanceof ParameterizedType) {
            Type argType = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
            if (argType instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) argType;
                Type[] upperBounds = wildcardType.getUpperBounds();
                if (upperBounds.length == 1) {
                    argType = upperBounds[0];
                }
            }
            this.itemType = argType;
        } else {
            this.itemType = Object.class;
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.NULL
                || (token == JSONToken.LITERAL_STRING && lexer.stringVal().length() == 0)) {
            setValue(object, null);
            return;
        }

        ArrayList list = new ArrayList();

        ParseContext context = parser.getContext();

        parser.setContext(context, object, fieldInfo.name);
        parseArray(parser, objectType, list);
        parser.setContext(context);

        if (object == null) {
            fieldValues.put(fieldInfo.name, list);
        } else {
            setValue(object, list);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(DefaultJSONParser parser, Type objectType, Collection array) {
        Type itemType = this.itemType;
        ObjectDeserializer itemTypeDeser = this.deserializer;

        if (objectType instanceof ParameterizedType) {
            if (itemType instanceof TypeVariable) {
                TypeVariable typeVar = (TypeVariable) itemType;
                ParameterizedType paramType = (ParameterizedType) objectType;

                Class<?> objectClass = null;
                if (paramType.getRawType() instanceof Class) {
                    objectClass = (Class<?>) paramType.getRawType();
                }

                int paramIndex = -1;
                if (objectClass != null) {
                    for (int i = 0, size = objectClass.getTypeParameters().length; i < size; ++i) {
                        TypeVariable item = objectClass.getTypeParameters()[i];
                        if (item.getName().equals(typeVar.getName())) {
                            paramIndex = i;
                            break;
                        }
                    }
                }

                if (paramIndex != -1) {
                    itemType = paramType.getActualTypeArguments()[paramIndex];
                    if (!itemType.equals(this.itemType)) {
                        itemTypeDeser = parser.getConfig().getDeserializer(itemType);
                    }
                }
            } else if (itemType instanceof ParameterizedType) {
                ParameterizedType parameterizedItemType = (ParameterizedType) itemType;
                Type[] itemActualTypeArgs = parameterizedItemType.getActualTypeArguments();
                if (itemActualTypeArgs.length == 1 && itemActualTypeArgs[0] instanceof TypeVariable) {
                    TypeVariable typeVar = (TypeVariable) itemActualTypeArgs[0];
                    ParameterizedType paramType = (ParameterizedType) objectType;

                    Class<?> objectClass = null;
                    if (paramType.getRawType() instanceof Class) {
                        objectClass = (Class<?>) paramType.getRawType();
                    }

                    int paramIndex = -1;
                    if (objectClass != null) {
                        for (int i = 0, size = objectClass.getTypeParameters().length; i < size; ++i) {
                            TypeVariable item = objectClass.getTypeParameters()[i];
                            if (item.getName().equals(typeVar.getName())) {
                                paramIndex = i;
                                break;
                            }
                        }

                    }

                    if (paramIndex != -1) {
                        itemActualTypeArgs[0] = paramType.getActualTypeArguments()[paramIndex];
                        itemType = new ParameterizedTypeImpl(itemActualTypeArgs, parameterizedItemType.getOwnerType(), parameterizedItemType.getRawType());
                    }
                }
            }
        } else if (itemType instanceof TypeVariable && objectType instanceof Class) {
            Class objectClass = (Class) objectType;
            TypeVariable typeVar = (TypeVariable) itemType;
            objectClass.getTypeParameters();

            for (int i = 0, size = objectClass.getTypeParameters().length; i < size; ++i) {
                TypeVariable item = objectClass.getTypeParameters()[i];
                if (item.getName().equals(typeVar.getName())) {
                    Type[] bounds = item.getBounds();
                    if (bounds.length == 1) {
                        itemType = bounds[0];
                    }
                    break;
                }
            }
        }

        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();
        if (token == JSONToken.LBRACKET) {
            if (itemTypeDeser == null) {
                itemTypeDeser = deserializer = parser.getConfig().getDeserializer(itemType);
                itemFastMatchToken = deserializer.getFastMatchToken();
            }

            lexer.nextToken(itemFastMatchToken);

            for (int i = 0;; ++i) {
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == JSONToken.COMMA) {
                        lexer.nextToken();
                        continue;
                    }
                }

                if (lexer.token() == JSONToken.RBRACKET) {
                    break;
                }

                Object val = itemTypeDeser.deserialze(parser, itemType, i);
                array.add(val);

                parser.checkListResolve(array);

                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken(itemFastMatchToken);
                    continue;
                }
            }

            lexer.nextToken(JSONToken.COMMA);
        } else {
            if (itemTypeDeser == null) {
                itemTypeDeser = deserializer = parser.getConfig().getDeserializer(itemType);
            }
            Object val = itemTypeDeser.deserialze(parser, itemType, 0);
            array.add(val);
            parser.checkListResolve(array);
        }
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
        try {
            if (valueMutators != null && valueMutators.length > 0){
                for (DeserializerValueMutator mutator : valueMutators){
                    value = mutator.process(object,fieldInfo.field.getAnnotations(), fieldInfo.name, value);
                }
            }
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
