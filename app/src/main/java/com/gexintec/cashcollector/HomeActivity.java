package com.gexintec.cashcollector;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.gexintec.gxt.*;

import java.io.IOException;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomeActivity extends AppCompatActivity implements GXTMessageCallback{
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };




    GXT core ;
    DeviceFactory factory ;

    GXTDevice cash_collector_device;
    GXTPrinter printer_device;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        // Custom

        Button connect_btn = (Button)findViewById(R.id.connect_btn);

        core = GXT.getInsance(this);
        core.addGXTMessageCallback(this);
        factory = core.getDeviceFactory();




        connect_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                System.out.println("Hello world");

//                GXT.hi();
//
                cash_collector_device.accept();
//                cash_collector_device.start();
//                printer_device.print("Hello world");

//                connect();
            }

        });

    }

    private UsbDevice cash_collector=null;
    private static final String ACTION_USB_PERMISSION  = "com.blecentral.USB_PERMISSION";

    private void connect(){


        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        String YOUR_DEVICE_NAME;
        byte[] data;
        int TIMEOUT=1000;

//        USBManager manager = getApplicationContext().getSystemService(Context.USB_SERVICE);

        Map<String, UsbDevice> devices = manager.getDeviceList();
        System.out.println("Number of USB Device : "+devices.size());

        data = new byte[1];
        data[0] = 2;

        for(Map.Entry<String, UsbDevice> i :devices.entrySet()){

            System.out.println("Device : "+i.getKey()+" = "+i.getValue().toString());
            if(i.getValue().getVendorId()==4292){
                cash_collector = i.getValue();
            }
        }

        if(cash_collector!=null){
            UsbDevice device = cash_collector;

            String VID = Integer.toHexString(device.getVendorId()).toUpperCase();
            String PID = Integer.toHexString(device.getProductId()).toUpperCase();
            if (!manager.hasPermission(device)) {
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                manager.requestPermission(device, mPermissionIntent);
                return;
            } else {
                System.out.println("Found cash collector : "+cash_collector.getManufacturerName() );
                UsbDeviceConnection connection = manager.openDevice(cash_collector);
                UsbEndpoint endpoint = cash_collector.getInterface(0).getEndpoint(0);

                connection.claimInterface(cash_collector.getInterface(0), true);
                connection.bulkTransfer(endpoint, data, data.length, TIMEOUT);
                System.out.println("Connect and active");
            }


        }
//        byte[] data;
//
//        data = new byte[1];
//        data[0] = 2;
//
//        UsbDevice mDevice = devices.get(YOUR_DEVICE_NAME);
//        UsbDeviceConnection connection = manager.openDevice(mDevice);
//        UsbEndpoint endpoint = mDevice.getInterface(0).getEndpoint(0);
//
//        connection.claimInterface(mDevice.getInterface(0), true);
//        connection.bulkTransfer(endpoint, data, data.length, TIMEOUT);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);

        cash_collector_device = factory.getDeviceByName("cash-collector");

        cash_collector_device.hi();

        try {
            cash_collector_device.connect();

            cash_collector_device.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


        GXTDevice printer = factory.getDeviceByName("receipt-printer");
//        printer.connect("192.168.123.100:9100");
//        printer_device = (GXTPrinter)printer;

    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to r`    emove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void callback(GXTMessage msg) {

        System.out.println("Device : "+msg.getNode().getName()+" Msg : "+msg.getMsg());
        if(msg.getMsg().compareTo("8142")==0){
            System.out.println("Accepted..!!");
            cash_collector_device.accept();
        }else{
            System.out.println("Accept only 100BAHT");
        }


    }
}
