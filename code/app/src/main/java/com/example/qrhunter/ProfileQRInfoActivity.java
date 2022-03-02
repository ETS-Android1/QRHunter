package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ProfileQRInfoActivity extends BaseNavigatableActivity {
    ListView seenList;
    ArrayList<Profile> seenDataList;
    ArrayAdapter<Profile> seenAdapter;
    ImageButton back;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile_qrinfo;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qrinfo);

        seenList = findViewById(R.id.seenList);
        seenDataList = new ArrayList<>();

        seenDataList.add(new Profile("user1"));
        seenDataList.add(new Profile("user3"));

        back = findViewById(R.id.backProfile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        seenAdapter = new SeenListAdapter(this, seenDataList);
        seenList.setAdapter(seenAdapter);
    }
}