package com.ats.gate_sale_monginis.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;

import java.util.ArrayList;

/**
 * Created by MAXADMIN on 27/1/2018.
 */

//public class PrintHelper implements ReceiveListener {
public class PrintHelper {
/*
    Activity activity;
    String printerAddress;
    int modelConstant;
    int printReceiptType;
    private Printer mPrinter = null;
    public static String tableType = null;
    private BillHeaderListData billHeaderListData;

    static {
        try {
            System.loadLibrary("libepos2.so");
        } catch (UnsatisfiedLinkError e) {
            Log.e("UnsatisfiedLinkError", "-----------------------" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "------------------------" + e.getMessage());
        }
    }


    public PrintHelper(Activity activity, String printerAddress, int ModelConstant, int printReceiptType) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82; //ModelConstant;
        this.printReceiptType = printReceiptType;
    }

    public PrintHelper(Activity activity, String printerAddress, int modelConstant) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82;
    }

    public PrintHelper(Activity activity, String printerAddress, int modelConstant, BillHeaderListData billHeaderListData) {
        this.activity = activity;
        this.printerAddress = printerAddress;
        this.modelConstant = Printer.TM_T82;
        this.billHeaderListData = billHeaderListData;
    }


    public boolean createReceiptData() {
        if (mPrinter == null) {
            return false;
        }

        if (printReceiptType == PrintReceiptType.BILL) {
            //create bill invoice
            return createBillReceiptPrint(billHeaderListData);
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
            // textData.append("\tThis is a TEST receipt\n");
            mPrinter.addText(textData.toString());
            mPrinter.addCut(Printer.CUT_FEED);
        } catch (Exception e) {
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }


    private boolean createBillReceiptPrint(BillHeaderListData billHeader) {
        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            ArrayList<GateSaleBillDetailList> orderItems = (ArrayList<GateSaleBillDetailList>) billHeader.getGateSaleBillDetailList();


            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            String date = billHeader.getBillDate();
            textData.append("\n\t\t\tBILL\n");

            textData.append("Date :- " + date + "\n");

            textData.append("Bill No :- " + billHeader.getBillId());
            textData.append("\tInvoice No :- " + billHeader.getInvoiceNo() + "\n\n");


            if (billHeader.getCategory() == 1) {
                if (billHeader.getGateSaleBillDetailList().size() == 1) {
                    if (billHeader.getBillGrantAmt() == 0) {
                        String hpyBday = "Happy Birthday";
                        int difference = 47 - hpyBday.length();
                        int half = difference / 2;

                        for (int i = 0; i < half; i++) {
                            textData.append(" ");
                        }
                        textData.append(hpyBday);
                        for (int i = 1; i < half; i++) {
                            textData.append(" ");
                        }
                        textData.append("\n\n");
                    }
                }
            }

            Log.e("OrderItems : ", "-----------" + orderItems.toString());

            mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);


            textData.append("Item");
            for (int i = 0; i < 18; i++) {
                textData.append(" ");
            }

            textData.append("   Qty   ");
            textData.append("  Rate   ");
            textData.append(" Amount\n");


            for (int i = 0; i < 47; i++) {
                textData.append("-");
            }
            textData.append("\n");

            double billTotal = 0;
            for (int i = 0; i < orderItems.size(); i++) {

                String strName = orderItems.get(i).getItemName();
                if (strName.length() >= 22) {
                    String itemName = orderItems.get(i).getItemName().substring(0, 22);
                    textData.append(itemName);
                } else if (strName.length() < 22) {
                    textData.append(strName);
                    int difference = 22 - strName.length();

                    for (int d = 0; d < difference; d++) {
                        textData.append(" ");
                    }
                }

                int floatQty = (int) orderItems.get(i).getItemQty();
                String qty = String.valueOf(floatQty);
                double totalDouble = orderItems.get(i).getItemRate() * orderItems.get(i).getItemQty();
                //String rate = String.valueOf(rateDouble);
                String total = String.format("%.1f", totalDouble);
                String rate = String.valueOf(orderItems.get(i).getItemRate());

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

            for (int i = 0; i < 47; i++) {
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

            for (int i = 0; i < 47; i++) {
                textData.append("-");
            }

            textData.append("\n");

            String tot = "Total : " + billHeader.getBillGrantAmt();
            difference = 47 - tot.length();
            for (int d = 0; d < difference; d++) {
                textData.append(" ");
            }
            textData.append("" + tot + "\n");

            for (int i = 0; i < 47; i++) {
                textData.append("-");
            }

            textData.append("\n\n");

            mPrinter.addText(textData.toString());
            Log.e("Print ", "\n\n" + textData.toString());

            mPrinter.addCut(Printer.CUT_FEED);


        } catch (Exception e) {
            e.printStackTrace();
            ShowMsg.showException(e, method, activity, false);
            return false;
        }
        return true;
    }


    private boolean createBillReceipt() {

        String method = "";
        StringBuilder textData = new StringBuilder();

        try {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            method = "addFeedLine";
            mPrinter.addFeedLine(1);
            textData.append("------------------------------\n");
            mPrinter.addText(textData.toString());
        } catch (Exception e) {
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
            ShowMsg.showException(e, "Printer", activity, false);
            return false;
        }

        mPrinter.setReceiveEventListener(this);
        return true;
    }


    */
/**
     * Release all printer resources
     *//*

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
            ShowMsg.showException(e, "connect", activity, false);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {
            Log.e("connectPrinter", "----------------------------Exception");
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
*/
}
