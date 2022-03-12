package com.example.qrhunter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class TestLocation {
    /**
     * @test tests the correct arguments are being uploaded to database
     * and that the onCompleteListener is being called
     */
    @Test
    public void test_location_upload() {
        DocumentReference scanReference = Mockito.mock(DocumentReference.class);
        DocumentReference locationReference = Mockito.mock(DocumentReference.class);
        LatLng myLatLng = new LatLng(0.0, 0.0);
        String address = "Hello";

        Task mockTask = Mockito.mock(Task.class);
        when(locationReference.set(any(Map.class))).thenReturn(mockTask);
        Map<String, Object> data = new HashMap<>();
        data.put("location", new GeoPoint(myLatLng.latitude, myLatLng.longitude));
        data.put("address",  address);
        data.put("scan", scanReference);
        OnCompleteListener<Void> c = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        };
        Geolocation.uploadGeolocationForScan(locationReference, myLatLng, address, scanReference, c);
        verify(locationReference, times(1)).set(eq(data));
        verify(mockTask, times(1)).addOnCompleteListener(c);
    }
}
