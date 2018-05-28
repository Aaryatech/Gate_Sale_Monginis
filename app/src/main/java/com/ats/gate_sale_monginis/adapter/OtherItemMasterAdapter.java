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
import com.ats.gate_sale_monginis.bean.OtherItemList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddOtherItemFragment;
import com.ats.gate_sale_monginis.fragment.AddSupplierFragment;
import com.ats.gate_sale_monginis.fragment.OtherItemMasterFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MAXADMIN on 8/2/2018.
 */

public class OtherItemMasterAdapter extends BaseAdapter implements Filterable {

    ArrayList<OtherItemList> displayedValues;
    ArrayList<OtherItemList> originalValues;
    Context context;
    private static LayoutInflater inflater = null;


    public OtherItemMasterAdapter(Context context, ArrayList<OtherItemList> empArray) {
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
        TextView tvName, tvRate;
        ImageView ivMenu;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.custom_other_item_master_layout, null);
        holder.tvName = rowView.findViewById(R.id.tvOtherItemMaster_Name);
        holder.tvRate = rowView.findViewById(R.id.tvOtherItemMaster_Rate);
        holder.ivMenu = rowView.findViewById(R.id.ivOtherItemMaster_Menu);

        holder.tvName.setText("" + displayedValues.get(position).getItemName());
        holder.tvRate.setText("" + displayedValues.get(position).getItemRate());

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
                            Fragment adf = new AddOtherItemFragment();
                            Bundle args = new Bundle();
                            args.putInt("itemId", displayedValues.get(position).getItemId());
                            args.putString("itemName", displayedValues.get(position).getItemName());
                            args.putString("itemRate", "" + displayedValues.get(position).getItemRate());
                            adf.setArguments(args);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "OtherItemMasterFragment").commit();

                        } else if (menuItem.getItemId() == R.id.action_delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                            builder.setTitle("Confirm Action");
                            builder.setMessage("Do You Want To Delete Item?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(displayedValues.get(position).getItemId());
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
                ArrayList<OtherItemList> filteredArrayList = new ArrayList<OtherItemList>();

                if (originalValues == null) {
                    originalValues = new ArrayList<OtherItemList>(displayedValues);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String name = originalValues.get(i).getItemName();
                        if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new OtherItemList(originalValues.get(i).getItemId(), originalValues.get(i).getSuppId(), originalValues.get(i).getItemName(), originalValues.get(i).getItemQty(), originalValues.get(i).getItemRate(), originalValues.get(i).getDelStatus()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }


                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedValues = (ArrayList<OtherItemList>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public void deleteItem(int itemId) {
        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();


        Call<ErrorMessage> errorMessageCall = Constants.myInterface.deleteOtherItem(itemId);
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
                            Fragment adf = new OtherItemMasterFragment();
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
