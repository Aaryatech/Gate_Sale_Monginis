package com.ats.gate_sale_monginis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.util.NewPrintHelper;
import com.ats.gate_sale_monginis.util.PrintReceiptType;
import com.ats.gate_sale_monginis.util.Prints;
import com.google.gson.Gson;
import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectIPActivity extends Activity implements OnClickListener, IOCallBack {

    //private static final String TAG = "ConnectIPActivity";

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    NETPrinting mNet = new NETPrinting();

    EditText inputIp, inputPort,edIp;
    Button btnConnect, btnDisconnect, btnPrint,btnSave,btnTestPrint;

    ConnectIPActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectip);

        mActivity = this;

        btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        btnPrint = (Button) findViewById(R.id.buttonPrint);
        inputIp = (EditText) findViewById(R.id.editTextInputIp);
        inputPort = (EditText) findViewById(R.id.editTextInputPort);
        edIp = (EditText) findViewById(R.id.edIp);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnTestPrint = (Button) findViewById(R.id.btnPrint);

        btnConnect.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTestPrint.setOnClickListener(this);

        inputIp.setText("192.168.1.19");
        inputPort.setText("9100");
        btnConnect.setEnabled(true);
        btnDisconnect.setEnabled(false);
        //btnPrint.setEnabled(false);

        mPos.Set(mNet);
        mNet.SetCallBack(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
        String printIp = pref.getString(Constants.PRINTER_IP, "");
        if (printIp != null) {
            edIp.setText(printIp);
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // btnDisconnect.performClick();
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            case R.id.buttonConnect:
                boolean valid = false;
                int port = 9100;
                String ip = "";
                try {
                    ip = inputIp.getText().toString();
                    if (null == IsIPValid(ip))
                        throw new Exception("Invalid IP Address");
                    port = Integer.parseInt(inputPort.getText().toString());
                    valid = true;
                    Log.e("IP ADDRESS : ", "------------" + ip);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid Port Number", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid IP Address", Toast.LENGTH_LONG)
                            .show();
                    valid = false;
                }
                if (valid) {
                    // 进行下一步连接操作。
                    Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
                    btnConnect.setEnabled(false);
                    btnDisconnect.setEnabled(false);
                    btnPrint.setEnabled(false);
                    es.submit(new TaskOpen(mNet, ip, port, mActivity));
                }
                break;

            case R.id.buttonDisconnect:
                es.submit(new TaskClose(mNet));
                break;

            case R.id.buttonPrint:
                btnPrint.setEnabled(false);
                es.submit(new TaskPrint(mPos));
                break;


            case  R.id.btnSave:
                String printId = edIp.getText().toString();

                if (printId.isEmpty()) {
                    Toast.makeText(ConnectIPActivity.this, "Please enter Printer IP Address", Toast.LENGTH_SHORT).show();
                    edIp.setError("required");
                } else {
                    edIp.setError(null);

                    String printerIPTCP = "TCP:" + printId;

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.PRINTER_IP, printerIPTCP);
                    editor.apply();

                    Toast.makeText(ConnectIPActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }


            case R.id.btnPrint:
                try {

                    SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
                    String printIp = pref.getString(Constants.PRINTER_IP, "");

                    NewPrintHelper printHelper = new NewPrintHelper(ConnectIPActivity.this, printIp, PrintReceiptType.TEST);
                    printHelper.runPrintReceiptSequence();
                } catch (Exception e) {
                }

        }
    }

    public static byte[] IsIPValid(String ip) {
        byte[] ipbytes = new byte[4];
        int valid = 0;
        int s, e;
        String ipstr = ip + ".";
        s = 0;
        for (e = 0; e < ipstr.length(); e++) {
            if ('.' == ipstr.charAt(e)) {
                if ((e - s > 3) || (e - s) <= 0)    // 最长3个字符
                    return null;

                int ipbyte = -1;
                try {
                    ipbyte = Integer.parseInt(ipstr.substring(s, e));
                    if (ipbyte < 0 || ipbyte > 255)
                        return null;
                    else
                        ipbytes[valid] = (byte) ipbyte;
                } catch (NumberFormatException exce) {
                    return null;
                }
                s = e + 1;
                valid++;
            }
        }
        if (valid == 4)
            return ipbytes;
        else
            return null;
    }

    public class TaskOpen implements Runnable {
        NETPrinting net = null;
        String ip = null;
        int port;
        Context context;

        public TaskOpen(NETPrinting net, String ip, int port, Context context) {
            this.net = net;
            this.ip = ip;
            this.port = port;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            net.Open(ip, port, 5000, context);
        }
    }

    static int dwWriteIndex = 1;

    public class TaskPrint implements Runnable {
        Pos pos = null;

        public TaskPrint(Pos pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, Constants.nPrintWidth, Constants.bCutter, Constants.bDrawer, Constants.bBeeper, Constants.nPrintCount, Constants.nPrintContent, Constants.nCompressMethod);
            final boolean bIsOpened = pos.GetIO().IsOpened();

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
                    mActivity.btnPrint.setEnabled(bIsOpened);
                }
            });

        }


    }

    public class TaskClose implements Runnable {
        NETPrinting net = null;

        public TaskClose(NETPrinting net) {
            this.net = net;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            net.Close();
        }

    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnConnect.setEnabled(false);
                btnDisconnect.setEnabled(true);
                btnPrint.setEnabled(true);
                Toast.makeText(mActivity, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnConnect.setEnabled(true);
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                Toast.makeText(mActivity, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnConnect.setEnabled(true);
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
            }
        });
    }

}