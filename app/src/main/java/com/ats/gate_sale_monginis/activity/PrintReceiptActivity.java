package com.ats.gate_sale_monginis.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.util.NewPrintHelper;
import com.ats.gate_sale_monginis.util.PermissionUtil;
import com.ats.gate_sale_monginis.util.PrintReceiptType;
import com.ats.gate_sale_monginis.util.Prints;
import com.google.gson.Gson;
import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrintReceiptActivity extends AppCompatActivity implements IOCallBack {

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    NETPrinting mNet = new NETPrinting();

    PrintReceiptActivity mActivity;

    String ip, bean;
    int port;
    BillHeaderListData billHeaderListData = new BillHeaderListData();

    private Button btnPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_receipt);

        mActivity = this;

        btnPrint=findViewById(R.id.btnPrint);

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

        try {
            SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
            ip = pref.getString(Constants.PRINTER_IP, "");
            port = pref.getInt("Port", 9100);

        } catch (Exception e) {
            Log.e("IP : ","*********************** "+ip);
            e.printStackTrace();
        }

        try {
            bean = getIntent().getExtras().getString("Bean");

        } catch (Exception e) {
        }

        Gson gson = new Gson();
        billHeaderListData = gson.fromJson(bean, BillHeaderListData.class);

        Log.e("IP : ", "" + ip);
        Log.e("PORT : ", "" + port);
        Log.e("BillHeaderListData : ", "" + billHeaderListData);

        mPos.Set(mNet);
        mNet.SetCallBack(this);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

      //  es.submit(new TaskOpen(mNet, ip, port, mActivity));


        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    NewPrintHelper printHelper = new NewPrintHelper(PrintReceiptActivity.this, ip, billHeaderListData, PrintReceiptType.RECEIPT);
                    printHelper.runPrintReceiptSequence();

                } catch (Exception e) {
                }

                // es.submit(new TaskOpen(mNet, ip, port, mActivity));
            }
        });

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


    public class TaskPrint implements Runnable {
        Pos pos = null;

        public TaskPrint(Pos pos) {
            this.pos = pos;
        }

        @Override
        public void run() {

            final int bPrintResult = Prints.PrintReceipt(getApplicationContext(), mPos, Constants.nPrintCount, Constants.nPrintContent, billHeaderListData);
           // finish();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("RESULT : ","-----------------"+bPrintResult);
                    // Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    public void OnOpen() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnOpen", "-----Connected");
                try {
                    es.submit(new TaskPrint(mPos));
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public void OnOpenFailed() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                //Toast.makeText(PrintReceiptActivity.this, "Printer Not Connected", Toast.LENGTH_SHORT).show();
                Log.e("OnOpenFailed", "-----Not Connected");
                try {
                    es.submit(new TaskPrint(mPos));
                } catch (Exception e) {
                }
                finish();
            }
        });

    }

    @Override
    public void OnClose() {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnClose", "-----Connection Closed");
                // Toast.makeText(PrintReceiptActivity.this, "Connection Closed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

}
