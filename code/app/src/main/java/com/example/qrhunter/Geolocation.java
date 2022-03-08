package com.example.qrhunter;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Geolocation {
    Float latitude;
    Float longitude;
    ArrayList<Float> coordinate = new ArrayList<Float>();
    public static final double oneMileLat = 0.0144927536231884;
    public static final double oneMileLon = 0.0181818181818182;
    public Geolocation(Float latitude, Float longitude) {
        coordinate.add(latitude);
        coordinate.add(longitude);
    }
    /**
     * This is method queries all nearby locations from firebase and passes them to a listener
     * @param center
     * the location the querier is currently at
     * @param zoom
     * The amount of zoom (determines the breadth of the query)
     * @param  geolistener
     * the listener to return results to
     */
    public static void firestoreQueryNearbyLocations (LatLng center, Float zoom, GeolocationListener geolistener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        double centerLat = (double)center.latitude;
        double centerLng = (double)center.longitude;
        double distance = 100/zoom;

        double lowerLat = centerLat - (oneMileLat * distance);
        double lowerLon = centerLng - (oneMileLon * distance);

        double greaterLat = centerLat + (oneMileLat * distance);
        double greaterLon = centerLng + (oneMileLon * distance);
        if(lowerLat < -90) {
            lowerLat = -90;
        }
        if(lowerLon < -180) {
            lowerLat = -180;
        }
        if(greaterLat > 90) {
            lowerLat = 90;
        }
        if(greaterLon > 180) {
            greaterLon = 180;
        }
        GeoPoint lesserGeopoint = new GeoPoint(lowerLat, lowerLon);
        GeoPoint greaterGeopoint =  new GeoPoint(greaterLat, greaterLon);

        CollectionReference docRef =  db.collection("location");

        docRef.whereGreaterThan("location", lesserGeopoint).whereLessThan("location", greaterGeopoint)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                for (QueryDocumentSnapshot document : task.getResult()) {
                    HashMap hashmaplocation = (HashMap) document.getData();
                    DocumentReference referenceToScan = (DocumentReference) hashmaplocation.get("scan");
                    if(referenceToScan == null) { continue; }
                    referenceToScan.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            HashMap hashmapScan = (HashMap) task.getResult().getData();
                            DocumentReference referenceToCode = (DocumentReference) hashmapScan.get("qrcode");
                            referenceToCode.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    HashMap hashMapCode = (HashMap) task.getResult().getData();
                                    geolistener.processLocation(hashmaplocation, task.getResult());
                                }
                            });
                        }

                    });

                }
            }

        });
    }
}
