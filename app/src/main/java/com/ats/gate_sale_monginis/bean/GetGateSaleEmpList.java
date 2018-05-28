package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 11/01/18.
 */

public class GetGateSaleEmpList {

    private String message;
    private Integer empId;
    private String empName;
    private Integer deptId;
    private String deptName;
    private Integer isUsed;
    private Integer empType;
    private String empMobile;
    private String empDob;
    private String empDoj;
    private Integer empFamMemb;
    private Integer discId;
    private String discountHead;
    private Float discountPer;
    private Integer monthlyLimit;
    private Integer yearlyLimit;
    private Integer delStatus;
    private Boolean error;
    private int monthlyConsumed;
    private int yearlyConsumed;

    public GetGateSaleEmpList(Integer empId, String empName, Integer deptId, String deptName, Integer isUsed, Integer empType, String empMobile, String empDob, String empDoj, Integer empFamMemb, Integer discId, String discountHead, Float discountPer, Integer monthlyLimit, Integer yearlyLimit, Integer delStatus) {
        this.empId = empId;
        this.empName = empName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.isUsed = isUsed;
        this.empType = empType;
        this.empMobile = empMobile;
        this.empDob = empDob;
        this.empDoj = empDoj;
        this.empFamMemb = empFamMemb;
        this.discId = discId;
        this.discountHead = discountHead;
        this.discountPer = discountPer;
        this.monthlyLimit = monthlyLimit;
        this.yearlyLimit = yearlyLimit;
        this.delStatus = delStatus;
    }

    public GetGateSaleEmpList(Integer empId, String empName, Integer deptId, String deptName, Integer isUsed, Integer empType, String empMobile, String empDob, String empDoj, Integer empFamMemb, Integer discId, String discountHead, Float discountPer, Integer monthlyLimit, Integer yearlyLimit, Integer delStatus, int monthlyConsumed, int yearlyConsumed) {
        this.empId = empId;
        this.empName = empName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.isUsed = isUsed;
        this.empType = empType;
        this.empMobile = empMobile;
        this.empDob = empDob;
        this.empDoj = empDoj;
        this.empFamMemb = empFamMemb;
        this.discId = discId;
        this.discountHead = discountHead;
        this.discountPer = discountPer;
        this.monthlyLimit = monthlyLimit;
        this.yearlyLimit = yearlyLimit;
        this.delStatus = delStatus;
        this.monthlyConsumed = monthlyConsumed;
        this.yearlyConsumed = yearlyConsumed;
    }

    public int getMonthlyConsumed() {
        return monthlyConsumed;
    }

    public void setMonthlyConsumed(int monthlyConsumed) {
        this.monthlyConsumed = monthlyConsumed;
    }

    public int getYearlyConsumed() {
        return yearlyConsumed;
    }

    public void setYearlyConsumed(int yearlyConsumed) {
        this.yearlyConsumed = yearlyConsumed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getEmpType() {
        return empType;
    }

    public void setEmpType(Integer empType) {
        this.empType = empType;
    }

    public String getEmpMobile() {
        return empMobile;
    }

    public void setEmpMobile(String empMobile) {
        this.empMobile = empMobile;
    }

    public String getEmpDob() {
        return empDob;
    }

    public void setEmpDob(String empDob) {
        this.empDob = empDob;
    }

    public String getEmpDoj() {
        return empDoj;
    }

    public void setEmpDoj(String empDoj) {
        this.empDoj = empDoj;
    }

    public Integer getEmpFamMemb() {
        return empFamMemb;
    }

    public void setEmpFamMemb(Integer empFamMemb) {
        this.empFamMemb = empFamMemb;
    }

    public Integer getDiscId() {
        return discId;
    }

    public void setDiscId(Integer discId) {
        this.discId = discId;
    }

    public String getDiscountHead() {
        return discountHead;
    }

    public void setDiscountHead(String discountHead) {
        this.discountHead = discountHead;
    }

    public Float getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(Float discountPer) {
        this.discountPer = discountPer;
    }

    public Integer getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Integer monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public Integer getYearlyLimit() {
        return yearlyLimit;
    }

    public void setYearlyLimit(Integer yearlyLimit) {
        this.yearlyLimit = yearlyLimit;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GetGateSaleEmpList{" +
                "message='" + message + '\'' +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", isUsed=" + isUsed +
                ", empType=" + empType +
                ", empMobile='" + empMobile + '\'' +
                ", empDob='" + empDob + '\'' +
                ", empDoj='" + empDoj + '\'' +
                ", empFamMemb=" + empFamMemb +
                ", discId=" + discId +
                ", discountHead='" + discountHead + '\'' +
                ", discountPer=" + discountPer +
                ", monthlyLimit=" + monthlyLimit +
                ", yearlyLimit=" + yearlyLimit +
                ", delStatus=" + delStatus +
                ", error=" + error +
                ", monthlyConsumed=" + monthlyConsumed +
                ", yearlyConsumed=" + yearlyConsumed +
                '}';
    }
}
