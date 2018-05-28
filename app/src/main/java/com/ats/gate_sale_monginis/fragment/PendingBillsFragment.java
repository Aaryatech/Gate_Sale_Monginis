package com.ats.gate_sale_monginis.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.activity.LoginActivity;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.interfaces.PendingBillInterface;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PendingBillsFragment extends Fragment implements PendingBillInterface {


    private ListView lvList;
    PendingBillListAdapter adapter;

    private ArrayList<BillHeaderListData> billData = new ArrayList<>();

    int userId, userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_bill, container, false);

        lvList = view.findViewById(R.id.lvPendingList);

        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData userBean = gson.fromJson(json2, LoginData.class);
        try {

            if (userBean != null) {
                userId = userBean.getUserId();
                userType = userBean.getUserType();
            }
        } catch (Exception e) {
            //Log.e("HomeActivity : ", " Exception : " + e.getMessage());
            e.printStackTrace();
        }

        if (userType == 1) {
            getInitiatorBillList("", "", 1, userId);
        } else {
            getBillList("", ""
                    , 1, 0, 0, 0);
        }

        return view;
    }


    public void getBillList(String fromDate, String toDate, int isApproved, int approvedBy, int isAmtCollected, int collectedBy) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<BillHeaderListData>> arrayListCall = Constants.myInterface.getBillData(fromDate, toDate, isApproved, approvedBy, isAmtCollected, collectedBy);
            arrayListCall.enqueue(new Callback<ArrayList<BillHeaderListData>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderListData>> call, Response<ArrayList<BillHeaderListData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<BillHeaderListData> data = response.body();

                            commonDialog.dismiss();
                            billData.clear();
                            billData = data;

                            adapter = new PendingBillListAdapter(getContext(), billData);
                            lvList.setAdapter(adapter);

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
                public void onFailure(Call<ArrayList<BillHeaderListData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    public void getInitiatorBillList(String fromDate, String toDate, int isApproved, int initiatorUserId) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ArrayList<BillHeaderListData>> arrayListCall = Constants.myInterface.getInitiatorBillData(fromDate, toDate, isApproved, initiatorUserId);
            arrayListCall.enqueue(new Callback<ArrayList<BillHeaderListData>>() {
                @Override
                public void onResponse(Call<ArrayList<BillHeaderListData>> call, Response<ArrayList<BillHeaderListData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<BillHeaderListData> data = response.body();

                            commonDialog.dismiss();
                            billData.clear();
                            billData = data;

                            adapter = new PendingBillListAdapter(getContext(), billData);
                            lvList.setAdapter(adapter);

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
                public void onFailure(Call<ArrayList<BillHeaderListData>> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "No List Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void fragmentBecameVisible() {
        getBillList("", "", 1, 0, 0, 0);
    }

    public void approveBill(int billId, int isApproved, int approveUserId) {
        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<ErrorMessage> messageCall = Constants.myInterface.approveOrRejectBill(billId, isApproved, approveUserId);
            messageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();

                            if (data.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                getBillList("", "", 1, 0, 0, 0);
                            }


                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Process", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    //--------ADAPTER CLASS------------

    public class PendingBillListAdapter extends BaseAdapter {

        ArrayList<BillHeaderListData> displayedValues;
        ArrayList<BillHeaderListData> orderValues;
        Context context;
        private LayoutInflater inflater = null;


        public PendingBillListAdapter(Context context, ArrayList<BillHeaderListData> billArray) {
            this.context = context;
            this.displayedValues = billArray;
            this.orderValues = billArray;
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
            TextView tvDate, tvName, tvCategory, tvApproveIcon, tvRejectIcon, tvBillTotal, tvDiscount, tvInvoice;
            ListView lvBillItems;
            LinearLayout llBillHead, llInvoice;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final Holder holder;
            View rowView = convertView;

            if (rowView == null) {
                holder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(context);
                rowView = inflater.inflate(R.layout.custom_pending_bill_header_layout, null);

                holder.tvName = rowView.findViewById(R.id.tvCustomPendingBill_Name);
                holder.tvDate = rowView.findViewById(R.id.tvCustomPendingBill_Date);
                holder.tvCategory = rowView.findViewById(R.id.tvCustomPendingBill_Category);
                holder.tvApproveIcon = rowView.findViewById(R.id.tvCustomPendingBill_ApproveIcon);
                holder.tvRejectIcon = rowView.findViewById(R.id.tvCustomPendingBill_RejectIcon);
                holder.lvBillItems = rowView.findViewById(R.id.lvCustomPendingBill_ItemsList);
                holder.llBillHead = rowView.findViewById(R.id.llCustomPendingBill_Head);
                holder.tvBillTotal = rowView.findViewById(R.id.tvCustomPendingBill_BillTotal);
                holder.tvDiscount = rowView.findViewById(R.id.tvCustomPendingBill_Discount);
                holder.tvInvoice = rowView.findViewById(R.id.tvCustomPendingBill_BillInvoice);
                holder.llInvoice = rowView.findViewById(R.id.llCustomPendingBill_Invoice);

                rowView.setTag(holder);

            } else {
                holder = (Holder) rowView.getTag();
            }

            if (userType == 1) {
                holder.tvApproveIcon.setVisibility(View.GONE);
                holder.tvRejectIcon.setVisibility(View.GONE);
            }

            holder.llInvoice.setVisibility(View.VISIBLE);
            holder.tvInvoice.setText("Invoice : " + displayedValues.get(position).getInvoiceNo());

            holder.tvName.setText("" + displayedValues.get(position).getCustName());

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

            ArrayList<GateSaleBillDetailList> itemArray = new ArrayList<>();

            if (displayedValues.get(position).getGateSaleBillDetailList().size() > 0) {
                for (int i = 0; i < displayedValues.get(position).getGateSaleBillDetailList().size(); i++) {
                    itemArray.add(displayedValues.get(position).getGateSaleBillDetailList().get(i));
                }
                PendingBillItemsAdapter adapter = new PendingBillItemsAdapter(context, itemArray, displayedValues.get(position).getDiscountPer());
                holder.lvBillItems.setAdapter(adapter);
            }

            setListViewHeightBasedOnChildren(holder.lvBillItems);


          /*  holder.llBillHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.lvBillItems.getVisibility() == View.VISIBLE) {
                        holder.lvBillItems.setVisibility(View.GONE);
                    } else if (holder.lvBillItems.getVisibility() == View.GONE) {
                        holder.lvBillItems.setVisibility(View.VISIBLE);
                    }
                }
            });*/


            holder.tvApproveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("Confirm Action");
                    builder.setMessage("Do You Want To Approve Bill?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            approveBill(displayedValues.get(position).getBillId(), 2, userId);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });

            holder.tvRejectIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                    builder.setTitle("Confirm Action");
                    builder.setMessage("Do You Want To Reject Bill?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            approveBill(displayedValues.get(position).getBillId(), 3, userId);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

        public class PendingBillItemsAdapter extends BaseAdapter {

            ArrayList<GateSaleBillDetailList> displayedValues;
            ArrayList<GateSaleBillDetailList> orderValues;
            Context context;
            private LayoutInflater inflater = null;
            float discount;

            public PendingBillItemsAdapter(Context context, ArrayList<GateSaleBillDetailList> billItemArray, float discount) {
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

        public void setListViewHeightBasedOnChildren(ListView listView) {
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


}
