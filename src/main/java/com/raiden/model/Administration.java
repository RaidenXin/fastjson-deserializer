package com.raiden.model;

import com.alibaba.fastjson.parser.deserializer.JsonDeserializer;
import com.raiden.CoustomizeValueMutator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 23:56 2020/4/17
 * @Modified By:
 */
@JsonDeserializer
@Getter
@Setter
public class Administration {

    private List<User> users;
    private String memberId;

    @Override
    public String toString() {
        return "Administration{" +
                "users=" + users +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
