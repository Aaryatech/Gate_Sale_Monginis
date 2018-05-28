package com.ats.gate_sale_monginis.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class CashCollectionFragment extends Fragment implements View.OnClickListener {

    private TextView tvRegularTotal, tvOtherTotal, tvTotal, tvCollect, tvCancel, tvRTotal, tvOTotal;
    private ListView lvList;
    private LinearLayout llRegular, llOther, llRegularAll, llOtherAll;

    private ArrayList<BillHeaderListData> billData = new ArrayList<>();
    BillListAdapter adapter;
    int userId, userType;

    private CheckBox cbRegularAll, cbOtherAll;

    public static Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private ArrayList<BillHeaderListData> selectedIdArray = new ArrayList<>();
    private ArrayList<BillHeaderListData> selectedIdArrayOther = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_collection, container, false);
        tvTitle.setText("Collect Cash");

        tvRegularTotal = view.findViewById(R.id.tvCashCollect_RegularTotal);
        tvOtherTotal = view.findViewById(R.id.tvCashCollect_OtherTotal);
        tvTotal = view.findViewById(R.id.tvCashCollect_Total);
        tvCollect = view.findViewById(R.id.tvCashCollect_Collect);
        tvCancel = view.findViewById(R.id.tvCashCollect_Cancel);
        lvList = view.findViewById(R.id.lvCashCollect_List);
        llRegular = view.findViewById(R.id.llCashCollect_Regular);
        llOther = view.findViewById(R.id.llCashCollect_Other);
        llRegularAll = view.findViewById(R.id.llCashCollect_RegularAll);
        llOtherAll = view.findViewById(R.id.llCashCollect_OtherAll);

        tvRTotal = view.findViewById(R.id.tvCashCollect_RegTotal);
        tvOTotal = view.findViewById(R.id.tvCashCollect_OthTotal);

        cbRegularAll = view.findViewById(R.id.cbCustomCashCollect_All);
        cbOtherAll = view.findViewById(R.id.cbCustomCashCollect_Other_All);

        llRegular.setOnClickListener(this);
        llOther.setOnClickListener(this);
        tvCollect.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData userBean = gson.fromJson(json2, LoginData.class);
        try {

            if (userBean != null) {
                userId = userBean.getUserId();
                userType = userBean.getUserType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        llRegular.setBackgroundColor(getResources().getColor(R.color.colorSelected));
        llOther.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        llOtherAll.setVisibility(View.GONE);

        getBillList(2, 1);


        cbRegularAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (billData.size() > 0) {
                    ArrayList<BillHeaderListData> regularBillArray = new ArrayList<>();
                    for (int i = 0; i < billData.size(); i++) {
                        if (billData.get(i).getIsOther() == 0) {
                            regularBillArray.add(billData.get(i));
                        }
                    }

                    if (b) {
                        selectedIdArray.clear();
                        if (regularBillArray.size() > 0) {
                            float total = 0;
                            for (int i = 0; i < regularBillArray.size(); i++) {
                                map.put(regularBillArray.get(i).getBillId(), true);
                                total = total + regularBillArray.get(i).getBillGrantAmt();
                                selectedIdArray.add(regularBillArray.get(i));
                            }
                            adapter = new BillListAdapter(getContext(), regularBillArray, 1, total, 1);
                            lvList.setAdapter(adapter);
                        }
                    } else {

                        selectedIdArray.clear();
                        if (regularBillArray.size() > 0) {
                            float total = 0;
                            for (int i = 0; i < regularBillArray.size(); i++) {
                                map.put(regularBillArray.get(i).getBillId(), false);
                                total = total + regularBillArray.get(i).getBillGrantAmt();
                            }
                            adapter = new BillListAdapter(getContext(), regularBillArray, 2, total, 1);
                            lvList.setAdapter(adapter);
                        }


                    }
                }
            }
        });

        cbOtherAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (billData.size() > 0) {
                    ArrayList<BillHeaderListData> otherBillArray = new ArrayList<>();
                    for (int i = 0; i < billData.size(); i++) {
                        if (billData.get(i).getIsOther() == 1) {
                            otherBillArray.add(billData.get(i));
                        }
                    }

                    if (b) {
                        selectedIdArrayOther.clear();
                        if (otherBillArray.size() > 0) {
                            float total = 0;
                            for (int i = 0; i < otherBillArray.size(); i++) {
                                map.put(otherBillArray.get(i).getBillId(), true);
                                total = total + otherBillArray.get(i).getBillGrantAmt();
                                selectedIdArrayOther.add(otherBillArray.get(i));
                            }
                            adapter = new BillListAdapter(getContext(), otherBillArray, 1, total, 2);
                            lvList.setAdapter(adapter);
                        }

                    } else {
                        selectedIdArrayOther.clear();
                        if (otherBillArray.size() > 0) {
                            float total = 0;
                            for (int i = 0; i < otherBillArray.size(); i++) {
                                map.put(otherBillArray.get(i).getBillId(), false);
                                total = total + otherBillArray.get(i).getBillGrantAmt();
                            }
                            adapter = new BillListAdapter(getContext(), otherBillArray, 2, total, 2);
                            lvList.setAdapter(adapter);
                        }

                    }

                }

            }
        });


        return view;
    }


    public void getBillList(int isApproved, int amtCollectedStatus) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<BillHeaderListData>> arrayListCall = Constants.myInterface.getCashCollectionListForApprover(isApproved, amtCollectedStatus);
            arrayListCall.enqueue(new Callback<ArrayList<BillHeaderListData>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderListData>> call, Response<ArrayList<BillHeaderListData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<BillHeaderListData> data = response.body();

                            commonDialog.dismiss();
                            billData.clear();
                            billData = data;

                            if (data.size() > 0) {
                                ArrayList<BillHeaderListData> tempData = new ArrayList<>();
                                float totalRegular = 0, totalOther = 0, total = 0;
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getIsOther() == 0) {
                                        tempData.add(data.get(i));
                                        totalRegular = totalRegular + data.get(i).getBillGrantAmt();
                                    } else if (data.get(i).getIsOther() == 1) {
                                        totalOther = totalOther + data.get(i).getBillGrantAmt();
                                    }
                                    map.put(data.get(i).getBillId(), false);
                                }

                                total = totalRegular + totalOther;

                                tvRegularTotal.setText("" + String.format("%.2f", totalRegular) + "/-");
                                tvOtherTotal.setText("" + String.format("%.2f", totalOther) + "/-");
                                //tvTotal.setText("" + String.format("%.2f", total) + "/-");
                                tvTotal.setText("0.0");
                                tvRTotal.setText("0.0");
                                tvOTotal.setText("0.0");

                                adapter = new BillListAdapter(getContext(), tempData, 0, 0, 1);
                                lvList.setAdapter(adapter);

                            } else {
                                tvRegularTotal.setText("");
                                tvOtherTotal.setText("");
                                tvTotal.setText("");
                                tvOTotal.setText("0.0");
                                tvRTotal.setText("0.0");

                                adapter = new BillListAdapter(getContext(), billData, 0, 0, 2);
                                lvList.setAdapter(adapter);
                            }

                            cbRegularAll.setChecked(false);
                            cbOtherAll.setChecked(false);
                            llRegular.callOnClick();


                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BillHeaderListData>> call, Throwable
                        t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvCashCollect_Collect) {

            final ArrayList<BillHeaderListData> finalArray = new ArrayList<>();
            finalArray.addAll(selectedIdArray);
            finalArray.addAll(selectedIdArrayOther);

            if (finalArray.size() == 0) {
                Toast.makeText(getActivity(), "Please Select Bills", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setTitle("Confirm Action");
                builder.setMessage("Do You Want To Collect Amount?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //collectGateSaleCash(userId);

                        ArrayList<Integer> billIdArray = new ArrayList<>();
                        for (int i = 0; i < finalArray.size(); i++) {
                            billIdArray.add(finalArray.get(i).getBillId());
                        }
                        collectGateSaleCashApprover(userId, 2, billIdArray);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else if (view.getId() == R.id.tvCashCollect_Cancel) {

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();

        } else if (view.getId() == R.id.llCashCollect_Regular) {

            Log.e("REGULAR : ", "---------------------------CLICKED");

            llOtherAll.setVisibility(View.GONE);
            llRegularAll.setVisibility(View.VISIBLE);

            llRegular.setBackgroundColor(getResources().getColor(R.color.colorSelected));
            llOther.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            // getBillList(0);

            if (billData.size() > 0) {
                ArrayList<BillHeaderListData> regularBillArray = new ArrayList<>();
                float total = 0;
                for (int i = 0; i < billData.size(); i++) {
                    if (billData.get(i).getIsOther() == 0) {
                        regularBillArray.add(billData.get(i));
                        total = total + billData.get(i).getBillGrantAmt();
                    }
                }

                adapter = new BillListAdapter(getContext(), regularBillArray, 0, 0, 1);
                lvList.setAdapter(adapter);

            }


        } else if (view.getId() == R.id.llCashCollect_Other) {

            llOtherAll.setVisibility(View.VISIBLE);
            llRegularAll.setVisibility(View.GONE);

            // getBillList(1);
            llRegular.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            llOther.setBackgroundColor(getResources().getColor(R.color.colorSelected));
            if (billData.size() > 0) {
                ArrayList<BillHeaderListData> otherBillArray = new ArrayList<>();
                float total = 0;
                for (int i = 0; i < billData.size(); i++) {
                    if (billData.get(i).getIsOther() == 1) {
                        otherBillArray.add(billData.get(i));
                        total = total + billData.get(i).getBillGrantAmt();
                    }
                }

                adapter = new BillListAdapter(getContext(), otherBillArray, 0, 0, 2);
                lvList.setAdapter(adapter);

            }
        }
    }

    public void collectGateSaleCash(int id) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.collectAmount(id);
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
                                llRegular.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                                llOther.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                getBillList(2, 1);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void collectGateSaleCashApprover(int id, int amtCollectStatus, ArrayList<Integer> billIds) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.collectCash(id, amtCollectStatus, billIds);
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
                                llRegular.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                                llOther.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                getBillList(2, 1);
                                // cbRegularAll.setChecked(false);
                                //cbOtherAll.setChecked(false);
                                selectedIdArray.clear();
                                selectedIdArrayOther.clear();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("category", "");
        editor.putInt("categoryId", 0);
        editor.putInt("eId", 0);
        editor.putInt("monthlyLimit", 0);
        editor.putInt("yearlyLimit", 0);
        editor.putFloat("catDiscount", 0);
        editor.putFloat("monthlyConsumed", 0);
        editor.putFloat("yearlyConsumed", 0);
        editor.apply();

        llCart.setVisibility(View.INVISIBLE);
        cartArray.clear();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_Filter);
        item.setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //--------------ADAPTER CLASS-----------------------------

    public class BillListAdapter extends BaseAdapter {

        ArrayList<BillHeaderListData> displayedValues;
        ArrayList<BillHeaderListData> orderValues;
        Context context;
        private LayoutInflater inflater = null;
        int cbStatus;
        private Boolean isTouched = false;
        private float totalSumAmt = 0;
        int billType;

        public BillListAdapter(Context context, ArrayList<BillHeaderListData> billArray, int cbStatus, float totalSumAmt, int billType) {
            this.context = context;
            this.displayedValues = billArray;
            this.orderValues = billArray;
            this.inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.cbStatus = cbStatus;
            this.totalSumAmt = totalSumAmt;
            this.billType = billType;
        }

        @Override
        public int getCount() {
            return displayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return displayedValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class Holder {
            TextView tvDate, tvName, tvCategory, tvApproveIcon, tvRejectIcon, tvBillTotal, tvDiscount, tvPrintIcon, tvUserName, tvInvoice;
            ListView lvBillItems;
            LinearLayout llBillHead;
            CheckBox cbCheck;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final Holder holder;
            View rowView = convertView;

            if (rowView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(R.layout.custom_cash_collection_item, null);

                holder.tvName = rowView.findViewById(R.id.tvCustomCashCollect_Name);
                holder.tvDate = rowView.findViewById(R.id.tvCustomCashCollect_Date);
                holder.tvCategory = rowView.findViewById(R.id.tvCustomCashCollect_Category);
                holder.tvApproveIcon = rowView.findViewById(R.id.tvCustomCashCollect_ApproveIcon);
                holder.tvRejectIcon = rowView.findViewById(R.id.tvCustomCashCollect_RejectIcon);
                holder.lvBillItems = rowView.findViewById(R.id.lvCustomCashCollect_ItemsList);
                holder.llBillHead = rowView.findViewById(R.id.llCustomCashCollect_Head);
                holder.tvBillTotal = rowView.findViewById(R.id.tvCustomCashCollect_BillTotal);
                holder.tvDiscount = rowView.findViewById(R.id.tvCustomCashCollect_Discount);
                holder.tvPrintIcon = rowView.findViewById(R.id.tvCustomCashCollect_PrintIcon);
                holder.cbCheck = rowView.findViewById(R.id.cbCustomCashCollect);
                holder.tvUserName = rowView.findViewById(R.id.tvCustomCashCollect_UserName);
                holder.tvInvoice = rowView.findViewById(R.id.tvCustomCashCollect_Invoice);

                rowView.setTag(holder);

            } else {
                holder = (Holder) rowView.getTag();
            }

            Boolean value = map.get(displayedValues.get(position).getBillId());
            holder.cbCheck.setChecked(value);

            isTouched = false;
            holder.cbCheck.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isTouched = true;
                    Log.e("TRUE", "------------------------------------------------------------");
                    return false;
                }
            });

            holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isTouched) {
                        isTouched = false;
                        if (isChecked) {
                            map.put(displayedValues.get(position).getBillId(), true);
                            if (displayedValues.get(position).getIsOther() == 0) {
                                selectedIdArray.add(displayedValues.get(position));
                            } else {
                                selectedIdArrayOther.add(displayedValues.get(position));
                            }

                            ArrayList<BillHeaderListData> finalArray = new ArrayList<>();
                            finalArray.addAll(selectedIdArray);
                            finalArray.addAll(selectedIdArrayOther);

                            if (billType == 1) {
                                if (selectedIdArray.size() > 0) {
                                    float amt = 0;
                                    for (int i = 0; i < selectedIdArray.size(); i++) {
                                        amt = amt + selectedIdArray.get(i).getBillGrantAmt();
                                    }
                                    tvRTotal.setText("" + String.format("%.2f", amt));

                                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                                    float sumTotal = amt + otherTotal;
                                    tvTotal.setText("" + String.format("%.2f", sumTotal));

                                } else {
                                    tvRTotal.setText("0.0");
                                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                                    tvTotal.setText("" + String.format("%.2f", otherTotal));
                                }
                            } else if (billType == 2) {
                                if (selectedIdArrayOther.size() > 0) {
                                    float amt = 0;
                                    for (int i = 0; i < selectedIdArrayOther.size(); i++) {
                                        amt = amt + selectedIdArrayOther.get(i).getBillGrantAmt();
                                    }
                                    tvOTotal.setText("" + String.format("%.2f", amt));

                                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                                    float sumTotal = amt + regTotal;
                                    tvTotal.setText("" + String.format("%.2f", sumTotal));

                                } else {
                                    tvOTotal.setText("0.0");
                                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                                    tvTotal.setText("" + String.format("%.2f", regTotal));

                                }
                            }


                        } else {
                            map.put(displayedValues.get(position).getBillId(), false);
                            if (displayedValues.get(position).getIsOther() == 0) {
                                Log.e("BILL - ", "-----------" + displayedValues.get(position));
                                Log.e("SIZE BEFORE", "------------" + selectedIdArray.size());
                                selectedIdArray.remove(displayedValues.get(position));
                                Log.e("SIZE AFTER", "------------" + selectedIdArray.size());
                                //cbRegularAll.setChecked(false);


                            } else {
                                selectedIdArrayOther.remove(displayedValues.get(position));
                                //cbOtherAll.setChecked(false);
                            }

                            ArrayList<BillHeaderListData> finalArray = new ArrayList<>();
                            finalArray.addAll(selectedIdArray);
                            finalArray.addAll(selectedIdArrayOther);

                            Log.e("Bill TYPE : ", "-----------------" + billType);

                            if (billType == 1) {
                                if (selectedIdArray.size() > 0) {
                                    float amt = 0;
                                    for (int i = 0; i < selectedIdArray.size(); i++) {
                                        amt = amt + selectedIdArray.get(i).getBillGrantAmt();
                                        Log.e("GRAND AMT : ", "-------" + selectedIdArray.get(i).getBillGrantAmt());
                                    }
                                    tvRTotal.setText("" + String.format("%.2f", amt));
                                    Log.e("REGULAR : ", "-------------------------" + amt);

                                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                                    float sumTotal = amt + otherTotal;
                                    tvTotal.setText("" + String.format("%.2f", sumTotal));
                                    Log.e("TOTAL : ", "-------------------------" + sumTotal);

                                } else {
                                    tvRTotal.setText("0.0");
                                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                                    tvTotal.setText("" + String.format("%.2f", otherTotal));
                                }
                            } else if (billType == 2) {
                                if (selectedIdArrayOther.size() > 0) {
                                    float amt = 0;
                                    for (int i = 0; i < selectedIdArrayOther.size(); i++) {
                                        amt = amt + selectedIdArrayOther.get(i).getBillGrantAmt();
                                    }
                                    tvOTotal.setText("" + String.format("%.2f", amt));

                                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                                    float sumTotal = amt + regTotal;
                                    tvTotal.setText("" + String.format("%.2f", sumTotal));

                                } else {
                                    tvOTotal.setText("0.0");
                                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                                    tvTotal.setText("" + String.format("%.2f", regTotal));
                                }
                            }


                        }
                    }
                }
            });

            if (cbStatus == 1) {
                holder.cbCheck.setChecked(true);
              /*  if (displayedValues.get(position).getIsOther() == 0) {
                    selectedIdArray.add(displayedValues.get(position));
                    Log.e("CB STATUS=1", "---------SIZE : " + selectedIdArray.size());
                } else {
                    selectedIdArrayOther.add(displayedValues.get(position));
                }*/
                cbStatus = 0;


                if (billType == 1) {
                    tvRTotal.setText("" + totalSumAmt);

                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                    float sumTotal = totalSumAmt + otherTotal;
                    tvTotal.setText("" + String.format("%.2f", sumTotal));
                    Log.e("BILL TYPE 1", " : TOTAL : -----------------" + sumTotal);

                } else if (billType == 2) {
                    tvOTotal.setText("" + totalSumAmt);

                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                    float sumTotal = totalSumAmt + regTotal;
                    tvTotal.setText("" + String.format("%.2f", sumTotal));
                }


            } else if (cbStatus == 2) {
                cbStatus = 0;

                if (billType == 1) {
                    tvRTotal.setText("0");

                    float otherTotal = Float.parseFloat(tvOTotal.getText().toString());
                    tvTotal.setText("" + String.format("%.2f", otherTotal));

                } else if (billType == 2) {
                    tvOTotal.setText("0");

                    float regTotal = Float.parseFloat(tvRTotal.getText().toString());
                    tvTotal.setText("" + String.format("%.2f", regTotal));
                }

            }

            holder.tvPrintIcon.setVisibility(View.GONE);
            holder.tvApproveIcon.setVisibility(View.GONE);
            holder.tvRejectIcon.setVisibility(View.GONE);

            holder.tvName.setText("" + displayedValues.get(position).getCustName());
            holder.tvUserName.setText("" + displayedValues.get(position).getUserName());
            holder.tvInvoice.setText("Invoice : " + displayedValues.get(position).getInvoiceNo());

            String category = "";
            if (displayedValues.get(position).getCategory() == 0) {
                category = "Other Bill";
            } else if (displayedValues.get(position).getCategory() == 1) {
                category = "Employee";
            } else if (displayedValues.get(position).getCategory() == 2) {
                category = "Corporate";
            } else if (displayedValues.get(position).getCategory() == 3) {
                category = "Administrative";
            } else if (displayedValues.get(position).getCategory() == 4) {
                category = "Director";
            } else if (displayedValues.get(position).getCategory() == 5) {
                category = "VVIP";
            } else if (displayedValues.get(position).getCategory() == 6) {
                category = "Others";
            }

            holder.tvCategory.setText("" + category);

            holder.tvDate.setText("" + displayedValues.get(position).getBillDate());

            int disc = (int) displayedValues.get(position).getDiscountPer();
            holder.tvDiscount.setText("" + disc + " % OFF");

            holder.tvBillTotal.setText("" + displayedValues.get(position).getBillGrantAmt());

            ArrayList<GateSaleBillDetailList> itemArray = new ArrayList<>();

            if (displayedValues.get(position).getGateSaleBillDetailList().size() > 0) {
                for (int i = 0; i < displayedValues.get(position).getGateSaleBillDetailList().size(); i++) {
                    itemArray.add(displayedValues.get(position).getGateSaleBillDetailList().get(i));
                }
                BillItemsAdapter adapter = new BillItemsAdapter(context, itemArray, displayedValues.get(position).getDiscountPer());
                holder.lvBillItems.setAdapter(adapter);
            }

            setListViewHeightBasedOnChildren(holder.lvBillItems);

            return rowView;
        }

        public class BillItemsAdapter extends BaseAdapter {

            ArrayList<GateSaleBillDetailList> displayedValues;
            ArrayList<GateSaleBillDetailList> orderValues;
            Context context;
            private LayoutInflater inflater = null;
            float discount;

            public BillItemsAdapter(Context context, ArrayList<GateSaleBillDetailList> billItemArray, float discount) {
                this.context = context;
                this.displayedValues = billItemArray;
                this.orderValues = billItemArray;
                this.inflater = (LayoutInflater) context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.discount = discount;
            }

            @Override
            public int getCount() {
                return displayedValues.size();
            }

            @Override
            public Object getItem(int position) {
                return displayedValues.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }


            public class Holder {
                TextView tvName, tvQty, tvUnitPrice, tvSubTotal;
            }

            @Override
            public View getView(int position, final View convertView, ViewGroup parent) {
                final BillItemsAdapter.Holder holder;
                View rowView = convertView;

                if (rowView == null) {
                    holder = new BillItemsAdapter.Holder();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    rowView = inflater.inflate(R.layout.custom_pending_bill_item_layout, null);

                    holder.tvName = rowView.findViewById(R.id.tvCustomPendingBillItem_Name);
                    holder.tvQty = rowView.findViewById(R.id.tvCustomPendingBillItem_Qty);
                    holder.tvUnitPrice = rowView.findViewById(R.id.tvCustomPendingBillItem_UnitPrice);
                    holder.tvSubTotal = rowView.findViewById(R.id.tvCustomPendingBillItem_SubTotal);

                    rowView.setTag(holder);

                } else {
                    holder = (BillItemsAdapter.Holder) rowView.getTag();
                }

                holder.tvName.setText("" + displayedValues.get(position).getItemName());

                int qty = (int) displayedValues.get(position).getItemQty();
                holder.tvQty.setText("Qty : " + qty);

                float discAmt = displayedValues.get(position).getItemRate() * (discount / 100);
                float offer = displayedValues.get(position).getItemRate() - discAmt;

                holder.tvUnitPrice.setText("Unit Price : Rs. " + String.format("%.2f", offer));
                holder.tvSubTotal.setText("Sub Total : Rs. " + String.format("%.2f", (qty * offer)));


                return rowView;
            }


        }

        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null)
                return;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0)
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }


    }
}
