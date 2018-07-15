package com.gexintec.gxt;

import android.content.Context;

import java.io.IOException;

/**
 * Created by soup on 1/7/2018 AD.
 */

public abstract class AbstractGXTDevice  implements GXTDevice, GXTNode, Runnable {

    protected Context context;
    protected String device_name;
    protected GXTMessageCallback root;
    protected String config;

    public GXTMessageCallback getCallback() {
        return root;
    }

    public void setCallback(GXTMessageCallback callback) {
        this.root = callback;
    }


    public String getDeviceName(){
        return device_name;
    }
    public void setDeviceName(String name){
        this.device_name = name;
    }

    public AbstractGXTDevice(Context context){
        this.context = context;
    }

    public void connect(String string) throws IOException{
        config = string;
        connect();
    }
    public void connect() throws IOException {

    }
    public void hi(){
        System.out.println("Hi");
    }
    public void setContext(Context context){
        this.context = context;
    }
    public void start(){

    }
    public void run(){

    }
    public void accept(){

    }
    public String getName(){
        return this.getDeviceName();
    }

}
