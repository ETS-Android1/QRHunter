package com.example.qrhunter;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @class QRCode
 * this class represents the qrcodes that are scanned
 */
public class QRCode {
    Float score;
    String uniqueHash;
    ArrayList<Scan> scans;


    public QRCode() {
        //TODO
    }

    /**
     * This is called when a qrcode is scanned and decoded
     * @param player
     * the logged in user the qrcode should be associated with
     * @param hash
     * the hash of the decoded qrcode
     * @param listener
     * an interface which implements upload and upload fail functions
     * @param db
     * access to the database
     */
    public static void uploadQRCode(DocumentReference player, String hash, ListensToQrUpload listener, FirebaseFirestore db) {
        CollectionReference c = db.collection("qrcodes");
        DocumentReference qrCodeRef = db.collection("qrcodes")
                .document(hash);
        qrCodeRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot d = task.getResult();
                        Map<String, Object> data = new HashMap<>();
                        Long tsLong = System.currentTimeMillis()/1000;
                        data.put("score", ScoringSystem.calculateScore(hash));
                        data.put("timeCreated", tsLong);
                        data.put("createdBy", player);
                        data.put("scans", new ArrayList<DocumentReference>());
                        if (!d.exists()) {
                            qrCodeRef.set(data).addOnSuccessListener(
                                (OnSuccessListener) -> {
                                    listener.onQrUpload(qrCodeRef);
                                }
                            ).addOnFailureListener((OnFailureListener) e -> {
                                listener.onQrUploadFail();
                            });
                        } else {
                            listener.onQrUpload(qrCodeRef);
                        }
                    }
                });
    }
}
