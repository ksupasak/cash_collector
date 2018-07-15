package com.gexintec.gxt;

import android.content.Context;

import java.io.IOException;

/**
 * Created by soup on 1/7/2018 AD.
 */

public interface GXTDevice {


    public void connect() throws IOException;
    public void connect(String config) throws IOException;
    public void start();
    public void accept();
    public void hi();
    public void setContext(Context context);
    public String getDeviceName();
    public void setDeviceName(String name);

}
