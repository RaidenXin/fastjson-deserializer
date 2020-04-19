package com.raiden.model;

import java.util.concurrent.ThreadFactory;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 23:28 2020/4/14
 * @Modified By:
 */
public class NameThreadFactory implements ThreadFactory {

    private String name;

    public NameThreadFactory(String name){
        this.name = name;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name);
        return thread;
    }
}
