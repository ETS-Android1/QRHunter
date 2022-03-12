package com.example.qrhunter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestQRcode {
    private DocumentReference mockReference = Mockito.mock(DocumentReference.class);
    DocumentReference userReference = Mockito.mock(DocumentReference.class);
    String testHash = "testHash";
    Map<String, Object> data = new HashMap<>();
    public void initHashMap(){
            Long tsLong=System.currentTimeMillis()/1000;
            data.put("score",ScoringSystem.calculateScore(testHash));
            data.put("timeCreated",tsLong);
            data.put("createdBy",userReference);
            data.put("scans",new ArrayList<DocumentReference>());
    }
    ListensToQrUpload l = Mockito.mock(ListensToQrUpload.class);
    private QRCode myQrCode = new QRCode(mockReference, testHash,null, userReference, l );
    /**
     * @test tests the qr code is searched for before proceeding with an upload
     */
    @Test
    public void test_qr_code_upload() {
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);
        when(mockReference.get()).thenReturn(mockTask);
        Map<String, Object> data = new HashMap<>();
        String hash= "abba";
        myQrCode.uploadQRCode();
        verify(mockReference, times(1)).get();
    }

    @Test
    public void test_on_complete_method_when_qr_code_exists() {
        initHashMap();
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);
        Task mockTask2 = Mockito.mock(Task.class);
        DocumentSnapshot mockDoc = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDoc);
        when(mockDoc.exists()).thenReturn(true);
        when(mockReference.set(this.data)).thenReturn(mockTask2);
        myQrCode.onCompleteListener.onComplete(mockTask);
        verify(mockReference, times(0)).set(this.data);
        verify(l, times(1)).onQrUpload(myQrCode.getQrCodeRef());
    }

    @Test
    public void test_on_complete_method_when_qr_code_does_not_exist() {
        initHashMap();
        Task<DocumentSnapshot> mockTask = Mockito.mock(Task.class);
        DocumentSnapshot mockDoc = Mockito.mock(DocumentSnapshot.class);
        Task mockTask2 = Mockito.mock(Task.class);
        when(mockTask.getResult()).thenReturn(mockDoc);
        when(mockDoc.exists()).thenReturn(false);
        when(mockTask2.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(mockTask2);
        when(mockReference.set(this.data)).thenReturn(mockTask2);
        myQrCode.onCompleteListener.onComplete(mockTask);
        verify(mockReference, times(1)).set(this.data);
    }


}
