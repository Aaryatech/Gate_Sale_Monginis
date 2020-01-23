package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;

import java.util.ArrayList;

public class RejectedBillListAdapter extends BaseAdapter {

    ArrayList<BillHeaderListData> displayedValues;
    ArrayList<BillHeaderListData> orderValues;
    Context context;
    private static LayoutInflater inflater = null;
    int type;

    public RejectedBillListAdapter(Context context, ArrayList<BillHeaderListData> billArray, int type) {
        this.context = context;
        this.displayedValues = billArray;
        this.orderValues = billArray;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
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
        TextView tvDate, tvName, tvCategory, tvBillTotal, tvDiscount, tvBillInvoice;
        ListView lvBillItems;
        LinearLayout llBillHead, llInvoice;
        CheckBox checkBox;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_rejected_bill_list, null);

            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.tvDate = rowView.findViewById(R.id.tvDate);
            holder.tvCategory = rowView.findViewById(R.id.tvCategory);
            holder.lvBillItems = rowView.findViewById(R.id.lvItemsList);
            holder.llBillHead = rowView.findViewById(R.id.llHead);
            holder.tvBillTotal = rowView.findViewById(R.id.tvBillTotal);
            holder.tvDiscount = rowView.findViewById(R.id.tvDiscount);

            holder.llInvoice = rowView.findViewById(R.id.llInvoice);
            holder.tvBillInvoice = rowView.findViewById(R.id.tvBillInvoice);

            holder.checkBox = rowView.findViewById(R.id.checkbox);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.llInvoice.setVisibility(View.VISIBLE);


        holder.tvName.setText("" + displayedValues.get(position).getCustName());
        holder.tvBillInvoice.setText("Invoice : " + displayedValues.get(position).getInvoiceNo());

        String category = "";
        if (displayedValues.get(position).getCategory() == 0) {
            category = "Other Bill";
        } else if (displayedValues.get(position).getCategory() == 1) {
            category = "Employee";
        } else if (displayedValues.get(position).getCategory() == 2) {
            category = "Corporate";
        } else if (displayedValues.get(position).getCategory() == 3) {
            category = "Administrative";
        } else if (displayedValues.get(position).getCategory() == 4) {
            category = "Director";
        } else if (displayedValues.get(position).getCategory() == 5) {
            category = "VVIP";
        } else if (displayedValues.get(position).getCategory() == 6) {
            category = "Others";
        }

        holder.tvCategory.setText("" + category);

        holder.tvDate.setText("" + displayedValues.get(position).getBillDate());

        int disc = (int) displayedValues.get(position).getDiscountPer();
        holder.tvDiscount.setText("" + disc + " % OFF");

        holder.tvBillTotal.setText("" + displayedValues.get(position).getBillGrantAmt() + "/-");


        if (displayedValues.get(position).getGateSaleBillDetailList().size() > 0) {
            ArrayList<GateSaleBillDetailList> itemArray = new ArrayList<>();
            for (int i = 0; i < displayedValues.get(position).getGateSaleBillDetailList().size(); i++) {
                itemArray.add(displayedValues.get(position).getGateSaleBillDetailList().get(i));
            }
            RejectedBillItemsAdapter adapter = new RejectedBillItemsAdapter(context, itemArray, displayedValues.get(position).getDiscountPer());
            holder.lvBillItems.setAdapter(adapter);

            setListViewHeightBasedOnChildren(holder.lvBillItems);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    displayedValues.get(position).setRejected(true);
                } else {
                    displayedValues.get(position).setRejected(false);
                }
            }
        });

        if (displayedValues.get(position).isRejected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        return rowView;
    }


    public class RejectedBillItemsAdapter extends BaseAdapter {

        ArrayList<GateSaleBillDetailList> displayedValues;
        ArrayList<GateSaleBillDetailList> orderValues;
        Context context;
        private LayoutInflater inflater = null;
        float discount;

        public RejectedBillItemsAdapter(Context context, ArrayList<GateSaleBillDetailList> billItemArray, float discount) {
            this.context = context;
            this.displayedValues = billItemArray;
            this.orderValues = billItemArray;
            this.inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.discount = discount;
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
            TextView tvName, tvQty, tvUnitPrice, tvSubTotal;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            final Holder holder;
            View rowView = convertView;

            if (rowView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(R.layout.custom_pending_bill_item_layout, null);

                holder.tvName = rowView.findViewById(R.id.tvCustomPendingBillItem_Name);
                holder.tvQty = rowView.findViewById(R.id.tvCustomPendingBillItem_Qty);
                holder.tvUnitPrice = rowView.findViewById(R.id.tvCustomPendingBillItem_UnitPrice);
                holder.tvSubTotal = rowView.findViewById(R.id.tvCustomPendingBillItem_SubTotal);

                rowView.setTag(holder);

            } else {
                holder = (Holder) rowView.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getItemName());

            int qty = (int) displayedValues.get(position).getItemQty();
            holder.tvQty.setText("Qty : " + qty);

            float discAmt = displayedValues.get(position).getItemRate() * (discount / 100);
            float offer = displayedValues.get(position).getItemRate() - discAmt;

            holder.tvUnitPrice.setText("Unit Price : Rs. " + String.format("%.2f", offer));
            holder.tvSubTotal.setText("Sub Total : Rs. " + String.format("%.2f", (qty * offer)));


            return rowView;
        }


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
