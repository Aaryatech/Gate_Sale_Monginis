package com.ats.gate_sale_monginis.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.adapter.SupplierMasterAdapter;
import com.ats.gate_sale_monginis.bean.OtherSupplierList;
import com.ats.gate_sale_monginis.bean.SupplierListData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class SupplierMasterFragment extends Fragment implements View.OnClickListener {

    private ListView lvList;
    private FloatingActionButton fab;

    private ArrayList<OtherSupplierList> suppList = new ArrayList<>();
    SupplierMasterAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_master, container, false);
        tvTitle.setText("Supplier Master");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        lvList = view.findViewById(R.id.lvSupplierMaster_list);
        fab = view.findViewById(R.id.fabSupplierMaster);
        fab.setOnClickListener(this);



        getSupplierList();

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fabSupplierMaster) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddSupplierFragment(), "SupplierMasterFragment");
            ft.commit();
        }
    }


    public void getSupplierList() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<SupplierListData> supplierListDataCall = Constants.myInterface.getSupplierList();
            supplierListDataCall.enqueue(new Callback<SupplierListData>() {
                @Override
                public void onResponse(Call<SupplierListData> call, Response<SupplierListData> response) {
                    try {
                        if (response.body() != null) {
                            SupplierListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No Suppliers Found", Toast.LENGTH_SHORT).show();

                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                suppList.clear();
                                for (int i = 0; i < data.getOtherSupplierList().size(); i++) {
                                    suppList.add(data.getOtherSupplierList().get(i));
                                }

                                adapter = new SupplierMasterAdapter(getContext(), suppList);
                                lvList.setAdapter(adapter);

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No Suppliers Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No Suppliers Found", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<SupplierListData> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

}
