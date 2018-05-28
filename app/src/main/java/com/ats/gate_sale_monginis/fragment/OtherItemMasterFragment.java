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
import com.ats.gate_sale_monginis.adapter.OtherItemMasterAdapter;
import com.ats.gate_sale_monginis.bean.OtherItemList;
import com.ats.gate_sale_monginis.bean.OtherItemListData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class OtherItemMasterFragment extends Fragment implements View.OnClickListener {

    private ListView lvList;
    private FloatingActionButton fab;

    OtherItemMasterAdapter adapter;
    ArrayList<OtherItemList> itemArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_item_master, container, false);
        tvTitle.setText("Other Item Master");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        lvList = view.findViewById(R.id.lvOtherItemMaster_list);
        fab = view.findViewById(R.id.fabOtherItemMaster);
        fab.setOnClickListener(this);

        getItemList();
        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fabOtherItemMaster) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new AddOtherItemFragment(), "OtherItemMasterFragment");
            ft.commit();
        }
    }


    public void getItemList() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<OtherItemListData> otherItemListDataCall = Constants.myInterface.getOtherItemList(1);
            otherItemListDataCall.enqueue(new Callback<OtherItemListData>() {
                @Override
                public void onResponse(Call<OtherItemListData> call, Response<OtherItemListData> response) {
                    try {
                        if (response.body() != null) {
                            OtherItemListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No Item Found", Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                itemArray.clear();
                                for (int i = 0; i < data.getOtherItemList().size(); i++) {
                                    itemArray.add(data.getOtherItemList().get(i));
                                }

                                adapter = new OtherItemMasterAdapter(getContext(), itemArray);
                                lvList.setAdapter(adapter);

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No Item Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No Item Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OtherItemListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No Item Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

}
