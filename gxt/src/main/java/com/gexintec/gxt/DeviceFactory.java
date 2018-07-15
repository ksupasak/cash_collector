package com.gexintec.gxt;

import android.content.Context;

import com.gexintec.gxt.devices.CashCollector;
import com.gexintec.gxt.devices.ReceiptPrinter;

/**
 * Created by soup on 1/7/2018 AD.
 */

public class DeviceFactory {

    Context context;

    private static DeviceFactory _instance = null;


    public DeviceFactory(Context context){
        this.context = context;
    }

    public static DeviceFactory getInstance(Context context){

        if(_instance==null){
            _instance = new DeviceFactory(context);
        }
        return _instance;
    }

    public GXTDevice getDeviceByName(String name){

        if(name.compareTo("cash-collector")==0){
            CashCollector  device = new CashCollector(this.context);
            device.setCallback(GXT.getInstance());
            return device;
        }
        if(name.compareTo("receipt-printer")==0){
            ReceiptPrinter device = new ReceiptPrinter(this.context);
            device.setCallback(GXT.getInstance());
            return device;
        }


        return null;
    }


}
