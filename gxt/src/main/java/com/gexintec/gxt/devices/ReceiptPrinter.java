package com.gexintec.gxt.devices;

import android.content.Context;

import com.gexintec.gxt.AbstractGXTDevice;
import com.gexintec.gxt.GXTPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by soup on 9/7/2018 AD.
 */

public class ReceiptPrinter extends AbstractGXTDevice implements GXTPrinter{

    public ReceiptPrinter(Context context){
        super(context);
    }
    Socket socket;
    String host;
    int port;

    @Override
    public void connect(){
        if(config!=null){
            String [] params = config.split(":");

            host = "localhost";
            port = 9100;

            if(params.length==2){
                host=params[0];
                port=Integer.parseInt(params[1]);
            }


            Thread t = new Thread() {
                public void run() {

                    try {


                        socket = new Socket(host, port);
                        System.out.println("connect....");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }

            };
            t.start();

        }
    }

    @Override
    public void print(String string) {

        Thread t = new Thread() {
            public void run() {

                try {
                    OutputStream out = socket.getOutputStream();
                    byte data[] = {27, 64, 29, 86, 66, 0};
                    out.write(data);
                    out.flush();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        };
        t.start();

    }

    @Override
    public void cut() {

    }
}
