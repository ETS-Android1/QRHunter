package com.example.qrhunter;

import java.util.Date;

public class OtherQRCodes {
    private String location;
    private Date date;
    private int numScanners;
    private int points;
    //private QRCode code;

    public OtherQRCodes(String location, Date date, int numScanners, int points) {
        this.location = location;
        this.date = date;
        this.numScanners = numScanners;
        this.points = points;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public int getNumScanners() {
        return numScanners;
    }

    public int getPoints() {
        return points;
    }
}
