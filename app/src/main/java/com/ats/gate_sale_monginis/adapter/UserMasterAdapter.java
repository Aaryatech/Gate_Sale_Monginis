package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddUserFragment;
import com.ats.gate_sale_monginis.fragment.UserMasterFragment;

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

public class UserMasterAdapter extends BaseAdapter {

    Context context;
    private ArrayList<GateSaleUserList> originalValues;
    private ArrayList<GateSaleUserList> displayedValues;
    LayoutInflater inflater;

    public UserMasterAdapter(Context context, ArrayList<GateSaleUserList> catArray) {
        this.context = context;
        this.originalValues = catArray;
        this.displayedValues = catArray;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return displayedValues.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        v = inflater.inflate(R.layout.custom_usermaster_layout, null);

        TextView tvName = v.findViewById(R.id.tvCustomUserMaster_Username);
        TextView tvPass = v.findViewById(R.id.tvCustomUserMaster_Pass);
        TextView tvMobile = v.findViewById(R.id.tvCustomUserMaster_Mobile);
        TextView tvEmail = v.findViewById(R.id.tvCustomUserMaster_Email);
        TextView tvType = v.findViewById(R.id.tvCustomUserMaster_Type);
        ImageView ivPopup = v.findViewById(R.id.ivCustomUserMaster_Menu);

        tvName.setText("" + displayedValues.get(position).getUserName());
        tvPass.setText("" + displayedValues.get(position).getPassword());
        tvMobile.setText("" + displayedValues.get(position).getMobileNumber());
        tvEmail.setText("" + displayedValues.get(position).getEmailId());

        int type = displayedValues.get(position).getUserType();
        if (type == 1) {
            tvType.setText("Initiator");
        } else if (type == 2) {
            tvType.setText("Approver");
        } else if (type == 3) {
            tvType.setText("Collector");
        }

        ivPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.crud_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new AddUserFragment();
                            Bundle args = new Bundle();
                            args.putInt("UserId", displayedValues.get(position).getUserId());
                            args.putString("UserName", displayedValues.get(position).getUserName());
                            args.putString("UserPass", displayedValues.get(position).getPassword());
                            args.putString("UserMobile", displayedValues.get(position).getMobileNumber());
                            args.putString("UserEmail", displayedValues.get(position).getEmailId());
                            args.putInt("IsActive", displayedValues.get(position).getDelStatus());
                            args.putInt("UserType", displayedValues.get(position).getUserType());
                            args.putString("UserToken", displayedValues.get(position).getToken());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "UserMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do You Want To Delete User?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUserById(displayedValues.get(position).getUserId());
                                    dialog.dismiss();
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
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        return v;
    }


    public void deleteUserById(int userId) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
          commonDialog.show();



        Call<ErrorMessage> errorMessageCall = Constants.myInterface.deleteUser(userId);
        errorMessageCall.enqueue(new Callback<ErrorMessage>() {
            @Override
            public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                try {
                    if (response.body() != null) {
                        ErrorMessage message = response.body();
                        if (message.getError()) {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new UserMasterFragment();
                            Bundle args = new Bundle();
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

                        }
                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ErrorMessage> call, Throwable t) {
                commonDialog.dismiss();
                Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
