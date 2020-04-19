package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 23:41 2020/4/17
 * @Modified By:
 */
public class CustomizeStringCodec implements ObjectSerializer, ObjectDeserializer {

    private static Map<String, String> languageConfig;

    public CustomizeStringCodec(Map<String, String> languageConfig){
        this.languageConfig = languageConfig;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.out;

        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullStringAsEmpty);
            return;
        }

        out.writeString(value);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (clazz == StringBuffer.class) {
            final JSONLexer lexer = parser.lexer;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                String val = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                return (T) new StringBuffer(val);
            }

            Object value = parser.parse();

            if (value == null) {
                return null;
            }

            return (T) new StringBuffer(value.toString());
        }

        if (clazz == StringBuilder.class) {
            final JSONLexer lexer = parser.lexer;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                String val = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                return (T) new StringBuilder(val);
            }

            Object value = parser.parse();

            if (value == null) {
                return null;
            }

            return (T) new StringBuilder(value.toString());
        }

        return (T) deserialze(parser);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        Object value = parser.parse();
        value = DeserializerUtils.deserializer(value, languageConfig);
        if (value == null) {
            return null;
        }

        return (T) value.toString();
    }
}
