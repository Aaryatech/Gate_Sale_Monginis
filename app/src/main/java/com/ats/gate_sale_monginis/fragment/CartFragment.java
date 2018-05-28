package com.ats.gate_sale_monginis.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.adapter.CartAdapter;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetail;
import com.ats.gate_sale_monginis.bean.GateSaleBillHeader;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvCartCount;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;


public class CartFragment extends Fragment implements View.OnClickListener {

    private TextView tvDiscount, tvSubmit, tvCategory, tvMonthly, tvYearly, tvMonthlyUsed, tvYearlyUsed;
    private ListView lvCartList;
    public static TextView tvTotal, tvGrandTotal;
    private LinearLayout llMoneyLimit, llMoneyUsed;
    CartAdapter adapter;

    int empId, monthlyLimit, yearlyLimit, catId, userId, userType, monthlyConsumed, yearlyConsumed;
    float percentDiscount;
    String empName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        tvTitle.setText("Cart");


        try {

            SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            Gson gson = new Gson();
            String json2 = pref.getString("loginData", "");
            LoginData userBean = gson.fromJson(json2, LoginData.class);
            Log.e("User Bean : ", "---------------" + userBean);
            if (userBean != null) {
                userId = userBean.getUserId();
                userType = userBean.getUserType();
            }

            empId = pref.getInt("eId", 0);
            empName = pref.getString("category", "");
            percentDiscount = pref.getFloat("catDiscount", 0);
            monthlyLimit = pref.getInt("monthlyLimit", 0);
            yearlyLimit = pref.getInt("yearlyLimit", 0);
            catId = pref.getInt("categoryId", 0);
            monthlyConsumed = pref.getInt("monthlyConsumed", 0);
            yearlyConsumed = pref.getInt("yearlyConsumed", 0);

            Log.e("Emp Id : ", " --- " + empId);
            Log.e("Emp Name : ", " --- " + empName);
            Log.e("Category Id : ", " --- " + catId);
            Log.e("Emp Percent : ", " --- " + percentDiscount);
            Log.e("Emp Monthly Limit : ", " --- " + monthlyLimit);
            Log.e("Emp Yearly limit : ", " --- " + yearlyLimit);
            Log.e("Emp Monthly Consumed : ", " --- " + monthlyConsumed);
            Log.e("Emp Yearly Consumed : ", " --- " + yearlyConsumed);

        } catch (Exception e) {
        }

        tvMonthly = view.findViewById(R.id.tvCart_MonthlyLimit);
        tvYearly = view.findViewById(R.id.tvCart_YearlyLimit);
        llMoneyLimit = view.findViewById(R.id.llCart_MoneyLimit);
        tvDiscount = view.findViewById(R.id.tvCart_Discount);
        tvSubmit = view.findViewById(R.id.tvCart_Submit);
        tvTotal = view.findViewById(R.id.tvCart_Total);
        tvGrandTotal = view.findViewById(R.id.tvCart_GrandTotal);
        lvCartList = view.findViewById(R.id.lvCartList);
        tvCategory = view.findViewById(R.id.tvCart_Category);
        tvMonthlyUsed = view.findViewById(R.id.tvCart_MonthlyUsed);
        tvYearlyUsed = view.findViewById(R.id.tvCart_YearlyUsed);
        llMoneyUsed = view.findViewById(R.id.llCart_MoneyUsed);

        tvCategory.setText("" + empName);
        tvDiscount.setText("" + percentDiscount + " %");


        if (catId != 1) {
            llMoneyLimit.setVisibility(View.GONE);
            llMoneyUsed.setVisibility(View.GONE);
        } else {
            llMoneyLimit.setVisibility(View.VISIBLE);
            llMoneyUsed.setVisibility(View.VISIBLE);
            tvYearly.setText("Yearly Limit: Rs. " + yearlyLimit);
            tvMonthly.setText("Monthly Limit: Rs. " + monthlyLimit);
            tvYearlyUsed.setText("Used: Rs. " + yearlyConsumed);
            tvMonthlyUsed.setText("Used: Rs. " + monthlyConsumed);
        }

        // tvTotal.setPaintFlags(tvTotal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvSubmit.setOnClickListener(this);

//        ArrayList<String> cartArray = new ArrayList<>();
//        cartArray.add("Black Forest Heart Cake");
//        cartArray.add("Black Forest Heart Cake");
//        cartArray.add("Black Forest Heart Cake");
//        cartArray.add("Black Forest Heart Cake");

        if (cartArray.size() > 0) {
            adapter = new CartAdapter(getContext(), cartArray);
            lvCartList.setAdapter(adapter);

            double totalSum = 0;
            for (int i = 0; i < cartArray.size(); i++) {
                totalSum = totalSum + (cartArray.get(i).getItemRate() * cartArray.get(i).getQuantity());
            }
            //tvTotal.setText("" + Math.round(totalSum));
            tvTotal.setText("" + String.format("%.2f", totalSum));

            double disc = totalSum * (percentDiscount / 100);
            tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));

        }


        return view;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tvCart_Submit) {
            if (cartArray.size() > 0) {
                if (catId != 1) {

                    ArrayList<GateSaleBillDetail> billItemArray = new ArrayList<>();
                    for (int i = 0; i < cartArray.size(); i++) {
                        GateSaleBillDetail billDetail = new GateSaleBillDetail(0, 0, cartArray.get(i).getId(), (float) cartArray.get(i).getQuantity(), (float) cartArray.get(i).getItemRate(), 0);
                        billItemArray.add(billDetail);
                    }
                    Log.e("Bill Item Array : ", " ---- " + billItemArray);

                    float grandTotal = 0, total = 0, disAmt = 0, temp = 0, roundGrandTotal = 0;

                    if (!tvGrandTotal.getText().toString().isEmpty()) {
                        grandTotal = Float.parseFloat(tvGrandTotal.getText().toString());
                        int grTot = (int) grandTotal;
                        roundGrandTotal = grTot;
                    }

                    if (!tvTotal.getText().toString().isEmpty()) {
                        total = Float.parseFloat(tvTotal.getText().toString());
                        temp = total * (percentDiscount / 100);
                        disAmt = total - temp;
                    }

                    int index = String.valueOf(grandTotal).indexOf(".");

                    int decimalLength = String.valueOf(grandTotal).length();

                    String decimal = String.valueOf(grandTotal).substring((index + 1), decimalLength);
                    Log.e("Round up : ", " -------- " + decimal);

                    float roundUp = Float.valueOf("0." + decimal);
                    Log.e("Round up Float : ", " -------- " + roundUp);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String todaysDate = sdf.format(Calendar.getInstance().getTimeInMillis());

                    if (catId == 1 || catId == 6) {

                        final GateSaleBillHeader billHeader = new GateSaleBillHeader(0, "", catId, 0, empName, empId, percentDiscount, total, temp, roundUp, roundGrandTotal, 2, todaysDate, userId, 1, "", 0, 1, userId, 0, billItemArray);


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getResources().getString(R.string.cart_dialog_title));
                        builder.setMessage(getResources().getString(R.string.cart_dialog_message));
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                saveOrder(billHeader);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //saveOrder(billHeader);

                    } else {

                        if (userType != 1) {
                            showDialog(total, temp, roundUp, roundGrandTotal, billItemArray, 2, todaysDate, userId, 1);
                        } else {
                            showDialog(total, temp, roundUp, roundGrandTotal, billItemArray, 1, "", 0, 0);
                        }
                    }

                } else {

                    float billTotal = Float.parseFloat(tvGrandTotal.getText().toString());
                    float consumed = monthlyConsumed;
                    float mLimit = monthlyLimit;
                    float monthlyRemaining = mLimit - consumed;
                    if (billTotal > monthlyRemaining) {
                        Toast.makeText(getActivity(), "Bill Amount Exceeds From Monthly Limit", Toast.LENGTH_SHORT).show();
                    } else {

                        ArrayList<GateSaleBillDetail> billItemArray = new ArrayList<>();
                        for (int i = 0; i < cartArray.size(); i++) {
                            GateSaleBillDetail billDetail = new GateSaleBillDetail(0, 0, cartArray.get(i).getId(), (float) cartArray.get(i).getQuantity(), (float) cartArray.get(i).getItemRate(), 0);
                            billItemArray.add(billDetail);
                        }
                        Log.e("Bill Item Array : ", " ---- " + billItemArray);

                        float grandTotal = 0, total = 0, disAmt = 0, temp = 0, roundGrandTotal = 0;

                        if (!tvGrandTotal.getText().toString().isEmpty()) {
                            grandTotal = Float.parseFloat(tvGrandTotal.getText().toString());
                            int grTot = (int) grandTotal;
                            roundGrandTotal = grTot;
                        }

                        if (!tvTotal.getText().toString().isEmpty()) {
                            total = Float.parseFloat(tvTotal.getText().toString());
                            temp = total * (percentDiscount / 100);
                            disAmt = total - temp;
                        }

                        int index = String.valueOf(grandTotal).indexOf(".");

                        int decimalLength = String.valueOf(grandTotal).length();

                        String decimal = String.valueOf(grandTotal).substring((index + 1), decimalLength);
                        Log.e("Round up : ", " -------- " + decimal);

                        float roundUp = Float.valueOf("0." + decimal);
                        Log.e("Round up Float : ", " -------- " + roundUp);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        String todaysDate = sdf.format(Calendar.getInstance().getTimeInMillis());

                        if (catId == 1 || catId == 6) {

                            final GateSaleBillHeader billHeader = new GateSaleBillHeader(0, "", catId, 0, empName, empId, percentDiscount, total, temp, roundUp, roundGrandTotal, 2, todaysDate, userId, 1, "", 0, 1, userId, 0, billItemArray);


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(getResources().getString(R.string.cart_dialog_title));
                            builder.setMessage(getResources().getString(R.string.cart_dialog_message));
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    saveOrder(billHeader);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            //saveOrder(billHeader);

                        } else {

                            if (userType != 1) {
                                showDialog(total, temp, roundUp, roundGrandTotal, billItemArray, 2, todaysDate, userId, 1);
                            } else {
                                showDialog(total, temp, roundUp, roundGrandTotal, billItemArray, 1, "", 0, 0);
                            }
                        }

                    }
                }

            } else {
                Toast.makeText(getActivity(), "Your Cart Is Empty!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDialog(final float total, final float disAmt, final float roundUp, final float grandTotal, final ArrayList<GateSaleBillDetail> billItemArray, final int isApprove, final String approveDate, final int approveUserId, final int billPrint) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_customer_name_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setTitle("Customer");
        final EditText edCustName = dialog.findViewById(R.id.edCustomDialog_CustName);
        TextView tvDialogSubmit = dialog.findViewById(R.id.tvCustomDialog_Submit);
        TextView tvDialogCancel = dialog.findViewById(R.id.tvCustomDialog_Cancel);

        tvDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edCustName.getText().toString().isEmpty()) {
                    edCustName.setError("Enter Customer Name");
                    edCustName.requestFocus();
                } else {
                    String custName = edCustName.getText().toString();

                    final GateSaleBillHeader billHeader = new GateSaleBillHeader(0, "", catId, 0, custName, empId, percentDiscount, total, disAmt, roundUp, grandTotal, isApprove, approveDate, approveUserId, 1, "", 0, billPrint, userId, 0, billItemArray);
                    Log.e("Bill Header : ", " ---- " + billHeader);

                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getResources().getString(R.string.cart_dialog_title));
                    builder.setMessage(getResources().getString(R.string.cart_dialog_message));
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            saveOrder(billHeader);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog1 = builder.create();
                    dialog1.show();

                    //saveOrder(billHeader);
                }
            }
        });

        tvDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void saveOrder(GateSaleBillHeader billHeader) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.saveBill(billHeader);
            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage message = response.body();
                            if (message.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                cartArray.clear();
                                // adapter.notifyDataSetChanged();
                                tvTotal.setText("0.0");
                                tvGrandTotal.setText("0.0");
                                tvCartCount.setText("0");

                                if (userType == 1) {
                                    Fragment adf = new ViewBillsFragment();
                                    Bundle args = new Bundle();
                                    args.putInt("TabNo", 0);
                                    adf.setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();
                                } else {
                                    Fragment adf = new ViewBillsFragment();
                                    Bundle args = new Bundle();
                                    args.putInt("TabNo", 1);
                                    adf.setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();
                                }

//                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                                ft.replace(R.id.content_frame, new HomeFragment(), "Home");
//                                ft.commit();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                            //Log.e("AddUser : ", " NULL ");
                            Log.e("Cart : ", "------------------  NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        //Log.e("AddUser : ", " Exception : " + e.getMessage());
                        Log.e("Cart : ", "-----------Exception-----" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.e("Cart : ", "-----------onFailure-----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}
