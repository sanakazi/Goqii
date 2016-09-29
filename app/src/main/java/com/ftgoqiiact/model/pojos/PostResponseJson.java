package com.ftgoqiiact.model.pojos;

/**
 * Created by Fiticket on 30/12/15.
 */
public class PostResponseJson {
    private int id;
    private String message;
    private String customerId;
    private String customerContact;
    private boolean isTrial;
    private String statusMsg;
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isTrial() {
        return isTrial;
    }

    public void setIsTrial(boolean isTrial) {
        this.isTrial = isTrial;
    }
}
