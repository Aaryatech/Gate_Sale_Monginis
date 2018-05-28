package com.ats.gate_sale_monginis.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.adapter.EmployeeListAdapter;
import com.ats.gate_sale_monginis.bean.CartListData;
import com.ats.gate_sale_monginis.bean.EmployeeListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetail;
import com.ats.gate_sale_monginis.bean.GateSaleBillHeader;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.bean.OtherBillItemBean;
import com.ats.gate_sale_monginis.bean.OtherItemListData;
import com.ats.gate_sale_monginis.bean.SupplierListData;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvCartCount;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;


public class OtherBillsFragment extends Fragment implements View.OnClickListener {

    private EditText edVendorname, edQty, edRate;
    private Spinner spCategory, spVendor;
    private TextView tvSubmit, tvAdd, tvTotal;

    private ListView lvBillList;

    private ArrayList<OtherBillItemBean> billItemsArray = new ArrayList<>();

    private ArrayList<String> supplierNameArray = new ArrayList<>();
    private ArrayList<Integer> supplierIdArray = new ArrayList<>();

    private ArrayList<String> itemNameArray = new ArrayList<>();
    private ArrayList<Integer> itemIdArray = new ArrayList<>();
    private ArrayList<String> itemQtyArray = new ArrayList<>();
    private ArrayList<Float> itemRateArray = new ArrayList<>();

    ArrayAdapter<String> supplierAdapter;
    ArrayAdapter<String> itemAdapter;

    int userId, userType;

    OtherBillAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_bills, container, false);
        tvTitle.setText("Other Bills");

        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        edVendorname = view.findViewById(R.id.edOtherBills_VendorName);
        edQty = view.findViewById(R.id.edOtherBills_Qty);
        edRate = view.findViewById(R.id.edOtherBills_Rate);
        tvAdd = view.findViewById(R.id.tvOtherBills_Add);
        tvSubmit = view.findViewById(R.id.tvOtherBills_Submit);
        spCategory = view.findViewById(R.id.spOtherBills_Category);
        spVendor = view.findViewById(R.id.spOtherBills_Vendor);
        lvBillList = view.findViewById(R.id.lvOtherBill_List);
        tvTotal = view.findViewById(R.id.tvOtherBills_Total);

        tvAdd.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        edQty.setText("0");

        setListViewHeightBasedOnChildren(lvBillList);

        try {
            SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            Gson gson = new Gson();
            String json2 = pref.getString("loginData", "");
            LoginData userBean = gson.fromJson(json2, LoginData.class);
            //Log.e("User Bean : ", "---------------" + userBean);
            if (userBean != null) {
                userId = userBean.getUserId();
                userType = userBean.getUserType();
            }
        } catch (Exception e) {
        }

        ArrayList<String> categoryArray = new ArrayList<>();
        categoryArray.add("Select Item");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_layout, categoryArray);
        spCategory.setAdapter(categoryAdapter);
//
//        ArrayList<String> vendorArray = new ArrayList<>();
//        vendorArray.add("Select Vendor");
//        vendorArray.add("VVIP");
//
//        ArrayAdapter<String> vendorAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vendorArray);
//        spVendor.setAdapter(vendorAdapter);

        adapter = new OtherBillAdapter(getContext(), billItemsArray);
        lvBillList.setAdapter(adapter);

        edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int qty = Integer.parseInt(charSequence.toString());
                    float amt = itemRateArray.get(spCategory.getSelectedItemPosition());
                    float rate = amt * qty;
                    edRate.setText("" + rate);

                } catch (Exception e) {
                    edRate.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getSupplierList();
        getItemList(1);


       /* spVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i != 0) {
                        getItemList(supplierIdArray.get(i));
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i != 0) {
                        edRate.setText("" + itemRateArray.get(i));
                        edQty.setText("1");
                    } else {
                        edRate.setText("0");
                        edQty.setText("0");
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvOtherBills_Add) {
            if (spVendor.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Vendor", Toast.LENGTH_SHORT).show();
                spVendor.requestFocus();
            } else if (spCategory.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Item", Toast.LENGTH_SHORT).show();
                spVendor.requestFocus();
            } else {

                String itemName = spCategory.getSelectedItem().toString();
                int itemId = itemIdArray.get(spCategory.getSelectedItemPosition());
                String vendor = spVendor.getSelectedItem().toString();
                int vendorId = supplierIdArray.get(spVendor.getSelectedItemPosition());
                int qty = Integer.parseInt(edQty.getText().toString());
                float rate = Float.parseFloat(edRate.getText().toString());
                float perItemRate = itemRateArray.get(spCategory.getSelectedItemPosition());


                OtherBillItemBean
                        bean = new OtherBillItemBean(vendorId, vendor, itemId, itemName, "", qty, perItemRate, rate, 0);

                boolean flag = true;
                if (billItemsArray.size() > 0) {
                    for (int i = 0; i < billItemsArray.size(); i++) {
                        if (billItemsArray.get(i).getItemId() == itemId) {
                            billItemsArray.get(i).setQty(qty);
                            billItemsArray.get(i).setRate(rate);
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                    if (!flag) {
                        billItemsArray.add(bean);
                    }

                } else {
                    billItemsArray.add(bean);
                }

                float total = 0;
                if (billItemsArray.size() > 0) {
                    for (int i = 0; i < billItemsArray.size(); i++) {
                        total = total + billItemsArray.get(i).getRate();
                    }
                    tvTotal.setText("" + total);
                } else {
                    tvTotal.setText("" + total);
                }

                adapter.notifyDataSetChanged();

                spCategory.setSelection(0);
                edRate.setText("0");
                spVendor.setEnabled(false);

            }


        } else if (view.getId() == R.id.tvOtherBills_Submit) {

            if (billItemsArray.size() > 0) {

                String vendor = spVendor.getSelectedItem().toString();
                int vendorId = supplierIdArray.get(spVendor.getSelectedItemPosition());
                float total = Float.parseFloat(tvTotal.getText().toString());

                ArrayList<GateSaleBillDetail> itemArray = new ArrayList<>();
                for (int i = 0; i < billItemsArray.size(); i++) {
                    GateSaleBillDetail billDetail = new GateSaleBillDetail(0, 0, billItemsArray.get(i).getItemId(), (float) billItemsArray.get(i).getQty(), billItemsArray.get(i).getPerItemRate(), 0);
                    itemArray.add(billDetail);
                }


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String todaysDate = sdf.format(Calendar.getInstance().getTimeInMillis());

                if (userType != 1) {
                    GateSaleBillHeader billHeader = new GateSaleBillHeader(0, "", 0, 1, vendor, vendorId, 0, total, 0, 0, total, 2, todaysDate, userId, 1, "", 0, 0, userId, 0, itemArray);
                    saveOrder(billHeader);
                } else {
                    GateSaleBillHeader billHeader = new GateSaleBillHeader(0, "", 0, 1, vendor, vendorId, 0, total, 0, 0, total, 1, "", 0, 1, "", 0, 0, userId, 0, itemArray);
                    saveOrder(billHeader);
                }

            } else {
                Toast.makeText(getActivity(), "Please Select Items For Bill", Toast.LENGTH_SHORT).show();
            }


        }


    }


    public void getSupplierList() {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<SupplierListData> supplierListDataCall = Constants.myInterface.getSupplierList();
            supplierListDataCall.enqueue(new Callback<SupplierListData>() {
                @Override
                public void onResponse(Call<SupplierListData> call, Response<SupplierListData> response) {
                    try {
                        if (response.body() != null) {
                            SupplierListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                supplierNameArray.clear();
                                supplierIdArray.clear();
                                supplierNameArray.add("Select Supplier");
                                supplierIdArray.add(0);
                                for (int i = 0; i < data.getOtherSupplierList().size(); i++) {
                                    supplierNameArray.add(data.getOtherSupplierList().get(i).getSuppName());
                                    supplierIdArray.add(data.getOtherSupplierList().get(i).getSuppId());
                                }

                                supplierAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_layout, supplierNameArray);
                                spVendor.setAdapter(supplierAdapter);

                            }
                        } else {
                            commonDialog.dismiss();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<SupplierListData> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }


    public void getItemList(int suppId) {

        if (Constants.isOnline(getContext())) {

            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();


            Call<OtherItemListData> otherItemListDataCall = Constants.myInterface.getOtherItemList(suppId);
            otherItemListDataCall.enqueue(new Callback<OtherItemListData>() {
                @Override
                public void onResponse(Call<OtherItemListData> call, Response<OtherItemListData> response) {
                    try {
                        if (response.body() != null) {
                            OtherItemListData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                commonDialog.dismiss();
                                //Log.e("User : ", " ERROR : " + data.getErrorMessage().getError());
                            } else {
                                commonDialog.dismiss();
                                itemNameArray.clear();
                                itemIdArray.clear();
                                itemQtyArray.clear();
                                itemRateArray.clear();
                                itemNameArray.add("Select Item");
                                itemIdArray.add(0);
                                itemQtyArray.add("0");
                                itemRateArray.add(0f);

                                for (int i = 0; i < data.getOtherItemList().size(); i++) {
                                    itemNameArray.add(data.getOtherItemList().get(i).getItemName());
                                    itemIdArray.add(data.getOtherItemList().get(i).getItemId());
                                    itemQtyArray.add(data.getOtherItemList().get(i).getItemQty());
                                    itemRateArray.add(data.getOtherItemList().get(i).getItemRate());

                                }

                                itemAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_layout, itemNameArray);
                                spCategory.setAdapter(itemAdapter);

                            }
                        } else {
                            commonDialog.dismiss();

                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call<OtherItemListData> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    public void saveOrder(GateSaleBillHeader billHeader) {
        if (Constants.isOnline(getContext())) {
            final CommonDialog commonDialog = new CommonDialog(getContext(), "Loading", "Please Wait...");
            commonDialog.show();

            Call<ErrorMessage> errorMessageCall = Constants.myInterface.saveBill(billHeader);
            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage message = response.body();
                            if (message.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "" + message.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                // cartArray.clear();
                                // adapter.notifyDataSetChanged();
                                //  tvTotal.setText("0.0");
                                // tvGrandTotal.setText("0.0");
                                //  tvCartCount.setText("0");
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new HomeFragment(), "Home");
                                ft.commit();
                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                            ////Log.e("AddUser : ", " NULL ");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        ////Log.e("AddUser : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class OtherBillAdapter extends BaseAdapter {

        Context context;
        private ArrayList<OtherBillItemBean> originalValues;
        private ArrayList<OtherBillItemBean> displayedValues;
        LayoutInflater inflater;

        public OtherBillAdapter(Context context, ArrayList<OtherBillItemBean> catArray) {
            this.context = context;
            this.originalValues = catArray;
            this.displayedValues = catArray;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayedValues.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            v = inflater.inflate(R.layout.custom_other_bill_item_layout, null);

            TextView tvName = v.findViewById(R.id.tvCustomOtherBill_Name);
            TextView tvQty = v.findViewById(R.id.tvCustomOtherBill_Qty);
            TextView tvRate = v.findViewById(R.id.tvCustomOtherBill_Rate);
            TextView tvRemove = v.findViewById(R.id.tvCustomOtherBill_Icon);

            tvName.setText("" + displayedValues.get(position).getItemName());
            tvQty.setText("Qty : " + displayedValues.get(position).getQty());
            tvRate.setText("Rate : Rs. " + (displayedValues.get(position).getRate()));

            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float total = Float.parseFloat(tvTotal.getText().toString());
                    //Log.e("TOTAL : ", "----------" + total);
                    float itemPrice = displayedValues.get(position).getRate();
                    //Log.e("ITEM PRICE : ", "----------" + itemPrice);
                    float grandTotal = total - itemPrice;
                    //Log.e("GRAND TOTAL : ", "----------" + grandTotal);
                    tvTotal.setText("" + grandTotal);
                    billItemsArray.remove(displayedValues.get(position));
                    notifyDataSetChanged();
                }
            });


            return v;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("category", "");
        editor.putInt("categoryId", 0);
        editor.putInt("eId", 0);
        editor.putInt("monthlyLimit", 0);
        editor.putInt("yearlyLimit", 0);
        editor.putFloat("catDiscount", 0);
        editor.putFloat("monthlyConsumed", 0);
        editor.putFloat("yearlyConsumed", 0);
        editor.apply();

        llCart.setVisibility(View.INVISIBLE);
        cartArray.clear();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_Filter);
        item.setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
