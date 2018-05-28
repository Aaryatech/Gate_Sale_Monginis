package com.ats.gate_sale_monginis.bean;

/**
 * Created by MIRACLEINFOTAINMENT on 10/01/18.
 */

public class LoginData {

    private String message;
    private Integer userId;
    private String userName;
    private Integer userType;
    private String mobileNumber;
    private String emailId;
    private String password;
    private Integer empId;
    private Integer delStatus;
    private Boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        return "LoginData{" +
                "message='" + message + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", empId=" + empId +
                ", delStatus=" + delStatus +
                ", error=" + error +
                '}';
    }
}
