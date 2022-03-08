package com.example.qrhunter;

import com.google.firebase.firestore.FirebaseFirestore;

public class ScoringSystem {
    FirebaseFirestore firebaseDB;
    public ScoringSystem() {}
    // TODO implement more sophisticated scoring system
    /**
     * A function that calculates the score from a hash
     * @param hash
     * @return the score of the hash
     */
    public static Float calculateScore(String hash) {
        Float score = 0f;
        for (char el : hash.toCharArray()) {
            score = score + el;
        }
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
