package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This Class tests the CustomQRList control Class to check if the array adapter is working
 */
public class TestCustomQRList {

    private CustomQRList list;
    @Before
    public void createList() {
        list = new CustomQRList( null, new ArrayList<QRCode>());
    }

    @Test
    public void addCityTest(){
        int listSize = list.getCount();
        //change this to QR add testcase
        //list.addQR(new QRCode("Halifax", "NS"));
       DocumentReference mockReference = Mockito.mock(DocumentReference.class);
        DocumentReference userReference = Mockito.mock(DocumentReference.class);
        String testHash = "testHash";
        ListensToQrUpload l = Mockito.mock(ListensToQrUpload.class);
        list.addQR(new QRCode(mockReference, testHash,null, null, userReference, l ));
        assertEquals(listSize+1,list.getCount());
    }
}
