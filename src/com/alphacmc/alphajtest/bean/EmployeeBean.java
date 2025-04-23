package com.alphacmc.alphajtest.bean;

public class EmployeeBean {
    // 社員ID
    private String employeeId;
    // 生年月日
    private String birthDate;
    // 性別 
    private String gender;
    // 入社年月日
    private String joinDate;
    // 郵便番号
    private String postalCode;
    // 住所
    private String address;
    // メールアドレス
    private String emailAddress;
    // 電話番号
    private String phoneNumber;

    public String getEmployeeId() {
        return employeeId;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getGender() {
        return gender;
    }
    public String getJoinDate() {
        return joinDate;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getAddress() {
        return address;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
