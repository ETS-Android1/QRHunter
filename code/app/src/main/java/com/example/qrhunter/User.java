package com.example.qrhunter;

import java.util.ArrayList;

/**
 * Represents the user object when logged in
 */
public class User {
    String username, password, email, phoneNum;
    String uniqueQRHash;
    ArrayList<QRCode> scannedQRCodes;
    Float totalScore;
    Float highest, lowest, worth;

    /**
     * Creates the user object when a new user signs up
     * @param username
     * String representing the unique username
     * @param password
     * String representing the password
     * @param email
     * String representing the email
     * @param phoneNum
     * String representing the phone number
     */
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

    /**
     * gets the user name
     * @return user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the user name
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * gets the password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * gets the email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the phone number
     * @return phone number
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * gets the unique hash
     * @return the unique hash
     */
    public String getUniqueQRHash() {
        return uniqueQRHash;
    }

    /**
     * gets the scanned codes
     * @return scanned codes
     */
    public ArrayList<QRCode> getScannedQRCodes() {
        return scannedQRCodes;
    }

    /**
     * gets highest code
     * @return highest code
     */
    public Float getHighest() {
        return highest;
    }

    /**
     * gets the lowest code
     * @return lowest code
     */
    public Float getLowest() {
        return lowest;
    }

    /**
     * gets the worth
     * @return worth
     */
    public Float getWorth() {
        return worth;
    }

}