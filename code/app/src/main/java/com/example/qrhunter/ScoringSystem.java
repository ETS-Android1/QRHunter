package com.example.qrhunter;

import com.google.firebase.firestore.FirebaseFirestore;

public class ScoringSystem {
    FirebaseFirestore firebaseDB;
    public ScoringSystem() {}

    public Float calculateScore(QRCode qrcode) {
        Float score = 0f;
        //TODO
        return score;
    }

    public QRCode getGlobalHighestScoringQRCode() {
        QRCode highestScoringCode;
        //TODO
        return new QRCode();
    }

    public QRCode getHighestScoringCodeForUser(User u) {
        //TODO
        return new QRCode();
    }

    public QRCode getLowestScoringCodeForUser(User u) {
        //TODO
        return new QRCode();
    }

}
