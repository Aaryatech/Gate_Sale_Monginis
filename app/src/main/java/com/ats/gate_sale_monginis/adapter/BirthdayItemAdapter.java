package com.ats.gate_sale_monginis.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.bean.CartListData;
import com.ats.gate_sale_monginis.bean.Item;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.CartFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeProductsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;

/**
 * Created by MAXADMIN on 7/2/2018.
 */

public class BirthdayItemAdapter extends BaseAdapter implements Filterable {

    ArrayList<Item> displayedValues;
    ArrayList<Item> originalValues;
    Context context;
    private static LayoutInflater inflater = null;
    Dialog dialog;

    RelativeLayout relativeLayout;


    public BirthdayItemAdapter(Context context, Dialog dialog, ArrayList<Item> empArray) {
        this.context = context;
        this.dialog = dialog;
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Item> filteredArrayList = new ArrayList<Item>();

                if (originalValues == null) {
                    originalValues = new ArrayList<Item>(displayedValues);
                }

                if (charSequence == null || charSequence.length() == 0) {
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        String name = originalValues.get(i).getItemName();
                        if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString())) {
                            filteredArrayList.add(new Item(originalValues.get(i).getId(), originalValues.get(i).getItemId(), originalValues.get(i).getItemName(), originalValues.get(i).getItemGrp1(), originalValues.get(i).getItemGrp2(), originalValues.get(i).getItemGrp3(), originalValues.get(i).getItemRate1(), originalValues.get(i).getItemRate2(), originalValues.get(i).getItemMrp1(), originalValues.get(i).getItemMrp2(), originalValues.get(i).getItemMrp3(), originalValues.get(i).getItemImage(), originalValues.get(i).getItemTax1(), originalValues.get(i).getItemTax2(), originalValues.get(i).getItemTax3(), originalValues.get(i).getItemIsUsed(), originalValues.get(i).getItemSortId(), originalValues.get(i).getGrnTwo(), originalValues.get(i).getDelStatus(), originalValues.get(i).getItemRate3(), originalValues.get(i).getMinQty(), originalValues.get(i).getShelfLife(), originalValues.get(i).getQty()));
                        }
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                displayedValues = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public static class Holder {
        TextView tvName, tvAdd;
        ImageView ivImage;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.custom_birthday_item_layout, null);

            holder.tvName = rowView.findViewById(R.id.tvCustomBirthdayItem_Name);
            holder.ivImage = rowView.findViewById(R.id.ivCustomBirthdayItem_Image);
            holder.tvAdd = rowView.findViewById(R.id.tvCustomBirthdayItem_Add);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText("" + displayedValues.get(position).getItemName());

        String imagePath = Constants.IMAGE_PATH + displayedValues.get(position).getItemImage();
        //http://btulp.com/wp-content/uploads/2017/03/cake-images-hd-2.jpg
        try {
            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(android.R.color.transparent)
                    .error(R.drawable.no_image)
                    .into(holder.ivImage);
        } catch (Exception e) {
        }


        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeActivity.llCart.setVisibility(View.VISIBLE);

                CartListData data = new CartListData(displayedValues.get(position).getId(), displayedValues.get(position).getItemId(), displayedValues.get(position).getItemName(), displayedValues.get(position).getItemImage(), 0, 1, 1);
                /*if (cartArray.size() > 0) {
                    boolean flag = false;
                    for (int i = 0; i < cartArray.size(); i++) {
                        if (cartArray.get(i).getId() == data.getId() && cartArray.get(i).getBirthday() == 1) {
                            flag = false;
                            break;
                        } else if (cartArray.get(i).getId() == data.getId() && cartArray.get(i).getBirthday() == 0) {
                            flag = true;
                        } else {
                            flag = true;
                        }
                    }

                    if (flag) {
                        int status = 0;
                        for (int i = 0; i < cartArray.size(); i++) {
                            if (cartArray.get(i).getBirthday() == 1) {
                                status = 1;

                                cartArray.get(i).setId(data.getId());
                                cartArray.get(i).setItemId(data.getItemId());
                                cartArray.get(i).setItemName(data.getItemName());
                                cartArray.get(i).setItemImage(data.getItemImage());
                                cartArray.get(i).setItemRate(0);
                                cartArray.get(i).setQuantity(1);
                                cartArray.get(i).setBirthday(1);

                                break;
                            } else {
                                status = 0;
                            }
                        }

                        if (status == 0) {
                            cartArray.add(data);

                        }

                        dialog.dismiss();
                        HomeActivity activity = (HomeActivity) context;
                        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, new CartFragment(),"ProductFragment");
                        ft.commit();


                    }
                } else {
                    cartArray.add(data);

                    dialog.dismiss();
                    HomeActivity activity = (HomeActivity) context;
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new CartFragment(),"ProductFragment");
                    ft.commit();
                }*/

                cartArray.clear();
                cartArray.add(data);

                dialog.dismiss();
                HomeActivity activity = (HomeActivity) context;
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new CartFragment(),"ProductFragment");
                ft.commit();


                if (cartArray.size() > 0) {
                    int totalQty = 0;
                    for (int i = 0; i < cartArray.size(); i++) {
                        totalQty = totalQty + cartArray.get(i).getQuantity();
                    }
                    HomeActivity.tvCartCount.setText("" + totalQty);
                }


                Toast.makeText(context, "" + displayedValues.get(position).getItemName() + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

}
