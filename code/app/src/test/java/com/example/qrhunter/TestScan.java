package com.example.qrhunter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class TestScan {
    /**
     * @test tests the correct arguments are being uploaded to database
     * and that the onCompleteListener is being called
     */
    @Test
    public void test_scan_upload() {
        DocumentReference mockReference = Mockito.mock(DocumentReference.class);
        DocumentReference locationReference = Mockito.mock(DocumentReference.class);
        DocumentReference qrCodeReference = Mockito.mock(DocumentReference.class);
        DocumentReference userReference = Mockito.mock(DocumentReference.class);
        Task mockTask = Mockito.mock(Task.class);
        when(mockReference.set(any(Map.class))).thenReturn(mockTask);
        Map<String, Object> data = new HashMap<>();
        Long tsLong = System.currentTimeMillis()/1000;
        data.put("createdAt", tsLong);
        data.put("user",  userReference);
        data.put("qrCode", qrCodeReference);
        data.put("location",locationReference);
        OnCompleteListener<Void> c = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        };
        Scan.uploadScan(mockReference, locationReference, qrCodeReference, userReference, c);
        verify(mockReference, times(1)).set(eq(data));
        verify(mockTask, times(1)).addOnCompleteListener(c);
    }
}
