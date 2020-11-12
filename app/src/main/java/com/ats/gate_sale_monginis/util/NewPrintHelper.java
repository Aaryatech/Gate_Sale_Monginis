package com.ats.gate_sale_monginis.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPrintHelper implements ReceiveListener {

    Activity activity;
    String printerAddress;
    int modelConstant;
    int printReceiptType;
    private Printer mPrinter = null;
    public static String tableType = null;

    BillHeaderListData billHeader;
    String tableName;

    static {
        try {
            System.loadLibrary("libepos2.so");
        } catch (UnsatisfiedLinkError e) {
            Log.e("UnsatisfiedLinkError", "-----------------------" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "------------------------" + e.getMessage());
        }
    }


    //TEST
    public NewPrintHelper(Activity activity, String printerAddress, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_M30; //ModelConstant;
        this.printReceiptType = printReceiptType;
    }

    //RECEIPT
    public NewPrintHelper(Activity activity, String printerAddress, BillHeaderListData billHeader, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_M30;
        this.billHeader = billHeader;
        this.printReceiptType = printReceiptType;
    }

    public boolean createReceiptData() {
        if (mPrinter == null) {
            return false;
        }

        if (printReceiptType == PrintReceiptType.RECEIPT) {
            return createPrintReceipt(activity, billHeader);
        } else {
            return createTestReceipt();
        }
    }

    private boolean createTestReceipt() {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append("This is a TEST receipt\n");
            mPrinter.addText(textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);
            Log.e("PRINT : ", "" + textData);
        } catch (Exception e) {
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }

    private boolean createPrintReceipt(Activity activity, BillHeaderListData billHeader) {
        String method = "";
        StringBuilder textData = new StringBuilder();
        String user = "";

        try {

            SharedPreferences pref = activity.getSharedPreferences(Constants.MY_PREF, activity.MODE_PRIVATE);
            Gson gson = new Gson();
            String json2 = pref.getString("loginData", "");
            LoginData userBean = gson.fromJson(json2, LoginData.class);
            Log.e("User Bean : ", "-------*--*--*--------" + userBean);
            try {
                if (userBean != null) {
                    user = userBean.getUserName();
                }
            } catch (Exception e) {
                Log.e("PrintReceipt : ", " Exception : " + e.getMessage());
            }


            try {
                ArrayList<GateSaleBillDetailList> orderItems = (ArrayList<GateSaleBillDetailList>) billHeader.getGateSaleBillDetailList();

                String date = billHeader.getBillDate();
                textData.append("\nBILL\n");

                textData.append("Date :- " + date + "\n");

                //  textData.append("Bill No :- " + billHeader.getBillId());
                textData.append("Invoice No :- " + billHeader.getInvoiceNo() + "\n");

                if (!user.isEmpty()) {
                    textData.append("User :- " + user + "\n\n");
                } else {
                    textData.append("\n");
                }

                if (billHeader.getCategory() == 1) {
                    if (billHeader.getGateSaleBillDetailList().size() == 1) {
                        if (billHeader.getBillGrantAmt() == 0) {
                            String hpyBday = "Happy Birthday";
                            int difference = 47 - hpyBday.length();
                            int half = difference / 2;

                            for (int a = 0; a < half; a++) {
                                textData.append(" ");
                            }
                            textData.append(hpyBday);
                            for (int a = 1; a < half; a++) {
                                textData.append(" ");
                            }
                            textData.append("\n\n");
                        }
                    }
                }

                Log.e("OrderItems : ", "-----------" + orderItems.toString());


                textData.append("Item");
                for (int a = 0; a < 18; a++) {
                    textData.append(" ");
                }

                textData.append("   Qty   ");
                textData.append("  Rate   ");
                textData.append(" Amount\n");


                for (int a = 0; a < 47; a++) {
                    textData.append("-");
                }
                textData.append("\n");

                double billTotal = 0;
                for (int a = 0; a < orderItems.size(); a++) {

                    String strName = orderItems.get(a).getItemName();
                    if (strName.length() >= 22) {
                        String itemName = orderItems.get(a).getItemName().substring(0, 22);
                        textData.append(itemName);
                    } else if (strName.length() < 22) {
                        textData.append(strName);
                        int difference = 22 - strName.length();

                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                    }

                    int floatQty = (int) orderItems.get(a).getItemQty();
                    String qty = String.valueOf(floatQty);
                    double totalDouble = orderItems.get(a).getItemRate() * orderItems.get(a).getItemQty();
                    //String rate = String.valueOf(rateDouble);
                    String total = String.format("%.1f", totalDouble);
                    String rate = String.valueOf(orderItems.get(a).getItemRate());

                    billTotal = billTotal + totalDouble;


                    try {

                        textData.append("   ");
                        int difference = 3 - qty.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                        textData.append("" + qty);

                        textData.append("   ");
                        difference = 6 - rate.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                        textData.append("" + rate);

                        textData.append("   ");
                        difference = 7 - total.length();
                        for (int d = 0; d < difference; d++) {
                            textData.append(" ");
                        }
                        textData.append("" + total + "\n");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (int a = 0; a < 47; a++) {
                    textData.append("-");
                }

                textData.append("\n");

                String billTot = "Bill Total : " + String.format("%.1f", billTotal);
                int difference = 47 - billTot.length();
                for (int d = 0; d < difference; d++) {
                    textData.append(" ");
                }
                textData.append("" + billTot + "\n");

                String disc = "Discount : " + billHeader.getDiscountPer() + " %";
                difference = 47 - disc.length();
                for (int d = 0; d < difference; d++) {
                    textData.append(" ");
                }
                textData.append("" + disc + "\n\n");

                for (int a = 0; a < 47; a++) {
                    textData.append("-");
                }

                textData.append("\n");

                String tot = "Total : " + billHeader.getBillGrantAmt();
                difference = 47 - tot.length();
                for (int d = 0; d < difference; d++) {
                    textData.append(" ");
                }
                textData.append("" + tot + "\n");

                for (int a = 0; a < 47; a++) {
                    textData.append("-");
                }

                textData.append("\n\n");
                textData.append(".");

                mPrinter.addText(textData.toString());
                Log.e("Print ", "\n\n" + textData.toString());

                mPrinter.addCut(Printer.CUT_FEED);
                Log.e("PRINT: ", "--------------" + textData.toString());

            } catch (Exception e) {
                Log.e("PrintReceipt", "----------EXCEPTION : " + e.getMessage());
                e.printStackTrace();
                ShowMsg.showException(e, method, activity, false);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }



    public boolean runPrintReceiptSequence() {
        try {
            if (!initializeObject()) {
                return false;
            }

            if (!createReceiptData()) {
                finalizeObject();
                return false;
            }

            if (!printData()) {
                finalizeObject();
                return false;
            }

        } catch (Exception e) {
        }

        return true;
    }


    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();
        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), activity, false);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "sendData", activity, false);
            try {
                mPrinter.disconnect();
            } catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }


    private boolean initializeObject() {
        try {
            mPrinter = new Printer(modelConstant,
                    Printer.MODEL_ANK,
                    activity);
        } catch (UnsatisfiedLinkError e) {
            Log.e("UnsatisfiedLinkError", "-----initializeObject" + e.getMessage());
            Toast.makeText(activity, "Please Check Printer IP, Printer Must Be In Same Network", Toast.LENGTH_SHORT).show();
        } catch (NoClassDefFoundError e) {
            Log.e("NoClassDefFoundError", "-----initializeObject" + e.getMessage());
            Toast.makeText(activity, "Please Check Printer IP, Printer Must Be In Same Network", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("initializeObject", "-----------------------------------------------------------------");
            ShowMsg.showException(e, "Printer", activity, false);
            return false;
        }

        mPrinter.setReceiveEventListener(this);
        return true;
    }


    /**
     * Release all printer resources
     */
    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }
        mPrinter.clearCommandBuffer();
        mPrinter.setReceiveEventListener(null);
        mPrinter = null;
    }


    private boolean connectPrinter() {
        Log.e("connectPrinter", "----------------------------");
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            Log.e("connectPrinter", "----------------------------printerAddress" + printerAddress);
            mPrinter.connect(printerAddress, Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            Log.e("connectPrinter", "----------------------------showException" + e.getMessage());
            e.printStackTrace();
            Log.e("connectPrinter", "---------------------------------------------------------------------------");
            ShowMsg.showException(e, "connect", activity, false);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            Log.e("connectPrinter", "----------------------------Exception");
            Log.e("connectPrinter", "beginTransaction---------------------------------------------------------------------------");
            ShowMsg.showException(e, "beginTransaction", activity, false);
        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        } catch (final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.e("disconnectPrinter", "endTransaction---------------------------------------------------------------------------");
                    ShowMsg.showException(e, "endTransaction", activity, false);
                }
            });
        }

        try {
            mPrinter.disconnect();
        } catch (final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    Log.e("disconnectPrinter", "---------------------------------------------------------------------------");
                    ShowMsg.showException(e, "disconnect", activity, false);
                }
            });
        }

        finalizeObject();
    }


    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;//print available
        }

        return true;
    }


    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_autocutter);
            msg += activity.getResources().getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_overheat);
                msg += activity.getResources().getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += activity.getResources().getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += activity.getResources().getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                Log.e("onPtrReceive", "---------------------------------------------------------------------------");
                ShowMsg.showResult(code, makeErrorMessage(status), activity, false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }


}
