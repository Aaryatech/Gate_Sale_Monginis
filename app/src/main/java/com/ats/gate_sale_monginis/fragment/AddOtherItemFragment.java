package com.ats.gate_sale_monginis.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.OtherItemList;
import com.ats.gate_sale_monginis.bean.OtherSupplierList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class AddOtherItemFragment extends Fragment implements View.OnClickListener {

    private EditText edName, edRate;
    private TextView tvSubmit, tvCancel;

    int itemId = 0;
    String iName, iRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_other_item, container, false);
        tvTitle.setText("Add Other Item");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        edName = view.findViewById(R.id.edAddOtherItem_Name);
        edRate = view.findViewById(R.id.edAddOtherItem_Rate);

        tvSubmit = view.findViewById(R.id.tvAddOtherItem_Submit);
        tvCancel = view.findViewById(R.id.tvAddOtherItem_Cancel);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        try {

            itemId = getArguments().getInt("itemId");
            iName = getArguments().getString("itemName");
            iRate = getArguments().getString("itemRate");

            edName.setText("" + iName);
            edRate.setText("" + iRate);

        } catch (Exception e) {
        }


        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tvAddOtherItem_Submit) {
            String name = edName.getText().toString();
            String rate = edRate.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Item Name", Toast.LENGTH_SHORT).show();
                edName.requestFocus();
            } else if (rate.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Item Rate", Toast.LENGTH_SHORT).show();
                edRate.requestFocus();
            } else {

                Float amt = 0f;
                try {
                    amt = Float.parseFloat(rate);
                } catch (Exception e) {
                }

                OtherItemList bean = new OtherItemList(itemId, 1, name, "", amt, 0);
                insertItem(bean);

            }
        } else if (view.getId() == R.id.tvAddOtherItem_Submit) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new OtherItemMasterFragment(), "HomeFragment");
            ft.commit();
        }

    }


    public void insertItem(OtherItemList item) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.saveOtherItem(item);
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
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new OtherItemMasterFragment(), "HomeFragment");
                                ft.commit();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                            //Log.e("AddUser : ", " NULL ");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        //Log.e("AddUser : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
