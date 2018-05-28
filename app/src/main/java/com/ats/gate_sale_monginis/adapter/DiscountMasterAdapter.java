package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.ats.gate_sale_monginis.bean.GateSaleDiscountList;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddDiscountFragment;
import com.ats.gate_sale_monginis.fragment.AddUserFragment;
import com.ats.gate_sale_monginis.fragment.DiscountMasterFragment;
import com.ats.gate_sale_monginis.fragment.UserMasterFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class DiscountMasterAdapter extends BaseAdapter {


    Context context;
    private ArrayList<GateSaleDiscountList> originalValues;
    private ArrayList<GateSaleDiscountList> displayedValues;
    LayoutInflater inflater;

    public DiscountMasterAdapter(Context context, ArrayList<GateSaleDiscountList> catArray) {
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
        v = inflater.inflate(R.layout.custom_discountmaster_layout, null);

        TextView tvName = v.findViewById(R.id.tvCustomDisMaster_Head);
        TextView tvType = v.findViewById(R.id.tvCustomDisMaster_Type);
        TextView tvPercent = v.findViewById(R.id.tvCustomDisMaster_Percent);
        ImageView ivPopup = v.findViewById(R.id.ivCustomDisMaster_Menu);

        tvName.setText("" + displayedValues.get(position).getDiscountHead());
        tvPercent.setText("Discount % : " + displayedValues.get(position).getDiscountPer());

        int type = displayedValues.get(position).getUserType();
        if (type == 1) {
            tvType.setText("Corporate");
        } else if (type == 2) {
            tvType.setText("Administrative");
        } else if (type == 3) {
            tvType.setText("Directors");
        } else if (type == 4) {
            tvType.setText("VVIP");
        } else if (type == 5) {
            tvType.setText("Others");
        }

        ivPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.crud_menu, popupMenu.getMenu());

                MenuItem item=popupMenu.getMenu().findItem(R.id.action_delete);
                item.setVisible(false);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new AddDiscountFragment();
                            Bundle args = new Bundle();
                            args.putInt("disId", displayedValues.get(position).getDiscountId());
                            args.putString("disHead", displayedValues.get(position).getDiscountHead());
                            args.putFloat("disPer", displayedValues.get(position).getDiscountPer());
                            args.putInt("delStatus", displayedValues.get(position).getDelStatus());
                            args.putInt("userType", displayedValues.get(position).getUserType());
                            args.putInt("catId", displayedValues.get(position).getCatId());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "DiscountMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do You Want To Delete Discount?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteById(displayedValues.get(position).getDiscountId());
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


    public void deleteById(int disId) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();


        Call<ErrorMessage> errorMessageCall = Constants.myInterface.deleteDiscount(disId);
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
                            Fragment adf = new DiscountMasterFragment();
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
