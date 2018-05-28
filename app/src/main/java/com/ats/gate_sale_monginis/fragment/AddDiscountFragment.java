package com.ats.gate_sale_monginis.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.adapter.DiscountMasterAdapter;
import com.ats.gate_sale_monginis.bean.DiscountListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleDiscountList;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class AddDiscountFragment extends Fragment implements View.OnClickListener {


    private Spinner spType;
    private EditText edName, edPercent;
    private TextView tvSubmit, tvCancel;

    int discountId = 0, delStatus = 0, catId = 0, type;
    String disHead;
    float disPercent;

    ArrayList<Integer> userTypeList = new ArrayList<>();
    ArrayList<String> userTypeArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_discount, container, false);
        tvTitle.setText("Add Discount");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        tvSubmit = view.findViewById(R.id.tvAddDiscount_Submit);
        tvCancel = view.findViewById(R.id.tvAddDiscount_Cancel);
        edName = view.findViewById(R.id.edAddDiscount_Name);
        edPercent = view.findViewById(R.id.edAddDiscount_Percent);
        spType = view.findViewById(R.id.spAddDiscount_Type);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        userTypeArray.add("Select User Type");
        userTypeArray.add("Corporate");
        userTypeArray.add("Administrative");
        userTypeArray.add("Directors");
        userTypeArray.add("VVIP");
        userTypeArray.add("Others");


        try {
            //Log.e("Discount Id : ", " -- " + getArguments().getInt("disId"));
            discountId = getArguments().getInt("disId");
            disHead = getArguments().getString("disHead");
            disPercent = getArguments().getFloat("disPer");
            delStatus = getArguments().getInt("delStatus");
            type = getArguments().getInt("userType");
            catId = getArguments().getInt("catId");

            edName.setText("" + disHead);
            edPercent.setText("" + disPercent);

        } catch (Exception e) {
            //Log.e("Add Discount : ", " Exception : " + e.getMessage());
            e.printStackTrace();
        }

        Log.e("Discount Id : ", "---------------" + discountId);
        Log.e("UserType : ", "---------------" + userTypeArray);

        if (discountId == 0) {
            getDiscountList();
        } else {
            ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, userTypeArray);
            spType.setAdapter(spAdapter);
            spType.setSelection(type);

            spType.setEnabled(false);

        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvAddDiscount_Submit) {
            if (edName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Discount title", Toast.LENGTH_SHORT).show();
                edName.requestFocus();
            } else if (spType.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select User Type", Toast.LENGTH_SHORT).show();
                spType.requestFocus();
            } else if (edPercent.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Discount Percent", Toast.LENGTH_SHORT).show();
                edPercent.requestFocus();
            } else if (Float.parseFloat(edPercent.getText().toString()) > 100) {
                Toast.makeText(getActivity(), "Please Enter Valid Discount Percent", Toast.LENGTH_SHORT).show();
                edPercent.requestFocus();
            } else {

                String title = edName.getText().toString();
                float per = Float.parseFloat(edPercent.getText().toString());
                int disType = 0;
                String uType = spType.getSelectedItem().toString();
                if (uType.equalsIgnoreCase("Corporate")) {
                    disType = 1;
                } else if (uType.equalsIgnoreCase("Administrative")) {
                    disType = 2;
                } else if (uType.equalsIgnoreCase("Directors")) {
                    disType = 3;
                } else if (uType.equalsIgnoreCase("VVIP")) {
                    disType = 4;
                } else if (uType.equalsIgnoreCase("Others")) {
                    disType = 5;
                }


                //Log.e("Bean Discount Id : ", " ------ " + discountId);
                GateSaleDiscountList bean = new GateSaleDiscountList(discountId, title, per, catId, disType, delStatus);
                insertDiscount(bean);
            }

        } else if (view.getId() == R.id.tvAddDiscount_Cancel) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DiscountMasterFragment(), "HomeFragment");
            ft.commit();
        }
    }


    public void insertDiscount(GateSaleDiscountList bean) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.insertDiscount(bean);
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
                                ft.replace(R.id.content_frame, new DiscountMasterFragment(), "HomeFragment");
                                ft.commit();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                            ////Log.e("AddUser : ", " NULL ");
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
                                userTypeList.clear();
                                for (int i = 0; i < data.getGateSaleDiscountList().size(); i++) {
                                    userTypeList.add(data.getGateSaleDiscountList().get(i).getUserType());
                                }

                                for (int i = 0; i < userTypeList.size(); i++) {
                                    if (userTypeList.get(i) == 1) {
                                        userTypeArray.remove("Corporate");
                                    } else if (userTypeList.get(i) == 2) {
                                        userTypeArray.remove("Administrative");
                                    } else if (userTypeList.get(i) == 3) {
                                        userTypeArray.remove("Directors");
                                    } else if (userTypeList.get(i) == 4) {
                                        userTypeArray.remove("VVIP");
                                    } else if (userTypeList.get(i) == 5) {
                                        userTypeArray.remove("Others");
                                    }
                                    //userTypeArray.remove(userTypeList.get(i));
                                    //Log.e("Index : ", " ---- " + userTypeList.get(i));
                                }


                                ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, userTypeArray);
                                spType.setAdapter(spAdapter);


                                //Log.e("userType List  : ", " --- " + userTypeList);
                                //Log.e("userType Array  : ", " --- " + userTypeArray);
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
}
