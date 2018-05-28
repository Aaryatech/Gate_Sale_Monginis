package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class GateSaleUserList {

    private Integer userId;
    private String userName;
    private Integer userType;
    private String mobileNumber;
    private String emailId;
    private String password;
    private Integer empId;
    private String empName;
    private Integer delStatus;
    private String token;

    public GateSaleUserList(Integer userId, String userName, Integer userType, String mobileNumber, String emailId, String password, Integer empId, String empName, Integer delStatus, String token) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.password = password;
        this.empId = empId;
        this.empName = empName;
        this.delStatus = delStatus;
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GateSaleUserList{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", delStatus=" + delStatus +
                ", token='" + token + '\'' +
                '}';
    }
}
