package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.bean.GetGateSaleEmpList;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddUserFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeProductsFragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIRACLEINFOTAINMENT on 29/12/17.
 */

public class EmployeeListAdapter extends BaseAdapter implements Filterable {

    ArrayList<GetGateSaleEmpList> displayedValues;
    ArrayList<GetGateSaleEmpList> originalValues;
    Context context;
    private static LayoutInflater inflater = null;


    public EmployeeListAdapter(Context context, ArrayList<GetGateSaleEmpList> empArray) {
        this.context = context;
        this.displayedValues = empArray;
        this.originalValues = empArray;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return displayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return displayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class Holder {
        TextView tvIcon, tvName, tvAmount;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        EmployeeListAdapter.Holder holder = new EmployeeListAdapter.Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_employee_list_layout, null);
        holder.tvIcon = rowView.findViewById(R.id.tvCustomEmployeeList_Icon);
        holder.tvName = rowView.findViewById(R.id.tvCustomEmployeeList_Name);
        holder.tvAmount = rowView.findViewById(R.id.tvCustomEmployeeList_Money);

        holder.tvName.setText("" + displayedValues.get(position).getEmpName());

        holder.tvAmount.setText("" + displayedValues.get(position).getMonthlyConsumed());


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = context.getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("categoryId", (1));
                editor.putString("category", displayedValues.get(position).getEmpName());
                editor.putInt("eId", displayedValues.get(position).getEmpId());
                editor.putInt("monthlyLimit", displayedValues.get(position).getMonthlyLimit());
                editor.putInt("yearlyLimit", displayedValues.get(position).getYearlyLimit());
                editor.putFloat("catDiscount", displayedValues.get(position).getDiscountPer());
                editor.putInt("monthlyConsumed", displayedValues.get(position).getMonthlyConsumed());
                editor.putInt("yearlyConsumed", displayedValues.get(position).getYearlyConsumed());
                editor.putString("empDOB", displayedValues.get(position).getEmpDob());
                editor.apply();

                HomeActivity activity = (HomeActivity) context;
                Fragment adf = new EmployeeProductsFragment();
                Bundle args = new Bundle();
                args.putInt("empId", displayedValues.get(position).getEmpId());
                args.putString("empName", displayedValues.get(position).getEmpName());
                args.putInt("empMonthlyLimit", displayedValues.get(position).getMonthlyLimit());
                args.putInt("empYearlyLimit", displayedValues.get(position).getYearlyLimit());
                args.putFloat("empDiscount", displayedValues.get(position).getDiscountPer());
                args.putInt("empMonthlyConsumed", displayedValues.get(position).getMonthlyConsumed());
                args.putInt("empYearlyConsumed", displayedValues.get(position).getYearlyConsumed());
                args.putString("empDOB", displayedValues.get(position).getEmpDob());
                adf.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

            }
        });


        return rowView;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<GetGateSaleEmpList> filteredArrayList = new ArrayList<GetGateSaleEmpList>();

                if (originalValues == null) {
                    originalValues = new ArrayList<GetGateSaleEmpList>(displayedValues);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String name = originalValues.get(i).getEmpName();
                        String dept = originalValues.get(i).getDeptName();
                        if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString()) || dept.toLowerCase().startsWith(charSequence.toString()) || dept.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new GetGateSaleEmpList(originalValues.get(i).getEmpId(), originalValues.get(i).getEmpName(), originalValues.get(i).getDeptId(), originalValues.get(i).getDeptName(), originalValues.get(i).getIsUsed(), originalValues.get(i).getEmpType(), originalValues.get(i).getEmpMobile(), originalValues.get(i).getEmpDob(), originalValues.get(i).getEmpDoj(), originalValues.get(i).getEmpFamMemb(), originalValues.get(i).getDiscId(), originalValues.get(i).getDiscountHead(), originalValues.get(i).getDiscountPer(), originalValues.get(i).getMonthlyLimit(), originalValues.get(i).getYearlyLimit(), originalValues.get(i).getDelStatus(), originalValues.get(i).getMonthlyConsumed(), originalValues.get(i).getYearlyConsumed()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedValues = (ArrayList<GetGateSaleEmpList>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


}

