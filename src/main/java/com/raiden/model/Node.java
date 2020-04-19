package com.raiden.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 11:48 2020/4/18
 * @Modified By:
 */
@Setter
@Getter
public class Node<T> {

    private Node pre;
    private Node next;
    private String key;
    private T data;

    public Node(String key,T data){
        this.key = key;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key='" + key + '\'' +
                ", data=" + data +
                '}';
    }
}
