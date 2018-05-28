package com.ats.gate_sale_monginis.fragment;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.adapter.DiscountMasterAdapter;
import com.ats.gate_sale_monginis.adapter.UserMasterAdapter;
import com.ats.gate_sale_monginis.bean.DiscountListData;
import com.ats.gate_sale_monginis.bean.GateSaleDiscountList;
import com.ats.gate_sale_monginis.bean.UserListData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class DiscountMasterFragment extends Fragment implements View.OnClickListener {

    private ListView lvDiscountList;
    private FloatingActionButton fab;

    private ArrayList<GateSaleDiscountList> discountArray = new ArrayList<>();

    DiscountMasterAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount_master, container, false);
        tvTitle.setText("Discount Master");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        lvDiscountList = view.findViewById(R.id.lvDiscountMaster);
        fab = view.findViewById(R.id.fabDiscountMaster);
        fab.setOnClickListener(this);


        getDiscountList();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabDiscountMaster) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddDiscountFragment(), "DiscountMasterFragment");
            ft.commit();
        }
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
                                    if (data.getGateSaleDiscountList().get(i).getUserType() != 6) {
                                        discountArray.add(data.getGateSaleDiscountList().get(i));
                                    }
                                }
                                adapter = new DiscountMasterAdapter(getContext(), discountArray);
                                lvDiscountList.setAdapter(adapter);

                                //Log.e("discountArray List : ", " --- " + discountArray);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                            //Log.e("discountArray : ", " NULL ");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //Log.e("discountArray : ", " EXCEPTION : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<DiscountListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    //Log.e("discountArray : ", " FAILURE : " + t.getMessage());
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

}
