package joslabs.kbssmsapp.bluetooth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import  android.bluetooth.BluetoothDevice;
import  android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import joslabs.kbssmsapp.R;

public class BTPrinter extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;

    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    TextView lblPrinterName;
    String sender,content,stime,smsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btprinter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras=getIntent().getExtras();
        sender=extras.getString("sender");
        content=extras.getString("content");
        stime=extras.getString("time");
        smsText=sender+" "+content+""+stime;

        Button btnConnect = (Button) findViewById(R.id.btnAdd);
        Button btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        Button btnPrint = (Button) findViewById(R.id.btnPrint);
        Button btnadmin=findViewById(R.id.btnadmin);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sa="Total";
                String sb="21000";
                String ans=sa+sb;
                int n=31-ans.length();
                Log.e("strSize",ans.length()+"\tdiff:\t "+(31-ans.length())+"\t length:\t");
                Log.e("diffa:\t",sa + new String(new char[n]).replace("\0", " ") + sb);
                Log.e("diffb:\t",sa + new String(new char[16]).replace("\0", " ") + sb);
                Log.e("diffb:\t",sa + new String(new char[10]).replace("\0", " ") + sb);
                Log.e("diffc:\t","TOTAL                     22000 ");

            }
        });




        lblPrinterName = (TextView) findViewById(R.id.txttitle);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    FindBluetoothDevice();
                    openBluetoothPrinter();
                    printData();//print directly

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
       /* btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    disconnectBT();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    printData();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });*/
    }
    void FindBluetoothDevice(){

        try{

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                lblPrinterName.setText("No Bluetooth Adapter found");
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }

            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();

            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){

                    // My Bluetoth printer name is BTP_F09F1A
                    if(pairedDev.getName().equals("BlueTooth Printer")){
                        bluetoothDevice=pairedDev;
                        lblPrinterName.setText("Bluetooth Printer Attached: "+pairedDev.getName());
                        break;
                    }
                }
            }

            lblPrinterName.setText("Bluetooth Printer Attached");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    // Open Bluetooth Printer

    void openBluetoothPrinter() throws IOException{
        try{

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){

        }
    }

    void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Printing Text to Bluetooth Printer //
    void printData() throws  IOException{
        try{
            //String msg = textBox.getText().toString();
           /* String msg=smsText;
            msg+="\n";
            outputStream.write(msg.getBytes());
            lblPrinterName.setText("Printing Text...");*/
            //from here
            String companyNameStr	=
                    "\nWaridi Cafe & Caterers" + "\n"
                            +"P.O. BOX 2888-00200"+ "\n"
                            +"NAIROBI, KENYA"+ "\n"
                            +"+254 20-20000"+ "\n";

            String titleStr	="PURCHASE ORDER"+ "\n\n";

            StringBuilder contentSb	= new StringBuilder();

           // String date = DateUtil.timeMilisToString(System.currentTimeMillis(), "dd-MM-yy / HH:mm");
/*
            contentSb.append("TRANSACTION #: ").append("").append("\n");
            contentSb.append("DATE         : ").append(date).append("\n");
            contentSb.append("SALES REP    : " +"JOHN DOE" + "\n\n");


            byte[] companyNameByte	= Printers.printfont(companyNameStr, FontDefine.FONT_32PX,FontDefine.Align_CENTER,(byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

            byte[] titleByte	= Printers.printfont(titleStr, FontDefine.FONT_32PX_UNDERLINE,FontDefine.Align_CENTER,(byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

            byte[] content2Byte	= Printers.printfont(contentSb.toString(), FontDefine.FONT_24PX,FontDefine.Align_LEFT,(byte)0x1A, PocketPos.LANGUAGE_ENGLISH);

            //initialize array size
            byte[] totalByte	= new byte[
                    companyNameByte.length
                            + titleByte.length
                            +content2Byte.length];

        /*
        System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        src − This is the source array.
        srcPos − This is the starting position in the source array.
        dest − This is the destination array.
        destPos − This is the starting position in the destination data.
        length − This is the number of array elements to be copied.
        */

           /* int offset = 0;
            System.arraycopy(companyNameByte, 0, totalByte, offset, companyNameByte.length);
            offset += companyNameByte.length;

            System.arraycopy(titleByte, 0, totalByte, offset, titleByte.length);
            offset += titleByte.length;

            System.arraycopy(content2Byte, 0, totalByte, offset, content2Byte.length);
            */
            //to here
            OutputStream opstream = null;
            try {
                opstream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = bluetoothSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
               outputStream.write(printformat);//TODO uncheck this to add customized printing format


               // printCustom(companyNameStr,1,1);
                //printPhoto(R.drawable.receiptsb);
                printPhoto(R.drawable.barcode);
                printCustom("Number:  888888\nReceipt  S00003333\nCashier：1001\nDate：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n",0,0);
                printCustom("ITEM    QTY      PRICE  AMOUNT\nShoes   10.00       899     8990\nBall    10.00       1599    15990\n",4,3);
                printCustom("- - - - - - - - - - - - - - - - - - - \n",0,3);
                printCustom("TOTAL",2,0);
                printCustom("21000\n",2,2);
                printCustom("\n======================================\n",0,3);
                //printTotals("TOTAL","21000",1,0,1);
                printText(leftRightAlign("\nTOTAL" , "2,100\n"));
               // printText(leftRightAlign("CASH" , "3,000/="));
                printCustom("TOTAL                     22000 ",1,0);
                printCustom("\n- - - - - - - - - - - - - - - - - - - - \n",0,3);
               // printCustom("Quantity：             20.00\ntotal：                16889.00\npayment：              17000.00\nKeep the change：      111.00\n",0,3);

               /* printCustom("Pepperoni Foods Ltd.",0,1);
                printPhoto(R.drawable.ic_printera);
                printCustom("H-123, R-123, Dhanmondi, Dhaka-1212",0,1);
                printCustom("Hot Line: +88000 000000",0,1);
                printCustom("Vat Reg : 0000000000,Mushak : 11",0,1);
                String dateTime[] = getDateTime();
                printText(leftRightAlign(dateTime[0], dateTime[1]));
                printText(leftRightAlign("Qty: Name" , "Price "));
                printCustom(new String(new char[32]).replace("\0", "."),0,1);
                printText(leftRightAlign("Total" , "2,0000/="));*/
                printNewLine();
               // printPhoto(R.drawable.qrcode);
               // printCustom("Thank you for coming & we look",0,1);
                printCustom("forward to serve you again",0,1);
                printNewLine();
                printNewLine();

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



    // Disconnect Printer //
    void disconnectBT() throws IOException{
        try {
            stopWorker=true;
            outputStream.close();
            inputStream.close();
            bluetoothSocket.close();
            lblPrinterName.setText("Printer Disconnected.");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;

            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
                case 3:

                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printTotals(String msg,String msgb, int size, int align,int alignb){
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(msg.getBytes("GBK"));
            outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            outputStream.write(msgb.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
            Log.e("stringsize",ans.length()+"\tnsize\t"+n);
        }
        Log.e("stringsize",ans.length()+"\tnsize\t");
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }
}

