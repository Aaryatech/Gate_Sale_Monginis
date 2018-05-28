package com.ats.gate_sale_monginis.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.adapter.BirthdayItemAdapter;
import com.ats.gate_sale_monginis.adapter.EmployeeListAdapter;
import com.ats.gate_sale_monginis.adapter.EmployeeProductsAdapter;
import com.ats.gate_sale_monginis.bean.EmployeeListData;
import com.ats.gate_sale_monginis.bean.Item;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvCartCount;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;


public class EmployeeProductsFragment extends Fragment implements View.OnClickListener {

    private GridView gvProducts;
    private LinearLayout llCakes, llPackProduct, llSavouries, llTradingMaterial, llMoneyLimit, llMoneyUsed;

    private TextView tvDiscount, tvMonthly, tvYearly, tvEmpName, tvPackItem, tvCake, tvSavouries, tvTradingMaterial, tvMonthlyUsed, tvYearlyUsed, tvCakeIcon, tvSavouriesIcon, tvPackProdIcon, tvTradingMatIcon, tvBirthdayIcon;
    private EditText edSearch;

    EmployeeProductsAdapter adapter;
    private ArrayList<Item> itemArray = new ArrayList<>();

    int empId, monthlyLimit, yearlyLimit, catId, monthlyConsumed, yearlyConsumed;
    float percentDiscount;
    String empName, dob;

    private ArrayList<Item> birthdayItemArray = new ArrayList<>();
    BirthdayItemAdapter birthdayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_products, container, false);
        tvTitle.setText("Cakes & Pastries");


        try {
            SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            catId = pref.getInt("categoryId", 0);
            empId = pref.getInt("eId", 0);
            empName = pref.getString("category", "");
            percentDiscount = pref.getFloat("catDiscount", 0);
            monthlyLimit = pref.getInt("monthlyLimit", 0);
            yearlyLimit = pref.getInt("yearlyLimit", 0);
            monthlyConsumed = pref.getInt("monthlyConsumed", 0);
            yearlyConsumed = pref.getInt("yearlyConsumed", 0);
            dob = pref.getString("empDOB", "");

        } catch (Exception e) {
        }


        llMoneyLimit = view.findViewById(R.id.llProduct_MoneyLimit);
        gvProducts = view.findViewById(R.id.gvProducts);
        llCakes = view.findViewById(R.id.llEmpProducts_Cakes);
        llPackProduct = view.findViewById(R.id.llEmpProducts_PackProduct);
        llSavouries = view.findViewById(R.id.llEmpProducts_Savouries);
        tvDiscount = view.findViewById(R.id.tvProduct_Discount);
        tvMonthly = view.findViewById(R.id.tvProduct_Monthly);
        tvYearly = view.findViewById(R.id.tvProduct_Yearly);
        tvEmpName = view.findViewById(R.id.tvProduct_EmpName);
        edSearch = view.findViewById(R.id.edProduct_Search);
        tvCake = view.findViewById(R.id.tvProduct_Cake);
        tvSavouries = view.findViewById(R.id.tvProduct_Savouries);
        tvPackItem = view.findViewById(R.id.tvProduct_PackItem);
        llMoneyUsed = view.findViewById(R.id.llProduct_MoneyUsed);
        tvMonthlyUsed = view.findViewById(R.id.tvProduct_MonthlyUsed);
        tvYearlyUsed = view.findViewById(R.id.tvProduct_YearlyUsed);
        tvCakeIcon = view.findViewById(R.id.tvProduct_CakeIcon);
        tvSavouriesIcon = view.findViewById(R.id.tvProduct_SavouriesIcon);
        tvPackProdIcon = view.findViewById(R.id.tvProduct_PackItemIcon);
        llTradingMaterial = view.findViewById(R.id.llEmpProducts_TradingMaterial);
        tvTradingMaterial = view.findViewById(R.id.tvProduct_TradingMaterial);
        tvTradingMatIcon = view.findViewById(R.id.tvProduct_TradingMatIcon);
        tvBirthdayIcon = view.findViewById(R.id.tvIcon_Birthday);

        llCakes.setOnClickListener(this);
        llPackProduct.setOnClickListener(this);
        llSavouries.setOnClickListener(this);
        llTradingMaterial.setOnClickListener(this);
        tvBirthdayIcon.setOnClickListener(this);

        tvDiscount.setText("" + percentDiscount + " %");
        tvEmpName.setText("" + empName);

        if (catId != 1) {
            llMoneyLimit.setVisibility(View.GONE);
            llMoneyUsed.setVisibility(View.GONE);
            tvBirthdayIcon.setVisibility(View.GONE);

        } else {
            llMoneyLimit.setVisibility(View.VISIBLE);
            llMoneyUsed.setVisibility(View.VISIBLE);
            tvYearly.setText("Yearly Limit: Rs. " + yearlyLimit);
            tvMonthly.setText("Monthly Limit: Rs. " + monthlyLimit);
            tvYearlyUsed.setText("Used: Rs. " + yearlyConsumed);
            tvMonthlyUsed.setText("Used: Rs. " + monthlyConsumed);

            try {
                Log.e("DOB : ", "-------------" + dob);

                int dd = Integer.parseInt(dob.substring(0, 2));
                int mm = Integer.parseInt(dob.substring(3, 5));
                int yy = Integer.parseInt(dob.substring(6, 10));

                Calendar calDOB = Calendar.getInstance();
                calDOB.set(Calendar.DAY_OF_MONTH, dd);
                calDOB.set(Calendar.MONTH, (mm - 1));
                calDOB.set(Calendar.YEAR, yy);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Log.e("DOB   DATE----", "" + sdf.format(calDOB.getTimeInMillis()));

                Calendar today = Calendar.getInstance();

                Log.e("dob : dd", "-----------" + dd);
                Log.e("dob : mm", "-----------" + mm);

                Log.e("today : dd", "-----------" + today.get(Calendar.DAY_OF_MONTH));
                Log.e("today : mm", "-----------" + today.get(Calendar.MONTH));

                if (calDOB.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH) && calDOB.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                    tvBirthdayIcon.setVisibility(View.VISIBLE);
                } else {
                    tvBirthdayIcon.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Log.e("Exception : DOB", "-----------------" + e.getMessage());
                e.printStackTrace();
            }


        }

        tvEmpName.setText("" + empName);
        tvDiscount.setText("Discount : " + percentDiscount + " %");
        tvMonthly.setText("Monthly Limit : Rs. " + monthlyLimit);
        tvYearly.setText("Yearly Limit : Rs. " + yearlyLimit);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        tvCake.setTextColor(getResources().getColor(R.color.colorAccent));
        tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
        tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
        tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
        tvCakeIcon.setTextColor(getResources().getColor(R.color.colorAccent));
        tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));

        getItemList(2);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llEmpProducts_Cakes) {
            tvTitle.setText("Cakes & Pastries");
            getItemList(2);
            tvCake.setTextColor(getResources().getColor(R.color.colorAccent));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));

        } else if (view.getId() == R.id.llEmpProducts_Savouries) {
            tvTitle.setText("Savouries");
            getItemList(1);
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (view.getId() == R.id.llEmpProducts_PackProduct) {
            tvTitle.setText("Packed Products");
            getItemList(4);
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorAccent));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorAccent));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorBlack));
        } else if (view.getId() == R.id.llEmpProducts_TradingMaterial) {
            tvTitle.setText("Trading Materials");
            getItemList(3);
            tvCake.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouries.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackItem.setTextColor(getResources().getColor(R.color.colorBlack));
            tvCakeIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvSavouriesIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvPackProdIcon.setTextColor(getResources().getColor(R.color.colorBlack));
            tvTradingMaterial.setTextColor(getResources().getColor(R.color.colorAccent));
            tvTradingMatIcon.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (view.getId() == R.id.tvIcon_Birthday) {

            if (birthdayItemArray.size() > 0) {
                new showBirthdayDialog(getContext()).show();
            } else {
                Toast.makeText(getActivity(), "Please Wait...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getItemList(int catId) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<Item>> arrayListCall = Constants.myInterface.getItemsByCategory(catId);
            arrayListCall.enqueue(new Callback<ArrayList<Item>>() {
                @Override
                public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<Item> data = response.body();

                            commonDialog.dismiss();
                            itemArray.clear();
                            itemArray = data;

                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setQty(1);
                            }

                            adapter = new EmployeeProductsAdapter(getContext(), itemArray);
                            gvProducts.setAdapter(adapter);


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
                public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
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
    public void onResume() {
        super.onResume();

        new showBirthdayDialog(getContext()).getBirthdayItemList();

        llCart.setVisibility(View.VISIBLE);
        if (cartArray.size() > 0) {
            int totalQty = 0;
            for (int i = 0; i < cartArray.size(); i++) {

                totalQty = totalQty + cartArray.get(i).getQuantity();
            }
            HomeActivity.tvCartCount.setText("" + totalQty);
        } else {
            tvCartCount.setText("0");
        }

    }


    public class showBirthdayDialog extends Dialog {

        GridView gvBirthdayItem;
        EditText edBirthdayItemSearch;


        public showBirthdayDialog(@NonNull Context context) {
            super(context);
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);

            setTitle("Birthday Special Products");
            setContentView(R.layout.custom_birthday_dialog);


            Window window = this.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;
            wlp.x = 100;
            wlp.y = 100;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            gvBirthdayItem = findViewById(R.id.gvBirthdayItem);
            edBirthdayItemSearch = findViewById(R.id.edBirthdayItem_Search);


            birthdayAdapter = new BirthdayItemAdapter(EmployeeProductsFragment.this.getContext(), showBirthdayDialog.this, birthdayItemArray);
            gvBirthdayItem.setAdapter(birthdayAdapter);
            //getBirthdayItemList();

            edBirthdayItemSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (birthdayAdapter != null) {
                        birthdayAdapter.getFilter().filter(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }


        public void getBirthdayItemList() {

            if (Constants.isOnline(getContext())) {

//                final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
//                commonDialog.show();


                Call<ArrayList<Item>> arrayListCall = Constants.myInterface.getItemsByBirthday();
                arrayListCall.enqueue(new Callback<ArrayList<Item>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                        try {
                            if (response.body() != null) {
                                ArrayList<Item> data = response.body();

                                // commonDialog.dismiss();
                                birthdayItemArray.clear();
                                birthdayItemArray = data;

//                                birthdayAdapter = new BirthdayItemAdapter(EmployeeProductsFragment.this.getContext(), showBirthdayDialog.this, birthdayItemArray);
//                                gvBirthdayItem.setAdapter(birthdayAdapter);


                            } else {
                                //  commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No Products Found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            // commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No Products Found", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                        //  commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No Products Found", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();

                    }
                });
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
