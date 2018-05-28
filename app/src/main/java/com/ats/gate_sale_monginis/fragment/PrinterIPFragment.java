package com.ats.gate_sale_monginis.fragment;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.util.PermissionUtil;
import com.ats.gate_sale_monginis.util.Prints;
import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class PrinterIPFragment extends Fragment implements View.OnClickListener, IOCallBack {

    private EditText edIP, edPort;
    private Button btnSave, btnTest;

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    NETPrinting mNet = new NETPrinting();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_i, container, false);
        tvTitle.setText("Printer Settings");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        edIP = view.findViewById(R.id.edPrinterIP);
        edPort = view.findViewById(R.id.edPort);
        btnSave = view.findViewById(R.id.btnPrinterSave);
        btnTest = view.findViewById(R.id.btnPrinterTest);

        try {
            SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String ip = pref.getString("IP", "");
            edIP.setText(ip);
        } catch (Exception e) {
        }

        btnSave.setOnClickListener(this);
        btnTest.setOnClickListener(this);

        mPos.Set(mNet);
        mNet.SetCallBack(this);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPrinterSave) {

            if (edIP.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Insert Printer IP Address", Toast.LENGTH_SHORT).show();
                edIP.requestFocus();
            } else if (!validate(edIP.getText().toString())) {
                Toast.makeText(getActivity(), "Invalid IP Address", Toast.LENGTH_SHORT).show();
                edIP.requestFocus();
            } else if (edPort.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Insert Port Number", Toast.LENGTH_SHORT).show();
                edPort.requestFocus();
            } else {
                SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                String ip = edIP.getText().toString();
                int port = Integer.parseInt(edPort.getText().toString());

                editor.putString("IP", ip);
                editor.putInt("Port", port);
                editor.apply();

                Toast.makeText(getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();

                Log.e("IP ADDRESS", "-------- SAVE -------" + ip);
                Log.e("PORT", "-------- SAVE -------" + port);
                es.submit(new TaskOpen(mNet, ip, port, getContext()));


            }


        } else if (view.getId() == R.id.btnPrinterTest) {
            try {
                es.submit(new TaskPrint(mPos));
            } catch (Exception e) {
            }
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
            Log.e("TaskOpen", "-------------------run");
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

            //final int bPrintResult = Prints.PrintTestReceipt(getContext(), pos, Constants.nPrintCount, Constants.nPrintContent);
            final int bPrintResult = Prints.PrintTicket(getContext(), pos, Constants.nPrintWidth, Constants.bCutter, Constants.bDrawer, Constants.bBeeper, Constants.nPrintCount, Constants.nPrintContent, Constants.nCompressMethod);
            final boolean bIsOpened = pos.GetIO().IsOpened();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity().getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnOpen", "----------Success");
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnOpenFailed", "----------Failed");
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.e("OnClose", "----------Closed");
            }
        });
    }


}
