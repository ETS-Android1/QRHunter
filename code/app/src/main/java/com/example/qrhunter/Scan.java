package com.example.qrhunter;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * This class models a qrcode scan
 */
public class Scan {
    private DateTime time;
    private Geolocation place;
    private ImageFile imageFile;

    public Scan() {}

    /**
     * A function to upload a scan of a qr code to firestore
     * @param scan a reference to the scan to be uploaded
     * @param location a reference to the location of the scan
     * @param qrCode a reference to the qrcode that was scanned
     * @param user a reference to the user that scanned the code
     * @param c a on complete listener
     */
    public static void uploadScan(DocumentReference scan, @Nullable  DocumentReference location, DocumentReference qrCode, DocumentReference user, OnCompleteListener<Void> c) {
        Map<String, Object> data = new HashMap<>();
        Long tsLong = System.currentTimeMillis()/1000;
        data.put("createdAt", tsLong);
        data.put("user",  user);
        data.put("qrCode", qrCode);
        qrCode.update("scans", FieldValue.arrayUnion(scan));
        if( location != null) {
            data.put("location", location);
        }
        scan.set(data).addOnCompleteListener(c);
    }
}
