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
import android.widget.ImageView;
import android.widget.TextView;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.EmployeeListFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeProductsFragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIRACLEINFOTAINMENT on 29/12/17.
 */

public class DashboardMenuAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<String> nameArray;
    ArrayList<String> iconArray;
    ArrayList<Float> discountArray;
    private static LayoutInflater inflater = null;

    public DashboardMenuAdapter(Context c, ArrayList<String> nameArray, ArrayList<String> iconArray, ArrayList<Float> discArray) {
        this.mContext = c;
        this.nameArray = nameArray;
        this.iconArray = iconArray;
        this.discountArray = discArray;
        this.inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nameArray.size();
    }

    @Override
    public Object getItem(int position) {
        return nameArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvName, tvDiscount;
        ImageView ivIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_dashboard_menu_layout, null);
        holder.ivIcon = rowView.findViewById(R.id.tvMenuIcon);
        holder.tvName = rowView.findViewById(R.id.tvMenuName);
        holder.tvDiscount = rowView.findViewById(R.id.tvMenuDisc);

        if (position == 0) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_employee));
        } else if (position == 1) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_corporate));
        } else if (position == 2) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_admin));
        } else if (position == 3) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_director));
        } else if (position == 4) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_siren));
        } else if (position == 5) {
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_otheremp));
        }

        holder.tvName.setText("" + nameArray.get(position));

        if (discountArray.get(position) == 0) {
            holder.tvDiscount.setText("");
        } else {
            holder.tvDiscount.setText("" + discountArray.get(position) + " %");
        }


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {

                    HomeActivity activity = (HomeActivity) mContext;
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new EmployeeListFragment(), "HomeFragment");
                    ft.commit();

                } else {

                    SharedPreferences pref = mContext.getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("category", nameArray.get(position));
                    editor.putInt("categoryId", (position + 1));
                    editor.putInt("eId", 0);
                    editor.putInt("monthlyLimit", 0);
                    editor.putInt("yearlyLimit", 0);
                    editor.putFloat("catDiscount", discountArray.get(position));
                    editor.apply();


                    HomeActivity activity = (HomeActivity) mContext;
                    Fragment adf = new EmployeeProductsFragment();
                    Bundle args = new Bundle();
                    args.putInt("empId", 0);
                    args.putString("empName", nameArray.get(position));
                    args.putInt("empMonthlyLimit", 0);
                    args.putInt("empYearlyLimit", 0);
                    args.putFloat("empDiscount", discountArray.get(position));
                    adf.setArguments(args);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

                }
            }
        });

        return rowView;
    }

}
