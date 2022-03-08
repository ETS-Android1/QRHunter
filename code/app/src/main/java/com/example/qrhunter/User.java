package com.example.qrhunter;

import java.util.ArrayList;

public class User {
    String username, password, email;
    QRCode uniqueQRCode;
    ArrayList<QRCode> scannedQRCodes;
    Float totalScore;
    ScoringSystem ss;

    public User(String username, String password, String email, QRCode uniqueQRCode) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.uniqueQRCode = uniqueQRCode;
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
