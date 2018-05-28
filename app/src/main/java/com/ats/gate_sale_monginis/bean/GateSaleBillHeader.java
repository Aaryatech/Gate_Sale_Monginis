package com.ats.gate_sale_monginis.bean;

import java.util.List;

/**
 * Created by MIRACLEINFOTAINMENT on 12/01/18.
 */

public class GateSaleBillHeader {

    private int billId;
    private String billDate;
    private int category;
    private int isOther;
    private String custName;
    private int empId;
    private float discountPer;
    private float billAmt;
    private float discountAmt;
    private float roundOff;
    private float billGrantAmt;
    private int isApproved;
    private String approvedDate;
    private int approvedUserId;
    private int amtIsCollected;
    private String collectedDate;
    private int collectedUserId;
    private int isBillPrint;
    private int initiatorUserId;
    private int delStatus;
    List<GateSaleBillDetail> gateSaleBillDetailList;

    public GateSaleBillHeader() {
    }

    public GateSaleBillHeader(int billId, String billDate, int category, int isOther, String custName, int empId, float discountPer, float billAmt, float discountAmt, float roundOff, float billGrantAmt, int isApproved, String approvedDate, int approvedUserId, int amtIsCollected, String collectedDate, int collectedUserId, int isBillPrint, int initiatorUserId, int delStatus, List<GateSaleBillDetail> gateSaleBillDetailList) {
        this.billId = billId;
        this.billDate = billDate;
        this.category = category;
        this.isOther = isOther;
        this.custName = custName;
        this.empId = empId;
        this.discountPer = discountPer;
        this.billAmt = billAmt;
        this.discountAmt = discountAmt;
        this.roundOff = roundOff;
        this.billGrantAmt = billGrantAmt;
        this.isApproved = isApproved;
        this.approvedDate = approvedDate;
        this.approvedUserId = approvedUserId;
        this.amtIsCollected = amtIsCollected;
        this.collectedDate = collectedDate;
        this.collectedUserId = collectedUserId;
        this.isBillPrint = isBillPrint;
        this.initiatorUserId = initiatorUserId;
        this.delStatus = delStatus;
        this.gateSaleBillDetailList = gateSaleBillDetailList;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getIsOther() {
        return isOther;
    }

    public void setIsOther(int isOther) {
        this.isOther = isOther;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public float getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(float discountPer) {
        this.discountPer = discountPer;
    }

    public float getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(float billAmt) {
        this.billAmt = billAmt;
    }

    public float getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(float discountAmt) {
        this.discountAmt = discountAmt;
    }

    public float getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(float roundOff) {
        this.roundOff = roundOff;
    }

    public float getBillGrantAmt() {
        return billGrantAmt;
    }

    public void setBillGrantAmt(float billGrantAmt) {
        this.billGrantAmt = billGrantAmt;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public int getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(int approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public int getAmtIsCollected() {
        return amtIsCollected;
    }

    public void setAmtIsCollected(int amtIsCollected) {
        this.amtIsCollected = amtIsCollected;
    }

    public String getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(String collectedDate) {
        this.collectedDate = collectedDate;
    }

    public int getCollectedUserId() {
        return collectedUserId;
    }

    public void setCollectedUserId(int collectedUserId) {
        this.collectedUserId = collectedUserId;
    }

    public int getIsBillPrint() {
        return isBillPrint;
    }

    public void setIsBillPrint(int isBillPrint) {
        this.isBillPrint = isBillPrint;
    }

    public int getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(int initiatorUserId) {
        this.initiatorUserId = initiatorUserId;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public List<GateSaleBillDetail> getGateSaleBillDetailList() {
        return gateSaleBillDetailList;
    }

    public void setGateSaleBillDetailList(List<GateSaleBillDetail> gateSaleBillDetailList) {
        this.gateSaleBillDetailList = gateSaleBillDetailList;
    }

    @Override
    public String toString() {
        return "GateSaleBillHeader{" +
                "billId=" + billId +
                ", billDate='" + billDate + '\'' +
                ", category=" + category +
                ", isOther=" + isOther +
                ", custName='" + custName + '\'' +
                ", empId=" + empId +
                ", discountPer=" + discountPer +
                ", billAmt=" + billAmt +
                ", discountAmt=" + discountAmt +
                ", roundOff=" + roundOff +
                ", billGrantAmt=" + billGrantAmt +
                ", isApproved=" + isApproved +
                ", approvedDate='" + approvedDate + '\'' +
                ", approvedUserId=" + approvedUserId +
                ", amtIsCollected=" + amtIsCollected +
                ", collectedDate='" + collectedDate + '\'' +
                ", collectedUserId=" + collectedUserId +
                ", isBillPrint=" + isBillPrint +
                ", initiatorUserId=" + initiatorUserId +
                ", delStatus=" + delStatus +
                ", gateSaleBillDetailList=" + gateSaleBillDetailList +
                '}';
    }

}
