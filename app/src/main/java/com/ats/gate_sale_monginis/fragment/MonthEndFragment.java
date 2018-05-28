package com.ats.gate_sale_monginis.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.Info;
import com.ats.gate_sale_monginis.bean.SettingsData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;

public class MonthEndFragment extends Fragment implements View.OnClickListener {

    private TextView tvPreviousSeries, tvSubmit;
    private EditText edNewSeries;

    ArrayList<String> settingsKeyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month_end, container, false);
        tvTitle.setText("Month End");

        tvPreviousSeries = view.findViewById(R.id.tvMonthEnd_PreviousSeries);
        tvSubmit = view.findViewById(R.id.tvMonthEnd_Submit);
        edNewSeries = view.findViewById(R.id.edMonthEnd_NewSeries);
        tvSubmit.setOnClickListener(this);


        settingsKeyList.add("gate_sale_alphabet");

        getPreviousInvoiceSeries(settingsKeyList);
        return view;
    }

    public void getPreviousInvoiceSeries(ArrayList<String> key) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<SettingsData> settingsDataCall = Constants.myInterface.getSettingsValue(key);
            settingsDataCall.enqueue(new Callback<SettingsData>() {
                @Override
                public void onResponse(Call<SettingsData> call, Response<SettingsData> response) {
                    try {
                        if (response.body() != null) {
                            SettingsData data = response.body();
                            if (data.getInfo().getError()) {
                                commonDialog.dismiss();
                                Log.e("MonthEnd : ", "----------Error : " + data.getInfo().getMessage());
                            } else {
                                commonDialog.dismiss();
                                tvPreviousSeries.setText("" + String.valueOf(Character.toChars(data.getFrItemStockConfigure().get(0).getSettingValue())));
                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("MonthEnd : ", "--------" + null);
                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("MonthEnd : ", "--------Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SettingsData> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("MonthEnd : ", "--------onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvMonthEnd_Submit) {
            String newSeries = edNewSeries.getText().toString();

            if (newSeries.isEmpty()) {
                edNewSeries.requestFocus();
            } else {
                int value = newSeries.charAt(0);
                saveMonthEnd("gate_sale_alphabet", value);
            }
        }
    }


    public void saveMonthEnd(String key, int value) {
        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.updateSettingValue(value, key);
            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.getError()) {
                                Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                edNewSeries.setText("");
                                updateInvoiceNumber("gate_sale_invoice", 1);
                            }
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateInvoiceNumber(String key, int value) {
        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.updateSettingValue(value, key);
            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.getError()) {
                                Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                getPreviousInvoiceSeries(settingsKeyList);
                                edNewSeries.setText("");
                            }
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
