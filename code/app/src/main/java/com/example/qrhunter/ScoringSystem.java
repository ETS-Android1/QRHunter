package com.example.qrhunter;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ScoringSystem {

    public ScoringSystem() {

    }
    // TODO implement more sophisticated scoring system
    /**
     * A function that calculates the score from a hash
     * @param hash
     * @return the score of the hash
     */
    public static Double calculateScore(String hash) {
        Double score = 0.0;
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
