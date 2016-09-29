package com.ftgoqiiact.model.pojos;

/**
 * Created by Fiticket on 20/01/16.
 */
public class BookRequestJson {
    private long activityId; // booking Id
    private int customerId;
    private String enrollmentDate; //yyyy-MM-dd HH:mm
    private String partnerName; //yyyy-MM-dd HH:mm

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
