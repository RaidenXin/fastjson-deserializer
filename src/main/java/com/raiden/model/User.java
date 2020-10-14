package com.raiden.model;

import com.alibaba.fastjson.parser.deserializer.JsonDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 15:53 2020/3/21
 * @Modified By:
 */
@JsonDeserializer
@Getter
@Setter
public class User {

    private String name;
    private String id;
    private String student;
    private List<String> contents;
    private String url;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", student='" + student + '\'' +
                ", contents='" + contents.toString() + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
