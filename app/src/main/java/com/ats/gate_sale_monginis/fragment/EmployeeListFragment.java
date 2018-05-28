package com.ats.gate_sale_monginis.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.adapter.DashboardMenuAdapter;
import com.ats.gate_sale_monginis.adapter.EmployeeListAdapter;
import com.ats.gate_sale_monginis.bean.DiscountListData;
import com.ats.gate_sale_monginis.bean.EmployeeListData;
import com.ats.gate_sale_monginis.bean.GetGateSaleEmpList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;


public class EmployeeListFragment extends Fragment {

    private EditText edSearch;
    private ListView lvEmployeeList;

    EmployeeListAdapter adapter;

    private ArrayList<GetGateSaleEmpList> employeeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);
        tvTitle.setText("Employee List");

        edSearch = view.findViewById(R.id.edEmployeeList_Search);
        lvEmployeeList = view.findViewById(R.id.lvEmployeeList);


        //adapter = new EmployeeListAdapter(getContext(), employeeList);
//        lvEmployeeList.setAdapter(adapter);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getEmployeeList();

        return view;
    }

    public void getEmployeeList() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<EmployeeListData> employeeListDataCall = Constants.myInterface.getEmployeeList();
            employeeListDataCall.enqueue(new Callback<EmployeeListData>() {
                @Override
                public void onResponse(Call<EmployeeListData> call, Response<EmployeeListData> response) {
                    try {
                        if (response.body() != null) {
                            EmployeeListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                employeeList.clear();


                                Calendar todayDate = Calendar.getInstance();

                                for (int i = 0; i < data.getGetGateSaleEmpList().size(); i++) {

                                    String joinDate = data.getGetGateSaleEmpList().get(i).getEmpDoj();
                                    int dd = Integer.parseInt(joinDate.substring(0, 2));
                                    int mm = Integer.parseInt(joinDate.substring(3, 5));
                                    int yy = Integer.parseInt(joinDate.substring(6, 10));

                                    Calendar joiningDate = Calendar.getInstance();
                                    joiningDate.set(Calendar.DAY_OF_MONTH, dd);
                                    joiningDate.set(Calendar.MONTH, (mm-1));
                                    joiningDate.set(Calendar.YEAR, yy);


                                    int yearDiff = todayDate.get(Calendar.YEAR) - joiningDate.get(Calendar.YEAR);
                                    int monthDiff = yearDiff * 12 + todayDate.get(Calendar.MONTH) - joiningDate.get(Calendar.MONTH);
                                    Log.e("Month Difference : ", "------------------" + monthDiff);

                                    if (monthDiff >= 6) {
                                        employeeList.add(data.getGetGateSaleEmpList().get(i));
                                    }

                                }

                                adapter = new EmployeeListAdapter(getContext(), employeeList);
                                lvEmployeeList.setAdapter(adapter);

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<EmployeeListData> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

}
