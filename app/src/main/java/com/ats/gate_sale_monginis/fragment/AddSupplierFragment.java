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
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.bean.OtherSupplierList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class AddSupplierFragment extends Fragment implements View.OnClickListener {

    private EditText edName, edMob, edAddress;
    private TextView tvSubmit, tvCancel;

    int supId = 0;
    String sName, sMob, sAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_supplier, container, false);
        tvTitle.setText("Add Supplier");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        edName = view.findViewById(R.id.edAddSupplier_Name);
        edMob = view.findViewById(R.id.edAddSupplier_Mobile);
        edAddress = view.findViewById(R.id.edAddSupplier_Address);

        tvSubmit = view.findViewById(R.id.tvAddSupplier_Submit);
        tvCancel = view.findViewById(R.id.tvAddSupplier_Cancel);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        try {

            supId = getArguments().getInt("supId");
            sName = getArguments().getString("supName");
            sMob = getArguments().getString("supMobile");
            sAddress = getArguments().getString("supAddress");

            edName.setText("" + sName);
            edMob.setText("" + sMob);
            edAddress.setText("" + sAddress);


        } catch (Exception e) {
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvAddSupplier_Submit) {
            String name = edName.getText().toString();
            String mob = edMob.getText().toString();
            String address = edAddress.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Supplier Name", Toast.LENGTH_SHORT).show();
                edName.requestFocus();
            } else if (mob.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMob.requestFocus();
            } else if (mob.length() != 10) {
                Toast.makeText(getActivity(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                edMob.requestFocus();
            } else if (address.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Supplier Address", Toast.LENGTH_SHORT).show();
                edAddress.requestFocus();
            } else {

                OtherSupplierList supplier = new OtherSupplierList(supId, name, address, mob, 0);
                insertSupplier(supplier);

            }

        } else if (view.getId() == R.id.tvAddSupplier_Cancel) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SupplierMasterFragment(), "HomeFragment");
            ft.commit();
        }
    }


    public void insertSupplier(OtherSupplierList supplier) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.saveSupplier(supplier);
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
                                ft.replace(R.id.content_frame, new SupplierMasterFragment(), "HomeFragment");
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
