package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GetGateSaleEmpList;
import com.ats.gate_sale_monginis.bean.OtherSupplierList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddSupplierFragment;
import com.ats.gate_sale_monginis.fragment.AddUserFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeProductsFragment;
import com.ats.gate_sale_monginis.fragment.SupplierMasterFragment;
import com.ats.gate_sale_monginis.fragment.UserMasterFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MAXADMIN on 7/2/2018.
 */

public class SupplierMasterAdapter extends BaseAdapter implements Filterable {


    ArrayList<OtherSupplierList> displayedValues;
    ArrayList<OtherSupplierList> originalValues;
    Context context;
    private static LayoutInflater inflater = null;


    public SupplierMasterAdapter(Context context, ArrayList<OtherSupplierList> empArray) {
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
        TextView tvMob, tvName, tvAddress;
        ImageView ivMenu;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.supplier_item_layout, null);
        holder.tvMob = rowView.findViewById(R.id.tvSupMaster_Mob);
        holder.tvName = rowView.findViewById(R.id.tvSupMaster_Name);
        holder.tvAddress = rowView.findViewById(R.id.tvSupMaster_Address);
        holder.ivMenu = rowView.findViewById(R.id.ivSupMaster_Menu);

        holder.tvName.setText("" + displayedValues.get(position).getSuppName());
        holder.tvMob.setText("" + displayedValues.get(position).getSuppMob());
        holder.tvAddress.setText("" + displayedValues.get(position).getSuppAddr());

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.crud_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_edit) {

                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new AddSupplierFragment();
                            Bundle args = new Bundle();
                            args.putInt("supId", displayedValues.get(position).getSuppId());
                            args.putString("supName", displayedValues.get(position).getSuppName());
                            args.putString("supMobile", displayedValues.get(position).getSuppMob());
                            args.putString("supAddress", displayedValues.get(position).getSuppAddr());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "SupplierMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do You Want To Delete Supplier?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteSupplier(displayedValues.get(position).getSuppId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();


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
                ArrayList<OtherSupplierList> filteredArrayList = new ArrayList<OtherSupplierList>();

                if (originalValues == null) {
                    originalValues = new ArrayList<OtherSupplierList>(displayedValues);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String name = originalValues.get(i).getSuppName();
                        if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new OtherSupplierList(originalValues.get(i).getSuppId(), originalValues.get(i).getSuppName(), originalValues.get(i).getSuppAddr(), originalValues.get(i).getSuppMob(), originalValues.get(i).getDelStatus()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedValues = (ArrayList<OtherSupplierList>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public void deleteSupplier(int sId) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();


        Call<ErrorMessage> errorMessageCall = Constants.myInterface.deleteSupplier(sId);
        errorMessageCall.enqueue(new Callback<ErrorMessage>() {
            @Override
            public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                try {
                    if (response.body() != null) {
                        ErrorMessage message = response.body();
                        if (message.getError()) {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            HomeActivity activity = (HomeActivity) context;
                            Fragment adf = new SupplierMasterFragment();
                            Bundle args = new Bundle();
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();

                        }
                    } else {
                        commonDialog.dismiss();
                        Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ErrorMessage> call, Throwable t) {
                commonDialog.dismiss();
                Toast.makeText(context, "Unable To Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
