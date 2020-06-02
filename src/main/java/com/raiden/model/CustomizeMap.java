package com.raiden.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 23:16 2020/6/2
 * @Modified By:
 */
public final class CustomizeMap<K,V> implements Map<K,V> {

    private Map<K,V> entity;

    public CustomizeMap(){
        entity = new HashMap<>();
    }

    @Override
    public int size() {
        return entity.size();
    }

    @Override
    public boolean isEmpty() {
        return entity.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return entity.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return entity.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return entity.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (key instanceof String){
            return entity.put((K) ((String) key).trim(), value);
        }else {
            return entity.put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        return entity.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        entity.putAll(m);
    }

    @Override
    public void clear() {
        entity.clear();
    }

    @Override
    public Set<K> keySet() {
        return entity.keySet();
    }

    @Override
    public Collection<V> values() {
        return entity.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entity.entrySet();
    }

    @Override
    public String toString() {
        return entity.toString();
    }
}
