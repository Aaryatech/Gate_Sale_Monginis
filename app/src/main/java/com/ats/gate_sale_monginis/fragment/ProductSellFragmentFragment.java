package com.ats.gate_sale_monginis.fragment;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.adapter.DashboardMenuAdapter;
import com.ats.gate_sale_monginis.bean.DiscountListData;
import com.ats.gate_sale_monginis.bean.GateSaleDiscountList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class ProductSellFragmentFragment extends Fragment {

    private GridView gvMenu;
    DashboardMenuAdapter adapter;
    private ArrayList<GateSaleDiscountList> discountArray = new ArrayList<>();
    private ArrayList<Float> percentArray = new ArrayList<>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> iconArray = new ArrayList<>();

    int userId, userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_sell, container, false);

        tvTitle.setText("Dashboard");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        gvMenu = view.findViewById(R.id.gvMenu);

        if (HomeActivity.cartArray.size() == 0) {
            HomeActivity.llCart.setVisibility(View.INVISIBLE);
        } else {
            HomeActivity.llCart.setVisibility(View.VISIBLE);
        }

        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData userBean = gson.fromJson(json2, LoginData.class);
        //Log.e("User Bean : ", "---------------" + userBean);
        try {
            if (userBean != null) {
                userId = userBean.getUserId();
                userType = userBean.getUserType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        nameArray.add("Employee");
        nameArray.add("Corporate");
        nameArray.add("Administrative");
        nameArray.add("Directors");
        nameArray.add("VVIP");
        nameArray.add("Others");

        percentArray.add(0f);
        percentArray.add(0f);
        percentArray.add(0f);
        percentArray.add(0f);
        percentArray.add(0f);
        percentArray.add(0f);

        getDiscountList();

        return view;
    }

    public void getDiscountList() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<DiscountListData> discountListDataCall = Constants.myInterface.getDiscountList();
            discountListDataCall.enqueue(new Callback<DiscountListData>() {
                @Override
                public void onResponse(Call<DiscountListData> call, Response<DiscountListData> response) {
                    try {
                        if (response.body() != null) {
                            DiscountListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                discountArray.clear();
                                for (int i = 0; i < data.getGateSaleDiscountList().size(); i++) {
                                    discountArray.add(data.getGateSaleDiscountList().get(i));
                                    if (data.getGateSaleDiscountList().get(i).getUserType() == 1) {
                                        percentArray.set(1, data.getGateSaleDiscountList().get(i).getDiscountPer());
                                    } else if (data.getGateSaleDiscountList().get(i).getUserType() == 2) {
                                        percentArray.set(2, data.getGateSaleDiscountList().get(i).getDiscountPer());
                                    } else if (data.getGateSaleDiscountList().get(i).getUserType() == 3) {
                                        percentArray.set(3, data.getGateSaleDiscountList().get(i).getDiscountPer());
                                    } else if (data.getGateSaleDiscountList().get(i).getUserType() == 4) {
                                        percentArray.set(4, data.getGateSaleDiscountList().get(i).getDiscountPer());
                                    } else if (data.getGateSaleDiscountList().get(i).getUserType() == 5) {
                                        percentArray.set(5, data.getGateSaleDiscountList().get(i).getDiscountPer());
                                    }
                                }

                                adapter = new DashboardMenuAdapter(getContext(), nameArray, iconArray, percentArray);
                                gvMenu.setAdapter(adapter);

                                //Log.e("discountArray List : ", " --- " + discountArray);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                            //Log.e("discountArray : ", " NULL ");
                            adapter = new DashboardMenuAdapter(getContext(), nameArray, iconArray, percentArray);
                            gvMenu.setAdapter(adapter);
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //Log.e("discountArray : ", " EXCEPTION : " + e.getMessage());
                        adapter = new DashboardMenuAdapter(getContext(), nameArray, iconArray, percentArray);
                        gvMenu.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<DiscountListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    //Log.e("discountArray : ", " FAILURE : " + t.getMessage());
                    adapter = new DashboardMenuAdapter(getContext(), nameArray, iconArray, percentArray);
                    gvMenu.setAdapter(adapter);
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            adapter = new DashboardMenuAdapter(getContext(), nameArray, iconArray, percentArray);
            gvMenu.setAdapter(adapter);
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
}
