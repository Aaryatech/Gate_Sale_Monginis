package com.ats.gate_sale_monginis.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class AddUserFragment extends Fragment implements View.OnClickListener {


    private TextView tvSubmit, tvCancel;
    private RadioButton rbInitiator, rbApprover, rbCollector;
    private EditText edName, edMobile, edPass, edConfirmPass, edEmail;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Pattern pattern;
    private Matcher matcher;

    int uId = 0, isActive = 0, uType;
    String uName, uMobile, uEmail, uPass, uToken = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        tvTitle.setText("Add User");
        tvTitle.setTextColor(Color.parseColor("#000000"));

        tvCancel = view.findViewById(R.id.tvAddUser_Cancel);
        tvSubmit = view.findViewById(R.id.tvAddUser_Submit);
        rbApprover = view.findViewById(R.id.rbAddUser_Approver);
        rbInitiator = view.findViewById(R.id.rbAddUser_Initiator);
        rbCollector = view.findViewById(R.id.rbAddUser_Collector);
        edName = view.findViewById(R.id.edAddUser_Name);
        edMobile = view.findViewById(R.id.edAddUser_Mobile);
        edPass = view.findViewById(R.id.edAddUser_Password);
        edEmail = view.findViewById(R.id.edAddUser_Email);
        edConfirmPass = view.findViewById(R.id.edAddUser_ConfirmPassword);

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        try {
            uId = getArguments().getInt("UserId");
            uName = getArguments().getString("UserName");
            uPass = getArguments().getString("UserPass");
            uMobile = getArguments().getString("UserMobile");
            uEmail = getArguments().getString("UserEmail");
            isActive = getArguments().getInt("IsActive");
            uType = getArguments().getInt("UserType");
            uToken = getArguments().getString("UserToken");

            edName.setText("" + uName);
            edPass.setText("" + uPass);
            edConfirmPass.setText("" + uPass);
            edMobile.setText("" + uMobile);
            edEmail.setText("" + uEmail);

            if (uType == 1) {
                rbInitiator.setChecked(true);
            } else if (uType == 2) {
                rbApprover.setChecked(true);
            } else if (uType == 3) {
                rbCollector.setChecked(true);
            }

        } catch (Exception e) {
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvAddUser_Submit) {
            if (!rbInitiator.isChecked() && !rbApprover.isChecked() && !rbCollector.isChecked()) {
                Toast.makeText(getActivity(), "Please Select User Type", Toast.LENGTH_SHORT).show();
                rbInitiator.requestFocus();
            } else if (edName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter User Name", Toast.LENGTH_SHORT).show();
                edName.requestFocus();
            } else if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                edEmail.requestFocus();
            } else if (edPass.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                edPass.requestFocus();
            } else if (edConfirmPass.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Confirm Enter Password", Toast.LENGTH_SHORT).show();
                edConfirmPass.requestFocus();
            } else if (!edPass.getText().toString().equalsIgnoreCase(edConfirmPass.getText().toString())) {
                Toast.makeText(getActivity(), "Password Not Matched", Toast.LENGTH_SHORT).show();
                edConfirmPass.requestFocus();
            } else {
                String name = edName.getText().toString();
                String mobile = edMobile.getText().toString();
                String pass = edPass.getText().toString();
                String mail = edEmail.getText().toString();
                int type = 0;
                if (rbInitiator.isChecked()) {
                    type = 1;
                } else if (rbApprover.isChecked()) {
                    type = 2;
                } else if (rbCollector.isChecked()) {
                    type = 3;
                }

                GateSaleUserList bean = new GateSaleUserList(uId, name, type, mobile, mail, pass, 1, "", 0, uToken);
                insertUser(bean);

            }
        } else if (view.getId() == R.id.tvAddUser_Cancel) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
            ft.commit();
        }
    }

    public void insertUser(GateSaleUserList mUser) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.insertUser(mUser);
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
                                ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
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
