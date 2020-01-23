package com.ats.gate_sale_monginis.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.adapter.PendingBillListAdapter;
import com.ats.gate_sale_monginis.adapter.RejectedBillListAdapter;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.interfaces.RejectedBillInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RejectedBillsFragment extends Fragment implements RejectedBillInterface {

    private ListView lvList;
    private CheckBox checkBox;
    private Button btnDelete;

    RejectedBillListAdapter adapter;
    PendingBillListAdapter pendingAdapter;

    private ArrayList<BillHeaderListData> billData = new ArrayList<>();

    int userId, userType;

    int yyyy, mm, dd;
    long fromDateMillis, toDateMillis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rejected_bills, container, false);

        lvList = view.findViewById(R.id.lvRejectedList);
        checkBox = view.findViewById(R.id.checkbox);
        btnDelete = view.findViewById(R.id.btnDelete);

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

        //Log.e("User Id : " + userId, "\nUser Type : " + userType);

        SimpleDateFormat sdf_From = new SimpleDateFormat("yyyy-MM-dd");

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        final String fromDate = sdf_From.format(yesterday.getTimeInMillis());

        final String toDate = sdf_From.format(Calendar.getInstance().getTimeInMillis());

        if (userType == 1) {
            getInitiatorBillList(fromDate, toDate, 3, userId);
        } else if (userType == 2) {
            getBillList(fromDate, toDate, 3, userId, 0, 0);
        } else {
            getBillList(fromDate, toDate, 3, 0, 0, 0);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (billData.size() > 0) {
                        for (int i = 0; i < billData.size(); i++) {
                            billData.get(i).setRejected(true);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (billData.size() > 0) {
                    ArrayList<Integer> idArray = new ArrayList<>();

                    for (int i = 0; i < billData.size(); i++) {
                        if (billData.get(i).isRejected()) {
                            idArray.add(billData.get(i).getBillId());
                        }
                    }

                    if (idArray.size() == 0) {
                        Toast.makeText(getContext(), "Please select bills to delete", Toast.LENGTH_SHORT).show();
                    } else {
                        deleteRejectedBill(idArray, fromDate, toDate);
                    }

                    Log.e("Bill ID Array : ", "-----------------" + idArray);
                    Log.e("Bill Array Size : ", "----------------" + billData.size());
                    Log.e("Bill ID Array Size : ", "----------------" + idArray.size());
                } else {
                    Toast.makeText(getContext(), "No rejected bills found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    public void getBillList(String fromDate, String toDate, int isApproved, int approvedBy, int isAmtCollected, int collectedBy) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<BillHeaderListData>> arrayListCall = Constants.myInterface.getBillData(fromDate, toDate, isApproved, approvedBy, isAmtCollected, collectedBy);
            arrayListCall.enqueue(new Callback<ArrayList<BillHeaderListData>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderListData>> call, Response<ArrayList<BillHeaderListData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<BillHeaderListData> data = response.body();

                            commonDialog.dismiss();
                            billData.clear();
                            billData = data;

                            for (int i = 0; i < billData.size(); i++) {
                                billData.get(i).setRejected(false);
                            }

                            if (userType == 3) {
                                adapter = new RejectedBillListAdapter(getContext(), billData, 3);
                                lvList.setAdapter(adapter);
                            } else {
                                pendingAdapter = new PendingBillListAdapter(getContext(), billData, 3);
                                lvList.setAdapter(pendingAdapter);
                            }

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
                public void onFailure(Call<ArrayList<BillHeaderListData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getInitiatorBillList(String fromDate, String toDate, int isApproved, int approvedBy) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<BillHeaderListData>> arrayListCall = Constants.myInterface.getInitiatorBillData(fromDate, toDate, isApproved, approvedBy);
            arrayListCall.enqueue(new Callback<ArrayList<BillHeaderListData>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderListData>> call, Response<ArrayList<BillHeaderListData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<BillHeaderListData> data = response.body();

                            commonDialog.dismiss();
                            billData.clear();
                            billData = data;

                            pendingAdapter = new PendingBillListAdapter(getContext(), billData, 3);
                            lvList.setAdapter(pendingAdapter);

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
                public void onFailure(Call<ArrayList<BillHeaderListData>> call, Throwable t) {
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
    public void fragmentBecameVisible() {
        //Log.e("User Id : " + userId, "\nUser Type : " + userType);

        SimpleDateFormat sdf_From = new SimpleDateFormat("yyyy-MM-dd");

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        String fromDate = sdf_From.format(yesterday.getTimeInMillis());

        String toDate = sdf_From.format(Calendar.getInstance().getTimeInMillis());

        if (userType == 1) {
            getInitiatorBillList(fromDate, toDate, 3, userId);
        } else if (userType == 2) {
            getBillList(fromDate, toDate, 3, userId, 0, 0);
        } else {
            getBillList(fromDate, toDate, 3, 0, 0, 0);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_Filter);
        item.setVisible(true);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Filter:
                new showDateDialog(getContext()).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class showDateDialog extends Dialog {

        EditText edFromDate, edToDate;
        TextView tvFromDate, tvToDate;

        public showDateDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            setTitle("Filter");
            setContentView(R.layout.custom_date_picker_dialog_layout);

            Window window = getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.dimAmount = 0.75f;
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 100;
            wlp.y = 100;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);

            edFromDate = findViewById(R.id.edDatePicker_FromDate);
            edToDate = findViewById(R.id.edDatePicker_ToDate);
            TextView tvDialogSearch = findViewById(R.id.tvDatePicker_Search);
            TextView tvDialogCancel = findViewById(R.id.tvDatePicker_Cancel);
            tvFromDate = findViewById(R.id.tvDatePicker_FromDate);
            tvToDate = findViewById(R.id.tvDatePicker_ToDate);

            edFromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (fromDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(fromDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), fromDateListener, yr, mn, dy);
                    dialog.show();
                }
            });

            edToDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int yr, mn, dy;
                    if (toDateMillis > 0) {
                        Calendar purchaseCal = Calendar.getInstance();
                        purchaseCal.setTimeInMillis(toDateMillis);
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    } else {
                        Calendar purchaseCal = Calendar.getInstance();
                        yr = purchaseCal.get(Calendar.YEAR);
                        mn = purchaseCal.get(Calendar.MONTH);
                        dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), toDateListener, yr, mn, dy);
                    dialog.show();
                }
            });


            tvDialogSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edFromDate.getText().toString().isEmpty()) {
                        edFromDate.setError("Select From Date");
                        edFromDate.requestFocus();
                    } else if (edToDate.getText().toString().isEmpty()) {
                        edToDate.setError("Select To Date");
                        edToDate.requestFocus();
                    } else {
                        dismiss();

                        String fromDate = tvFromDate.getText().toString();
                        String toDate = tvToDate.getText().toString();

                        if (userType == 1) {
                            getInitiatorBillList(fromDate, toDate, 3, userId);
                        } else if (userType == 2) {
                            getBillList(fromDate, toDate, 3, userId, 0, 0);
                        } else {
                            getBillList(fromDate, toDate, 3, 0, 0, 0);
                        }
                    }
                }
            });

            tvDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edFromDate.setText(dd + "-" + mm + "-" + yyyy);
                tvFromDate.setText(yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                fromDateMillis = calendar.getTimeInMillis();
            }
        };

        DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yyyy = year;
                mm = month + 1;
                dd = dayOfMonth;
                edToDate.setText(dd + "-" + mm + "-" + yyyy);
                tvToDate.setText(yyyy + "-" + mm + "-" + dd);

                Calendar calendar = Calendar.getInstance();
                calendar.set(yyyy, mm - 1, dd);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR, 0);
                toDateMillis = calendar.getTimeInMillis();
            }
        };
    }


    public void deleteRejectedBill(ArrayList<Integer> billIdArray, final String fromDate, final String toDate) {
        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ErrorMessage> messageCall = Constants.myInterface.deleteRejectedBill(billIdArray);
            messageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();

                            if (data.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                getBillList(fromDate, toDate, 3, 0, 0, 0);
                                checkBox.setChecked(false);
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
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
