package com.ats.gate_sale_monginis.interfaces;

import com.ats.gate_sale_monginis.bean.BillHeaderListData;
import com.ats.gate_sale_monginis.bean.DiscountListData;
import com.ats.gate_sale_monginis.bean.EmployeeListData;
import com.ats.gate_sale_monginis.bean.ErrorMessage;
import com.ats.gate_sale_monginis.bean.GateSaleBillHeader;
import com.ats.gate_sale_monginis.bean.GateSaleDiscountList;
import com.ats.gate_sale_monginis.bean.GateSaleUserList;
import com.ats.gate_sale_monginis.bean.Item;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.bean.OtherItemList;
import com.ats.gate_sale_monginis.bean.OtherItemListData;
import com.ats.gate_sale_monginis.bean.OtherSupplierList;
import com.ats.gate_sale_monginis.bean.SettingsData;
import com.ats.gate_sale_monginis.bean.SupplierListData;
import com.ats.gate_sale_monginis.bean.UserListData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public interface InterfaceApi {

    @POST("gatesale/loginUser")
    Call<LoginData> getLogin(@Query("mobile") String mobile, @Query("password") String password);

    @GET("gatesale/getGateSaleUserList")
    Call<UserListData> getAllUserList();

    @POST("gatesale/saveGateSaleUser")
    Call<ErrorMessage> insertUser(@Body GateSaleUserList gateSaleUserList);

    @POST("gatesale/deleteGateSaleUser")
    Call<ErrorMessage> deleteUser(@Query("userId") int userId);

    @POST("gatesale/updateUserToken")
    Call<ErrorMessage> updateToken(@Query("userId") int userId, @Query("token") String token);

    @GET("gatesale/getGateSaleDiscList")
    Call<DiscountListData> getDiscountList();

    @POST("gatesale/saveGateSaleDiscount")
    Call<ErrorMessage> insertDiscount(@Body GateSaleDiscountList saleDiscountList);

    @POST("gatesale/deleteGateSaleDiscount")
    Call<ErrorMessage> deleteDiscount(@Query("discountId") int discountId);

    @GET("gatesale/getGateEmpList")
    Call<EmployeeListData> getEmployeeList();

//    @POST("getItemsByCatId")
//    Call<ArrayList<Item>> getItemsByCategory(@Query("itemGrp1") int itemGrp1);

    @POST("getItemsByCatIdForGateSale")
    Call<ArrayList<Item>> getItemsByCategory(@Query("itemGrp1") int itemGrp1);


    @POST("gatesale/saveGateSaleBill")
    Call<ErrorMessage> saveBill(@Body GateSaleBillHeader gateSaleBillHeader);

    @POST("gatesale/gateBillHeaderAndDetails")
    Call<ArrayList<BillHeaderListData>> getBillData(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("isApproved") int isApproved, @Query("approvedUserId") int approvedUserId, @Query("amtIsCollected") int amtIsCollected, @Query("collectedUserId") int collectedUserId);

    @POST("gatesale/approveGetSaleBill")
    Call<ErrorMessage> approveOrRejectBill(@Query("billId") int billId, @Query("isApproved") int isApproved, @Query("approvedUserId") int approvedUserId);

    @GET("gatesale/getGateOtherSuppList")
    Call<SupplierListData> getSupplierList();

    @POST("gatesale/getGateOtherItemBySuppId")
    Call<OtherItemListData> getOtherItemList(@Query("suppId") int suppId);

    @GET("gatesale/gateBillDetailsAmtPending")
    Call<ArrayList<BillHeaderListData>> getCashCollectionList();

    @POST("gatesale/collectGetSaleAmt")
    Call<ErrorMessage> collectAmount(@Query("collectedUserId") int collectedUserId);

    @POST("gatesale/saveGateOtherSupplier")
    Call<ErrorMessage> saveSupplier(@Body OtherSupplierList otherSupplierList);

    @POST("gatesale/deleteGateOtherSupplier")
    Call<ErrorMessage> deleteSupplier(@Query("suppId") int supId);

    @GET("getItemsByIsAllowBday")
    Call<ArrayList<Item>> getItemsByBirthday();

    @POST("gatesale/saveGateOtherItem")
    Call<ErrorMessage> saveOtherItem(@Body OtherItemList otherItemList);

    @POST("gatesale/deleteGateOtherItem")
    Call<ErrorMessage> deleteOtherItem(@Query("itemId") int itemId);

    @POST("updateSeetingForPB")
    Call<ErrorMessage> updateSettingValue(@Query("settingValue") int settingValue, @Query("settingKey") String settingKey);

    @POST("getDeptSettingValue")
    Call<SettingsData> getSettingsValue(@Query("settingKeyList") ArrayList<String> key);

    @POST("gatesale/gateBillDetailAmtPending")
    Call<ArrayList<BillHeaderListData>> getCashCollectionListForApprover(@Query("isApproved") int isApproved, @Query("amtIsCollected") int amtIsCollected);

    @POST("gatesale/collectGetSaleAmtOfBill")
    Call<ErrorMessage> collectCash(@Query("collectedUserId") int collectedUserId, @Query("amtIsCollected") int amtIsCollected, @Query("billIds") ArrayList<Integer> billIds);

    @POST("gatesale/gateBillHeaderAndDetailsByInitiator")
    Call<ArrayList<BillHeaderListData>> getInitiatorBillData(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("isApproved") int isApproved, @Query("initiatorUserId") int initiatorUserId);

    @POST("gatesale/deleteGateSaleBill")
    Call<ErrorMessage> deleteRejectedBill(@Query("billIdList") ArrayList<Integer> billIdList);


    //Approver : 8793338336 --->123456
    //Collector : 8446464681 --->trisha
    //Initiator : 9579576696 ---> 6696
}
