package com.example.harithaliman.bruservev3.HistoryRecyclerView;

public class HistoryObject {

    private String complaintId;
    private String time;
    private String siteLocation;
    private String currentStatus;
    private String imageAddress;

    public HistoryObject(String complaintId, String time, String siteLocation, String currentStatus, String imageAddress) {
        this.complaintId = complaintId;
        this.time = time;
        this.siteLocation = siteLocation;
        this.currentStatus = currentStatus;
        this.imageAddress = imageAddress;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
