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
    FirebaseFirestore firebaseDB;
    private  int highest, lowest, total;
    private ArrayList<String> allnames;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

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
    /*
    public void fetching(){
        firebaseDB = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseDB.collection("qrcodes");
        allqrcodes = new ArrayList<>();
        //obtain list of QRcodes
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                allqrcodes.clear();
                for (QueryDocumentSnapshot doc: value){
                    QRCode q = doc.toObject(QRCode.class);
                    allqrcodes.add(q);
                }
            }
        });
    }
    public void sorting(){
        ArrayList<String> user =new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        for (QRCode q: allqrcodes){
            user.add(q.getPlayer());
            scores.add(q.getScore());
        }
        //sorting descending order
        //if name = profilename then
        Collections.sort(scores, Collections.reverseOrder());
    }
    /*
     */
//Step1: Get currents users all qrcode save all qrcode to array list
    //Step2: Use each qrcode in the arraylist to get its worth
       //     step3: then sort by score...

    /*public void fetchQR(){
        firebaseDB = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseDB.collection("User");
        allnames = new ArrayList<>();
        //obtain the list of profiles
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: value) {
                    allnames.clear();
                    String s = doc.toString();
                    allnames.add(s);
                }
                }
        });

    }*/

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
