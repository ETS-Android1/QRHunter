package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
//Refrences: https://www.youtube.com/watch?v=NhiUTjm2BrE
public class ListCodesActivity extends BaseNavigatableActivity implements AdapterView.OnItemClickListener {

    ListView QRCode;
    ArrayAdapter<OtherQRCodes> codeAdapter;
    ArrayList<OtherQRCodes> dummyQRlist;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activitylist_main;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QRCode = findViewById(R.id.qr_list);

        String[] locations = {"Edmonton, AB", "Vancouver, BC"};
        Date[] dates = {new Date(), new Date()};
        int[] scanners = {10, 15};
        int[] scores = {1000, 500};

        dummyQRlist = new ArrayList<>();

        for (int i = 0; i < locations.length; i++) {
            dummyQRlist.add((new OtherQRCodes(locations[i], dates[i], scanners[i],scores[i])));
        }

        codeAdapter = new CustomQRList(this, dummyQRlist);
        QRCode.setAdapter(codeAdapter);

        //now once the user clicks on the listed item
        QRCode.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //use this to get info of the clicked item
        // adapterView.getItemAtPosition(i);
        Intent intent = new Intent(ListCodesActivity.this, UserQRInfoActivity.class);
        startActivity(intent);
    }
}