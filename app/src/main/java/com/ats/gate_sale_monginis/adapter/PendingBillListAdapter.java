package com.ats.gate_sale_monginis.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
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

import com.ats.gate_sale_monginis.BuildConfig;
import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.ats.gate_sale_monginis.activity.PrintReceiptActivity;
import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.GateSaleBillDetailList;
import com.ats.gate_sale_monginis.common.CommonDialog;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.PrinterIPFragment;
import com.ats.gate_sale_monginis.util.PrintHelper;
import com.ats.gate_sale_monginis.util.Prints;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lvrenyang.io.NETPrinting;
import com.lvrenyang.io.Pos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MIRACLEINFOTAINMENT on 30/12/17.
 */

public class PendingBillListAdapter extends BaseAdapter {

    ArrayList<BillHeaderListData> displayedValues;
    ArrayList<BillHeaderListData> orderValues;
    Context context;
    private static LayoutInflater inflater = null;
    int type;

    //------PDF------
    private PdfPCell cell;
    private String path;
    private File dir;
    private File file;
    private TextInputLayout inputLayoutBillTo, inputLayoutEmailTo;
    double totalAmount = 0;
    int day, month, year;
    long dateInMillis;
    long amtLong;
    private Image bgImage;
    BaseColor myColor = WebColors.getRGBColor("#ffffff");
    BaseColor myColor1 = WebColors.getRGBColor("#cbccce");

    public PendingBillListAdapter(Context context, ArrayList<BillHeaderListData> billArray, int type) {
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
        TextView tvDate, tvName, tvCategory, tvApproveIcon, tvRejectIcon, tvBillTotal, tvDiscount, tvPrintIcon, tvBillInvoice;
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
            holder.tvPrintIcon = rowView.findViewById(R.id.tvCustomPendingBill_PrintIcon);

            holder.llInvoice = rowView.findViewById(R.id.llCustomPendingBill_Invoice);
            holder.tvBillInvoice = rowView.findViewById(R.id.tvCustomPendingBill_BillInvoice);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }


        if (type == 2) {
            holder.tvPrintIcon.setVisibility(View.VISIBLE);
        } else {
            holder.tvPrintIcon.setVisibility(View.GONE);
        }

        holder.llInvoice.setVisibility(View.VISIBLE);

        holder.tvApproveIcon.setVisibility(View.GONE);
        holder.tvRejectIcon.setVisibility(View.GONE);

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
            PendingBillItemsAdapter adapter = new PendingBillItemsAdapter(context, itemArray, displayedValues.get(position).getDiscountPer());
            holder.lvBillItems.setAdapter(adapter);

            setListViewHeightBasedOnChildren(holder.lvBillItems);
            // Log.e("Bill Items : ", "--------------" + itemArray);
        }


        holder.tvPrintIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    HomeActivity activity = (HomeActivity) context;

                    Gson gson = new Gson();
                    String bean = gson.toJson(displayedValues.get(position));

                    Intent intent = new Intent(activity, PrintReceiptActivity.class);
                    intent.putExtra("Bean",bean);
                    context.startActivity(intent);

//                    PrintHelper printHelper = new PrintHelper(activity, ip, 9, displayedValues.get(position));
//                    printHelper.runPrintReceiptSequence();
                } catch (Exception e) {
                }

               /* AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                builder.setTitle("Confirm Action");
                builder.setMessage("Do You Want To Generate Bill?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GateSaleApp";
                            dir = new File(path);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            createPDF(displayedValues.get(position).getCustName(), "", displayedValues.get(position));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();*/


            }
        });


     /*   holder.llBillHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.lvBillItems.getVisibility() == View.VISIBLE) {
                    holder.lvBillItems.setVisibility(View.GONE);
                } else if (holder.lvBillItems.getVisibility() == View.GONE) {
                    holder.lvBillItems.setVisibility(View.VISIBLE);
                }
            }
        });*/


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


    public void createPDF(String to, String email, BillHeaderListData billData) throws FileNotFoundException, DocumentException {

        final CommonDialog commonDialog = new CommonDialog(context, "Loading", "Please Wait...");
        commonDialog.show();

        String resultTo = to;
        //create document files
        // Rectangle pagesize = new Rectangle(288, 720);

        Document doc = new Document();
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
        Font boldTotalFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font boldTextFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        try {
            //Log.e("PDFCreator", "PDF Path: " + path);

            Calendar calendar = Calendar.getInstance();
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            dateInMillis = calendar.getTimeInMillis();

            //file = new File(dir, resultTo + "_Bill_" + day + "-" + month + "-" + year + "_" + hour + ":" + minutes + ".pdf");
            file = new File(dir, "Bill.pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            Log.d("File Name-------------", "" + file.getName());
            //open the document
            doc.open();

            PdfPTable ptHead = new PdfPTable(1);
            ptHead.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph("Bill", boldFont));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);

            //ptHead.addCell(cell);

            PdfPTable ptHeadBlank1 = new PdfPTable(1);
            ptHeadBlank1.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank1.addCell(cell);

            PdfPTable ptHeadBlank2 = new PdfPTable(1);
            ptHeadBlank2.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(" "));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(1);
            ptHeadBlank2.addCell(cell);

            //create table
            PdfPTable pt = new PdfPTable(1);
            pt.setWidthPercentage(100);

            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.ic_password);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);


                cell = new PdfPCell(new Paragraph("Galdhar Foods Private Limited", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph("BILL", boldFont));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(1);
                pt.addCell(cell);


                //IMAGE

                bgImage = Image.getInstance(bitmapdata);
                bgImage.scaleAbsolute(150f, 150f);
                bgImage.setAbsolutePosition(150f, 150f);
                cell.addElement(bgImage);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                // pt.addCell(cell);

//                cell = new PdfPCell(new Paragraph("Date : " + day + "/" + month + "/" + year));
//                cell.setBorder(Rectangle.NO_BORDER);
//                cell.setHorizontalAlignment(2);
//                pt.addCell(cell);

                cell = new PdfPCell(new Paragraph(" "));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);


                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                cell.addElement(ptHead);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(5);
                float[] columnWidth = new float[]{10, 50, 30, 30, 30};
                table.setWidths(columnWidth);
                table.setTotalWidth(columnWidth);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setColspan(5);
                cell.addElement(pTable);

                table.addCell(cell);//image cell&address

                cell = new PdfPCell(new Phrase("DATE : " + day + "/" + month + "/" + year, boldTextFont));
                cell.setColspan(5);
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("INVOICE NO : " + billData.getInvoiceNo(), boldTextFont));
                cell.setColspan(5);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setColspan(5);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Name : " + resultTo, textFont));
                cell.setColspan(5);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(5);
                cell.setBackgroundColor(myColor);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);


                cell = new PdfPCell(new Phrase("NO.", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ITEM", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("QUANTITY", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("UNIT PRICE", boldTextFont));
                cell.setHorizontalAlignment(1);
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("AMOUNT", boldTextFont));
                cell.setBackgroundColor(myColor1);
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                ArrayList<GateSaleBillDetailList> itemList = new ArrayList<>();
                itemList.addAll(billData.getGateSaleBillDetailList());

                float total = 0;
                for (int i = 0; i < itemList.size(); i++) {

                    table.addCell("" + (i + 1));

                    table.addCell("" + itemList.get(i).getItemName());

                    int qty = (int) itemList.get(i).getItemQty();

                    cell = new PdfPCell(new Phrase("" + qty));
                    cell.setHorizontalAlignment(1);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + itemList.get(i).getItemRate()));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("" + (itemList.get(i).getItemQty() * itemList.get(i).getItemRate())));
                    cell.setHorizontalAlignment(2);
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

                    total = total + (itemList.get(i).getItemQty() * itemList.get(i).getItemRate());
                }

                //----BLANK ROW
                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setBackgroundColor(myColor);
                table.addCell(cell);

                //-------------NEW TABLE--------------------------

                PdfPTable table2 = new PdfPTable(3);
                float[] columnWidth2 = new float[]{60, 40, 50};
                table2.setWidths(columnWidth2);

                cell = new PdfPCell(new Phrase(""));
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                cell = new PdfPCell(new Phrase("  TOTAL", boldTotalFont));
                cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("" + total, boldTotalFont));
                cell.setHorizontalAlignment(2);
                cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM | Rectangle.TOP);
                cell.setBackgroundColor(myColor);
                table2.addCell(cell);


                doc.add(table);
                doc.add(table2);

            } catch (DocumentException de) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "DocumentException:" + de);
                Toast.makeText(context, "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                commonDialog.dismiss();
                //Log.e("PDFCreator", "ioException:" + e);
                Toast.makeText(context, "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
            } finally {
                doc.close();
                commonDialog.dismiss();

                File file1 = new File(dir, "Bill.pdf");

                if (file1.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        intent.setDataAndType(Uri.fromFile(file1), "application/pdf");
                    } else {
                        if (file1.exists()) {
                            String authorities = BuildConfig.APPLICATION_ID + ".provider";
                            Uri uri = FileProvider.getUriForFile(context, authorities, file1);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);

                } else {
                    commonDialog.dismiss();
                    Toast.makeText(context, "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            commonDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(context, "Unable To Generate Bill", Toast.LENGTH_SHORT).show();
        }
    }


}
