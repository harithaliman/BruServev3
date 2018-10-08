package com.example.harithaliman.bruservev3;

public class Complaint {
    private String userId;
    private String complaintId;
    public String complaintTitle;
    private String complaintDescription;

    private String dateOfEncounter;
    private String imageAddress;

    private String currentStatus;

    private String category;

    private String siteLocation;

    private double complaintValue;

    private long timestamp;

    private String phoneNumber;


    public Complaint() {}

    public Complaint(String userId, String complaintId, String complaintTitle, String complaintDescription, String dateOfEncounter, String imageAddress, String currentStatus, String category, String siteLocation, double complaintValue, long timestamp, String phoneNumber) {
        this.userId = userId;
        this.complaintId = complaintId;
        this.complaintTitle = complaintTitle;
        this.complaintDescription = complaintDescription;
//            this.latitude = latitude;
//            this.longitude = longitude;
        this.dateOfEncounter = dateOfEncounter;
        this.imageAddress = imageAddress;
        this.currentStatus = currentStatus;
        this.category = category;
        this.siteLocation = siteLocation;
        this.complaintValue = complaintValue;
        this.timestamp = timestamp;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public String getDateOfEncounter() {
        return dateOfEncounter;
    }

    public void setDateOfEncounter(String dateOfEncounter) {
        this.dateOfEncounter = dateOfEncounter;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public double getComplaintValue() {
        return complaintValue;
    }

    public void setComplaintValue(double complaintValue) {
        this.complaintValue = complaintValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
