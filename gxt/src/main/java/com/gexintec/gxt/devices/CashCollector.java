package com.gexintec.gxt.devices;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.gexintec.gxt.AbstractGXTDevice;
import com.gexintec.gxt.GXTDevice;
import com.gexintec.gxt.GXTMessage;
import com.gexintec.gxt.GXTMessageCallback;
import com.gexintec.gxt.util.HexUtils;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;


import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by soup on 1/7/2018 AD.
 */

public class CashCollector extends AbstractGXTDevice {


    int WRITE_TIMEOUT = 1000;
    int READ_TIMEOUT = 1000;

    private UsbDevice cash_collector=null;

    private static final String ACTION_USB_PERMISSION  = "com.blecentral.USB_PERMISSION";

    protected UsbDeviceConnection connection;
    protected UsbEndpoint endpoint_out;
    protected UsbEndpoint endpoint_in;

    protected Thread current_thread;

    protected UsbSerialPort serialPort;
    public CashCollector(Context context){
        super(context);
    }

    public void connect()  {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return;
        }


        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            return;
        }

// Read some data! Most have just one port (port 0).
        serialPort = driver.getPorts().get(0);

        System.out.println("OK");
        try {
            serialPort.open(connection);
            serialPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

        } catch (IOException e) {
            // Deal with error.
            System.out.println("Error "+e.getMessage());
        } finally {

        }
        String YOUR_DEVICE_NAME;


//        USBManager manager = getApplicationContext().getSystemService(Context.USB_SERVICE);
//
//        Map<String, UsbDevice> devices = manager.getDeviceList();
//        System.out.println("Number of USB Device : "+devices.size());
//
//
//        for(Map.Entry<String, UsbDevice> i :devices.entrySet()){
//
//            System.out.println("Device : "+i.getKey()+" = "+i.getValue().toString());
//            if(i.getValue().getVendorId()==4292){
//                cash_collector = i.getValue();
//            }
//        }
//
//        if(cash_collector!=null){
//            UsbDevice device = cash_collector;
//
//            System.out.println("Found cash collector : stage 1 " );
//
//            String VID = Integer.toHexString(device.getVendorId()).toUpperCase();
//            String PID = Integer.toHexString(device.getProductId()).toUpperCase();
//            if (!manager.hasPermission(device)) {
//                System.out.println("No permission : stage 1 " );
//
//                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
//                manager.requestPermission(device, mPermissionIntent);
//                return;
//            } else {
//                System.out.println("Has permission : stage 1 " );
//
//                System.out.println("Found cash collector : "+cash_collector.getManufacturerName() );
//                System.out.println("Found interface count : "+cash_collector.getInterfaceCount() );
//
//                System.out.println("Found endpoint count : "+cash_collector.getInterface(0).getEndpointCount());
//
//                connection = manager.openDevice(cash_collector);
//
//                connection.claimInterface(cash_collector.getInterface(0), true);
//                endpoint_out = cash_collector.getInterface(0).getEndpoint(0);
//                endpoint_in = cash_collector.getInterface(0).getEndpoint(1);
//
////
////                // find the endpoints
////
////                for(int j = 0; j < cash_collector.getEndpointCount(); j++)
////                {
////                    if(intf.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT && intf.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
////                    {
////                        // from android to device
////                        output = intf.getEndpoint(j);
////                    }
////
////                    if(intf.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN && intf.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
////                    {
////                        // from device to android
////                        input = intf.getEndpoint(j);
////                    }
////                }
//
//
//
//                System.out.println("Connect and active");
//
//            }
//

//        }

    }
    @Override
    public void accept(){

        byte[] data;

        data = new byte[1];
        data[0] = 2;
        System.out.println("Accept");

        try {

            byte buffer[] = new byte[1];
            buffer[0] = 2;
            serialPort.write(buffer,10);

        } catch (IOException e) {
            // Deal with error.

            System.out.println("Err read "+e.getMessage());
        } finally {

        }


//        connection.bulkTransfer(endpoint_out, data, data.length, WRITE_TIMEOUT);
        System.out.println("Sent");
    }

    @Override
    public void start(){
        current_thread = new Thread(this);
        current_thread.start();
    }

    @Override
    public void run(){

            System.out.println("start receiver");

            boolean working = true;
            while (working) {


                try {

                    byte buffer[] = new byte[16];
                    int numBytesRead = serialPort.read(buffer, 1000);
                    Log.d("Serial", "Read " + numBytesRead + " bytes : "+HexUtils.byteArrayToHexString(buffer));
                    if(numBytesRead==2){
                        this.root.callback(new GXTMessage(this, HexUtils.byteArrayToHexString(buffer,0,numBytesRead) ));

                    }
                } catch (IOException e) {
                    // Deal with error.

                   System.out.println("Err read "+e.getMessage());
                } finally {

                }

//                byte[] buffer = new byte[1024];
//
////                connection.claimInterface(cash_collector.getInterface(0), true);
////                endpoint_in = cash_collector.getInterface(0).getEndpoint(1);
//
//                int receivedBytes = connection.bulkTransfer(endpoint_in, buffer, buffer.length, READ_TIMEOUT);
//
//                System.out.println("waiting "+receivedBytes+" "+endpoint_in.getAddress());
//                if (receivedBytes > 0) {
//                    byte[] data = new byte[receivedBytes];
//                    System.arraycopy(buffer, 0, data, 0, receivedBytes);
//                    Log.v("Device","Message received: " + HexUtils.byteArrayToHexString(data));
//                    this.root.callback(new GXTMessage(this, HexUtils.byteArrayToHexString(data) ));
//
//
////                    readBuffer.write(buffer, 0, receivedBytes);
//                    // Notify interface so that XBee Reader is notified about data available.
////                    synchronized (context) {
////                        context.notify();
////                    }
//                }
            }


    }

}
