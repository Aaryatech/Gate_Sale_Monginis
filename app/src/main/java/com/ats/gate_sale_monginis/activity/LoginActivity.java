package com.ats.gate_sale_monginis.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fcm.SharedPrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edPassword;
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edMobile = findViewById(R.id.edLogin_Mobile);
        edPassword = findViewById(R.id.edLogin_Password);
        tvSubmit = findViewById(R.id.tvLogin_Submit);
        tvSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvLogin_Submit) {
            if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                edPassword.requestFocus();
            } else {
                String mob = edMobile.getText().toString();
                String pass = edPassword.getText().toString();
                doLogin(mob, pass);
            }
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            overridePendingTransition(R.anim.enter, R.anim.exit);
//            finish();
        }
    }


    public void doLogin(String mobile, String pass) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(LoginActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<LoginData> loginDataCall = Constants.myInterface.getLogin(mobile, pass);
            loginDataCall.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                    try {
                        if (response.body() != null) {
                            LoginData data = response.body();
                            if (data.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                Gson gson = new Gson();
                                String json = gson.toJson(data);

                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("loginData", json);
                                editor.apply();


                                String token = SharedPrefManager.getmInstance(LoginActivity.this).getDeviceToken();
                                Log.e("TOKEN : ", "------- " + token);
                                updateUserToken(data.getUserId(), token);
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Sorry, Unable To Login", Toast.LENGTH_SHORT).show();
                            //Log.e("Login : ", "Null: ");
                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Sorry, Unable To Login", Toast.LENGTH_SHORT).show();
                        //Log.e("Login : ", "Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Sorry, Unable To Login", Toast.LENGTH_SHORT).show();
                    //Log.e("Login : ", "Failure: " + t.getMessage());
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateUserToken(int id, String token) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(LoginActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.updateToken(id, token);
            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        Log.e("Login TOKEN : ", " ---- " + response.body());
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.getError()) {
                                commonDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                commonDialog.dismiss();

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }
                        } else {
                            commonDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                            //Log.e("Login : ", "Null: ");
                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        //Log.e("Login : ", "Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    //Log.e("Login : ", "Failure: " + t.getMessage());
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

}
