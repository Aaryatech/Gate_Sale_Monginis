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
import com.ats.gate_sale_monginis.adapter.UserMasterAdapter;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.bean.UserListData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class UserMasterFragment extends Fragment implements View.OnClickListener {

    private ListView lvUserList;
    private FloatingActionButton fabAddUser;

    private ArrayList<GateSaleUserList> userArray = new ArrayList<>();
    UserMasterAdapter userAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_master, container, false);
        tvTitle.setText("User Master");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        lvUserList = view.findViewById(R.id.lvUserMaster);
        fabAddUser = view.findViewById(R.id.fabUserMaster);
        fabAddUser.setOnClickListener(this);

        getUserList();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabUserMaster) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddUserFragment(), "UserMasterFragment");
            ft.commit();
        }

    }


    public void getUserList() {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<UserListData> userListDataCall = Constants.myInterface.getAllUserList();
            userListDataCall.enqueue(new Callback<UserListData>() {
                @Override
                public void onResponse(Call<UserListData> call, Response<UserListData> response) {
                    try {
                        if (response.body() != null) {
                            UserListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                userArray.clear();
                                for (int i = 0; i < data.getGateSaleUserList().size(); i++) {
                                    userArray.add(data.getGateSaleUserList().get(i));
                                }
                                userAdapter = new UserMasterAdapter(getContext(), userArray);
                                lvUserList.setAdapter(userAdapter);

                                //Log.e("User List : ", " --- " + userArray);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                            //Log.e("User : ", " NULL ");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //Log.e("User : ", " EXCEPTION : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    //Log.e("User : ", " FAILURE : " + t.getMessage());
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
