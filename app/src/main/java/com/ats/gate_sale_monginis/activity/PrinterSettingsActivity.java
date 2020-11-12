package com.ats.gate_sale_monginis.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.View.OnClickListener;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.util.NewPrintHelper;
import com.ats.gate_sale_monginis.util.PermissionUtil;
import com.ats.gate_sale_monginis.util.PrintReceiptType;
import com.ats.gate_sale_monginis.util.Prints;
import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class PrinterSettingsActivity extends AppCompatActivity implements OnClickListener, IOCallBack {

    private EditText edIP, edPort;
    private Button btnSave, btnTest, btnAdvance;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    NETPrinting mNet = new NETPrinting();

    PrinterSettingsActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_settings);

        mActivity = this;

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

        edIP = findViewById(R.id.edPrinterIP);
        edPort = findViewById(R.id.edPort);
        btnSave = findViewById(R.id.btnPrinterSave);
        btnTest = findViewById(R.id.btnPrinterTest);
        btnAdvance = findViewById(R.id.btnAdvance);

        try {
            SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String ip = pref.getString(Constants.PRINTER_IP, "");
            edIP.setText(ip);
        } catch (Exception e) {
        }

        btnSave.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnAdvance.setOnClickListener(this);

        mPos.Set(mNet);
        mNet.SetCallBack(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPrinterSave) {

            if (edIP.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Insert Printer IP Address", Toast.LENGTH_SHORT).show();
                edIP.requestFocus();
            }
            /*else if (!validate(edIP.getText().toString())) {
                Toast.makeText(this, "Invalid IP Address", Toast.LENGTH_SHORT).show();
                edIP.requestFocus();
            }*/
            /*else if (edPort.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Insert Port Number", Toast.LENGTH_SHORT).show();
                edPort.requestFocus();
            }*/
            else {
                SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                String ip = edIP.getText().toString();
                int port = Integer.parseInt(edPort.getText().toString());

                String printerIPTCP = "TCP:" + ip;

                editor.putString(Constants.PRINTER_IP, printerIPTCP);
                editor.putInt("Port", port);
                editor.apply();

                Toast.makeText(PrinterSettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Log.e("IP ADDRESS", "-------------------" + ip);
                Log.e("PORT: ", "-------------------" + port);
               // es.submit(new TaskOpen(mNet, ip, port, mActivity));

            }


        } else if (view.getId() == R.id.btnPrinterTest) {

            /*try {
                es.submit(new TaskPrint(mPos));
            } catch (Exception e) {
            }*/


            try {

                SharedPreferences pref = getSharedPreferences(Constants.PRINTER_PREF, MODE_PRIVATE);
                String printIp = pref.getString(Constants.PRINTER_IP, "");

                NewPrintHelper printHelper = new NewPrintHelper(PrinterSettingsActivity.this, printIp, PrintReceiptType.TEST);
                printHelper.runPrintReceiptSequence();
            } catch (Exception e) {
            }


        } else if (view.getId() == R.id.btnAdvance) {
            startActivity(new Intent(PrinterSettingsActivity.this, ConnectIPActivity.class));
        }
    }

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
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
            // TODO Auto-generated method stub

            //final int bPrintResult = Prints.PrintTestReceipt(getApplicationContext(), pos, Constants.nPrintCount, Constants.nPrintContent);
            final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, Constants.nPrintWidth, Constants.bCutter, Constants.bDrawer, Constants.bBeeper, Constants.nPrintCount, Constants.nPrintContent, Constants.nCompressMethod);

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnOpen", "-----Connected");
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
                Log.e("OnOpenFailed", "-----Not Connected");
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
                Log.e("OnClose", "-----Connection Closed");
                Toast.makeText(mActivity, "Closed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
