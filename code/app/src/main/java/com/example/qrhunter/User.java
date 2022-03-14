package com.example.qrhunter;

import java.util.ArrayList;

/**
 * This class models a user
 */
public class User {
    String username, password, email, phoneNum;
    String uniqueQRHash;
    ArrayList<QRCode> scannedQRCodes;
    Float totalScore;
    Float highest, lowest, worth;

    public User(String username, String password, String email, String phoneNum) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNum = phoneNum;
        this.uniqueQRHash = null;
        this.scannedQRCodes = new ArrayList<>();
        this.highest = 0f;
        this.lowest = 0f;
        this.worth = 0f;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUniqueQRHash() {
        return uniqueQRHash;
    }

    public void setUniqueQRHash(QRCode uniqueQRCode) {
        String hash = uniqueQRCode.getUniqueHash();
        this.uniqueQRHash = hash;
    }

    public ArrayList<QRCode> getScannedQRCodes() {
        return scannedQRCodes;
    }

    public void setScannedQRCodes(ArrayList<QRCode> scannedQRCodes) {
        this.scannedQRCodes = scannedQRCodes;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public Float getHighest() {
        return highest;
    }

    public void setHighest(Float highest) {
        this.highest = highest;
    }

    public Float getLowest() {
        return lowest;
    }

    public void setLowest(Float lowest) {
        this.lowest = lowest;
    }

    public Float getWorth() {
        return worth;
    }

    public void setWorth(Float worth) {
        this.worth = worth;
    }

    public boolean addQRCode(QRCode qrcode) {
        if (scannedQRCodes.add(qrcode)) return true;
        else return false;
    }

    public boolean addComment(String comment, QRCode qrcode) {
        //TODO
        return false;
    }
}