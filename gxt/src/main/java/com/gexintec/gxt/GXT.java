package com.gexintec.gxt;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by soup on 1/7/2018 AD.
 */

public class GXT implements GXTMessageCallback{


    ArrayList<GXTMessageCallback> callbacks;

    public static GXT _instance;

    private Context context;

    public GXT(Context context){
        this.context = context;
        callbacks = new ArrayList<GXTMessageCallback>();
    }

    public static void hi(){
        System.out.println("Hello");
    }

    public static GXT getInstance(){
        return _instance;
    }

    public static GXT getInsance(Context context){
        if(_instance==null){
            _instance = new GXT(context);
        }
        return _instance;
    }

    public DeviceFactory getDeviceFactory(){
        return DeviceFactory.getInstance(this.context);
    }

    public void callback(GXTMessage msg){
        Iterator<GXTMessageCallback> it = callbacks.iterator();
        while(it.hasNext()){
            GXTMessageCallback c = it.next();
            c.callback(msg);
        }
    }

    public void addGXTMessageCallback(GXTMessageCallback callback){
        callbacks.add(callback);
    }

}
