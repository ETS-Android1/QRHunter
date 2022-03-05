package com.example.qrhunter;

public class ScoringSystem {
    Database firebaseDB;
    public ScoringSystem() {}

    public Float calculateScore(QRCode qrcode) {
        Float score = 0f;
        //TODO
        return score;
    }

    public QRCode getGlobalHighestScoringQRCode() {
        QRCode highestScoringCode;
        //TODO
        return highestScoringCode;
    }

    public QRCode getHighestScoringCodeForUser(User u) {
        //TODO
    }

    public QRCode getLowestScoringCodeForUser(User u) {
        //TODO
    }

}
