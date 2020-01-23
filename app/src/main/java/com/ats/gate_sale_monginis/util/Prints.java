package com.ats.gate_sale_monginis.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.activity.LoginActivity;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.google.gson.Gson;
import com.lvrenyang.io.Pos;

import java.util.ArrayList;


public class Prints {

    /*public static int PrintTicket(Context ctx, Pos pos, int nPrintWidth, boolean bCutter, boolean bDrawer, boolean bBeeper, int nCount, int nPrintContent, int nCompressMethod) {
        int bPrintResult = -6;
        StringBuilder textData = new StringBuilder();

        byte[] status = new byte[1];
        Log.e("STATUS : ", "-----------------" + status[0]);
        if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
            if ((status[0] & 0x08) == 0) {
                if (pos.POS_QueryStatus(status, 3000, 2)) {

                    for (int i = 0; i < nCount; ++i) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_FeedLine();
                            pos.POS_S_Align(1);

                            textData.append("\t\t\bBill\n");
                            textData.append("Bill No :- 123\t Date :-5/4/2018\n\n");

                            pos.POS_TextOut(textData.toString(), 0, 0, 0, 0, 0, 0);

                            pos.POS_TextOut(textData.toString(), 4, 0, 0, 0, 0, 0);

                            pos.POS_S_TextOut(textData.toString(), 0, 0, 0, 0, 0);

                            pos.POS_FeedLine();

                            Log.e("PRINT: ", "--------------" + textData.toString());
                        }

                    }

                    if (bBeeper)
                        pos.POS_Beep(1, 5);
                    if (bCutter)
                        pos.POS_CutPaper();
                    if (bDrawer)
                        pos.POS_KickDrawer(0, 100);

                    bPrintResult = pos.POS_TicketSucceed(0, 30000);
                } else {
                    bPrintResult = -8;
                }
            } else {
                bPrintResult = -4;
            }
        } else {
            bPrintResult = -7;
        }

        return bPrintResult;
    }

    public static String ResultCodeToString(int code) {
        switch (code) {
            case 0:
                return "Print successfully";//Print successfully
            case -1:
                return "Disconnect";//Disconnect
            case -2:
                return "Write failed";//Write failed
            case -3:
                return "Read failed";//Read failed
            case -4:
                return "Printer is offline";//Printer is offline
            case -5:
                return "Printer is out of paper";//Printer is out of paper
            case -7:
                return "Real-time status query failed";//Real-time status query failed
            case -8:
                return "Failed to check status";//Failed to check status
            case -6:
            default:
                return "unknown mistake";//unknown mistake
        }
    }


    public static int PrintTestReceipt(Context ctx, Pos pos, int nCount, int nPrintContent) {
        int bPrintResult = -6;
        StringBuilder textData = new StringBuilder();

        byte[] status = new byte[1];
        Log.e("STATUS : ", "-----------------" + status[0]);
        if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
            if ((status[0] & 0x08) == 0) {
                if (pos.POS_QueryStatus(status, 3000, 2)) {

                    for (int i = 0; i < nCount; ++i) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_FeedLine();
                            pos.POS_S_Align(1);

                            textData.append("\t\bTEST RECEIPT\n");
                            textData.append("\tDate :- " + Calendar.getInstance().getTime() + "\n\n");

                            pos.POS_TextOut(textData.toString(), 0, 0, 0, 0, 0, 0);

                            pos.POS_FeedLine();

                            Log.e("PRINT: ", "--------------" + textData.toString());
                        }


                    }

                    pos.POS_CutPaper();

                    bPrintResult = pos.POS_TicketSucceed(0, 30000);
                } else {
                    bPrintResult = -8;
                }
            } else {
                bPrintResult = -4;
            }
        } else {
            bPrintResult = -7;
        }

        return bPrintResult;
    }


    public static int PrintReceipt(Context ctx, Pos pos, int nCount, int nPrintContent, BillHeaderListData billHeader) {
        int bPrintResult = -6;
        StringBuilder textData = new StringBuilder();

        byte[] status = new byte[1];
        Log.e("STATUS : ", "-----------------" + status[0]);


        if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
            if ((status[0] & 0x08) == 0) {
                if (pos.POS_QueryStatus(status, 3000, 2)) {

                    for (int i = 0; i < nCount; ++i) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_FeedLine();
                            pos.POS_S_Align(1);


                            try {
                                ArrayList<GateSaleBillDetailList> orderItems = (ArrayList<GateSaleBillDetailList>) billHeader.getGateSaleBillDetailList();

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

                                Log.e("Print ", "\n\n" + textData.toString());

                                pos.POS_TextOut(textData.toString(), 0, 0, 0, 0, 0, 0);

                                pos.POS_FeedLine();
                                Log.e("PRINT: ", "--------------" + textData.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    pos.POS_CutPaper();

                    bPrintResult = pos.POS_TicketSucceed(0, 30000);
                } else {
                    bPrintResult = -8;
                }
            } else {
                bPrintResult = -4;
            }
        } else {
            bPrintResult = -7;
        }

        return bPrintResult;
    }
*/


    public static int PrintTicket(Context ctx, Pos pos, int nPrintWidth, boolean bCutter, boolean bDrawer, boolean bBeeper, int nCount, int nPrintContent, int nCompressMethod) {
        int bPrintResult = -6;
        StringBuilder textData = new StringBuilder();

        byte[] status = new byte[1];
        Log.e("STATUS : ", "-----------------" + status[0]);
        if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
            if ((status[0] & 0x08) == 0) {
                if (pos.POS_QueryStatus(status, 3000, 2)) {

                    for (int i = 0; i < nCount; ++i) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_FeedLine();
                            pos.POS_S_Align(1);

                            textData.append("\t\t\bBill\n");
                            textData.append("Bill No :- 123\t Date :-9/4/2018\n\n");

                            pos.POS_TextOut(textData.toString(), 0, 0, 0, 0, 0, 0);

                            pos.POS_TextOut(textData.toString(), 4, 0, 0, 0, 0, 0);

                            pos.POS_S_TextOut(textData.toString(), 0, 0, 0, 0, 0);

                            pos.POS_FeedLine();

                            Log.e("PRINT: ", "--------------" + textData.toString());
                        }


                    }

                    if (bBeeper)
                        pos.POS_Beep(1, 5);
                    if (bCutter)
                        pos.POS_CutPaper();
                    if (bDrawer)
                        pos.POS_KickDrawer(0, 100);

                    bPrintResult = pos.POS_TicketSucceed(0, 30000);
                } else {
                    bPrintResult = -8;
                }
            } else {
                bPrintResult = -4;
            }
        } else {
            bPrintResult = -7;
        }

        return bPrintResult;
    }


    public static int PrintReceipt(Context ctx, Pos pos, int nCount, int nPrintContent, BillHeaderListData billHeader) {
        int bPrintResult = -6;
        StringBuilder textData = new StringBuilder();
        String user = "";

        SharedPreferences pref = ctx.getSharedPreferences(Constants.MY_PREF, ctx.MODE_PRIVATE);
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


        byte[] status = new byte[1];
        Log.e("STATUS : ", "-----------------" + status[0]);
        if (pos.POS_RTQueryStatus(status, 1, 3000, 2) && ((status[0] & 0x12) == 0x12)) {
            if ((status[0] & 0x08) == 0) {
                if (pos.POS_QueryStatus(status, 3000, 2)) {

                    for (int i = 0; i < nCount; ++i) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_FeedLine();
                            pos.POS_S_Align(1);

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

                                pos.POS_TextOut(textData.toString(), 0, 0, 0, 0, 0, 0);

                                pos.POS_FeedLine();
                                Log.e("PRINT: ", "--------------" + textData.toString());

                            } catch (Exception e) {
                                Log.e("PrintReceipt","----------EXCEPTION : "+e.getMessage());
                                e.printStackTrace();
                            }
                        }

                    }
                    pos.POS_CutPaper();
                    bPrintResult = pos.POS_TicketSucceed(0, 30000);
                } else {
                    bPrintResult = -8;
                }
            } else {
                bPrintResult = -4;
            }
        } else {
            bPrintResult = -7;
        }

        return bPrintResult;
    }


    public static String ResultCodeToString(int code) {
        switch (code) {
            case 0:
                return "Print successfully";//Print successfully
            case -1:
                return "Disconnect";//Disconnect
            case -2:
                return "Write failed";//Write failed
            case -3:
                return "Read failed";//Read failed
            case -4:
                return "Printer is offline";//Printer is offline
            case -5:
                return "Printer is out of paper";//Printer is out of paper
            case -7:
                return "Real-time status query failed";//Real-time status query failed
            case -8:
                return "Failed to check status";//Failed to check status
            case -6:
            default:
                return "unknown mistake";//unknown mistake
        }
    }

}
