package com.example.qrhunter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


/**
 * This Class tests the CustomQRList Class to check if the array adapter is working
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
        assertEquals(listSize+1,list.getCount());
    }
}
