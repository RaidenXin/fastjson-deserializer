package com.raiden.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 11:50 2020/4/18
 * @Modified By:
 */
public class LRUList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int length;
    private Map<String, Node<T>> map;
    private int maximumCapacity;


    public LRUList(int maximumCapacity){
        this.length = 0;
        this.maximumCapacity = maximumCapacity;
        this.map = new HashMap<>();
    }

    public void save(Node node){
        if (node == null){
            return;
        }
        if (this.head == null){
            this.head = node;
            this.tail = node;
            map.put(node.getKey(), node);
            length++;
        }else {
            if (getNode(node.getKey()) == null){
                //如果达到最大内存删除最后一个
                if (length == maximumCapacity){
                    remove(tail.getKey());
                }
                Node head = this.head;
                head.setPre(node);
                node.setNext(head);
                this.head = node;
                map.put(node.getKey(), node);
                length++;
            }
        }
    }

    public Node<T> remove(String key){
        if (length == 0){
            return null;
        }
        Node<T> node = map.remove(key);
        if (node != null){
            Node pre = node.getPre();
            Node next = node.getNext();
            if (node == head){
                next.setPre(pre);
                this.head = next;
            }else if (node == tail){
                pre.setNext(next);
                this.tail = pre;
            }else{
                pre.setNext(next);
                next.setPre(pre);
            }
            length--;
        }
        return node;
    }

    public Node<T> getNode(String key){
        Node<T> node = map.get(key);
        if (node == null){
            return null;
        }else {
            //如果该节点就链表头 直接返回
            if (node == this.head){
                return node;
            }
            Node pre = node.getPre();
            node.setPre(null);
            //如果是链表尾巴
            if (node == this.tail){
                //将该节点前一个变成末尾
                pre.setNext(null);
                this.tail = pre;
                //将该节点变为链表头
            }else {
                Node next = node.getNext();
                node.setNext(null);
                pre.setNext(next);
                next.setPre(pre);
            }
            Node head = this.head;
            head.setPre(node);
            node.setNext(head);
            this.head = node;
            return node;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LinkedList[");
        Node node = this.head;
        while (node != null){
            builder.append(node.toString());
            if (node.getNext() != null){
                builder.append(",");
            }
            node = node.getNext();
        }
        builder.append("]");
        return builder.toString();
    }
}
