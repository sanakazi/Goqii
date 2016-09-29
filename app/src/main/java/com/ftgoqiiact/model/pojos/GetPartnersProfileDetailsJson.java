package com.ftgoqiiact.model.pojos;

/**
 * Created by NiravShrimali on 6/21/2016.
 */

public class GetPartnersProfileDetailsJson {
//    GetPartnersProfileDetailsJson getPartnersProfileDetailsJson;
//
//    public GetPartnersProfileDetailsJson getGetPartnersProfileDetailsJson() {
//        return getPartnersProfileDetailsJson;
//    }
//
//    public void setGetPartnersProfileDetailsJson(GetPartnersProfileDetailsJson getPartnersProfileDetailsJson) {
//        this.getPartnersProfileDetailsJson = getPartnersProfileDetailsJson;
//    }

    public String contactNo;
    public String emailId;
    public  String firstName;
    public  String lastName;
    public  String packageEndDate;
    public String packageName;
    public  String pincode;

//    public GetPartnersProfileDetailsJson(String contactNo, String pincode, String packageEndDate, String firstName, String emailId, String lastName, String packageName) {
//        this.contactNo = contactNo;
//        this.pincode = pincode;
//        this.packageEndDate = packageEndDate;
//        this.firstName = firstName;
//        this.emailId = emailId;
//        this.lastName = lastName;
//        this.packageName = packageName;
//    }


    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPackageEndDate() {
        return packageEndDate;
    }

    public void setPackageEndDate(String packageEndDate) {
        this.packageEndDate = packageEndDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


}
