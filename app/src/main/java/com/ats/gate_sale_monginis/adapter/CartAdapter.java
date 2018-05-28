package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.bean.CartListData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.fragment.CartFragment.tvTotal;
import static com.ats.gate_sale_monginis.fragment.CartFragment.tvGrandTotal;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvCartCount;


/**
 * Created by MIRACLEINFOTAINMENT on 29/12/17.
 */

public class CartAdapter extends BaseAdapter {

    ArrayList<CartListData> displayedValues;
    ArrayList<CartListData> orderValues;
    Context context;
    private static LayoutInflater inflater = null;


    public CartAdapter(Context context, ArrayList<CartListData> empArray) {
        this.context = context;
        this.displayedValues = empArray;
        this.orderValues = empArray;
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
        TextView tvDate, tvOrderId, tvName, tvDesc, tvWeight, tvQty, tvUnitPrice, tvSubTotal, tvMinusIcon, tvPlusIcon, tvDeleteIcon, tvOfferPrice;
        ImageView ivImage;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;


        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.custom_cart_layout, null);

            holder.tvDate = rowView.findViewById(R.id.tvCustomCart_OrderDate);
            holder.tvQty = rowView.findViewById(R.id.tvCustomCart_OrderId);
            holder.tvName = rowView.findViewById(R.id.tvCustomCart_OrderName);
            holder.tvDesc = rowView.findViewById(R.id.tvCustomCart_OrderDesc);
            holder.tvWeight = rowView.findViewById(R.id.tvCustomCart_OrderWeight);
            holder.tvQty = rowView.findViewById(R.id.tvCustomCart_OrderQty);
            holder.tvUnitPrice = rowView.findViewById(R.id.tvCustomCart_UnitPrice);
            holder.tvOfferPrice = rowView.findViewById(R.id.tvCustomCart_OfferPrice);
            holder.tvSubTotal = rowView.findViewById(R.id.tvCustomCart_SubTotal);
            holder.tvMinusIcon = rowView.findViewById(R.id.tvCustomCart_MinusIcon);
            holder.tvPlusIcon = rowView.findViewById(R.id.tvCustomCart_PlusIcon);
            holder.tvDeleteIcon = rowView.findViewById(R.id.tvCustomCart_Delete);
            holder.ivImage = rowView.findViewById(R.id.ivCustomCart_Image);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }


        if (displayedValues.get(position).getBirthday()==1){
            holder.tvMinusIcon.setVisibility(View.GONE);
            holder.tvPlusIcon.setVisibility(View.GONE);
        }

        holder.tvName.setText("" + displayedValues.get(position).getItemName());

        String imagePath = Constants.IMAGE_PATH + displayedValues.get(position).getItemImage();
        //"http://btulp.com/wp-content/uploads/2017/03/cake-images-hd-2.jpg"
        Picasso.with(context)
                .load(imagePath)
                .placeholder(android.R.color.transparent)
                .error(R.drawable.no_image)
                .into(holder.ivImage);


        holder.tvUnitPrice.setText("Rs. " + String.format("%.2f",displayedValues.get(position).getItemRate()));
        holder.tvUnitPrice.setPaintFlags(holder.tvUnitPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        final SharedPreferences pref = context.getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        float percentDiscount = pref.getFloat("catDiscount", 0);
        double disc = displayedValues.get(position).getItemRate() * (percentDiscount / 100);
        //final double offer = Math.round(displayedValues.get(position).getItemRate() - disc);
        final double offer = displayedValues.get(position).getItemRate() - disc;
        holder.tvOfferPrice.setText("Rs. " + String.format("%.2f", offer));
        holder.tvSubTotal.setText("Rs. " + String.format("%.2f",(offer * displayedValues.get(position).getQuantity())));


        holder.tvQty.setText("" + displayedValues.get(position).getQuantity());

        //holder.tvSubTotal.setText("Rs. " + (displayedValues.get(position).getItemRate() * displayedValues.get(position).getQuantity()));

        holder.tvPlusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    int qty = Integer.parseInt(holder.tvQty.getText().toString());
                    holder.tvQty.setText("" + (qty + 1));
                    displayedValues.get(position).setQuantity((qty + 1));

                    //holder.tvSubTotal.setText("Rs. " + (displayedValues.get(position).getItemRate() * displayedValues.get(position).getQuantity()));
                    holder.tvSubTotal.setText("Rs. " + String.format("%.2f",(offer * displayedValues.get(position).getQuantity())));

                    int totalQty = 0;
                    double totalSum = 0;
                    for (int i = 0; i < cartArray.size(); i++) {
                        totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                        totalQty = totalQty + cartArray.get(i).getQuantity();
                    }
                    tvCartCount.setText("" + totalQty);
                    //tvTotal.setText("" + Math.round(totalSum));
                    tvTotal.setText("" + String.format("%.2f",totalSum));

                    float percentDiscount = pref.getFloat("catDiscount", 0);
                    //  if (percentDiscount > 0) {
                    double disc = totalSum * (percentDiscount / 100);
                    double grandTot = Math.round(totalSum - disc);
                    //tvGrandTotal.setText("" + grandTot);
                    tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));
                    // }

                }
            }
        });


        holder.tvMinusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.tvQty.getText().toString().isEmpty()) {
                    if (Integer.parseInt(holder.tvQty.getText().toString()) > 1) {
                        int qty = Integer.parseInt(holder.tvQty.getText().toString());
                        holder.tvQty.setText("" + (qty - 1));
                        displayedValues.get(position).setQuantity((qty - 1));

                        // holder.tvSubTotal.setText("Rs. " + (displayedValues.get(position).getItemRate() * displayedValues.get(position).getQuantity()));
                        holder.tvSubTotal.setText("Rs. " + String.format("%.2f",(offer * displayedValues.get(position).getQuantity())));

                        int totalQty = 0;
                        double totalSum = 0;
                        for (int i = 0; i < cartArray.size(); i++) {
                            totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                            totalQty = totalQty + cartArray.get(i).getQuantity();
                        }
                        tvCartCount.setText("" + totalQty);
                        //tvTotal.setText("" + Math.round(totalSum));
                        tvTotal.setText("" + String.format("%.2f",totalSum));


                        float percentDiscount = pref.getFloat("catDiscount", 0);
                        //if (percentDiscount > 0) {
                        double disc = totalSum * (percentDiscount / 100);
                        double grandTot = Math.round(totalSum - disc);
                        //tvGrandTotal.setText("" + grandTot);
                        tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));
                        // }
                    }
                }
            }
        });


        holder.tvDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                builder.setTitle("Confirm Action");
                builder.setMessage("Remove " + displayedValues.get(position).getItemName() + " from cart");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int count = Integer.parseInt(tvCartCount.getText().toString());
                        tvCartCount.setText("" + (count - displayedValues.get(position).getQuantity()));


                        cartArray.remove(displayedValues.get(position));
                        notifyDataSetChanged();

                        double totalSum = 0;
                        for (int i = 0; i < cartArray.size(); i++) {
                            totalSum = totalSum + (cartArray.get(i).getQuantity() * cartArray.get(i).getItemRate());
                        }
                        //tvTotal.setText("" + Math.round(totalSum));
                        tvTotal.setText("" + String.format("%.2f",totalSum));


                        float percentDiscount = pref.getFloat("catDiscount", 0);
                        // if (percentDiscount > 0) {
                        double disc = totalSum * (percentDiscount / 100);
                        double grandTot = Math.round(totalSum - disc);
                        tvGrandTotal.setText("" + String.format("%.2f", (totalSum - disc)));
                        //}


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
        });

        return rowView;
    }


}
