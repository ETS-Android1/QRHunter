package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ProfileQRInfoActivity extends AppCompatActivity {
    ListView seenList;
    ArrayList<Profile> seenDataList;
    ArrayAdapter<Profile> seenAdapter;
    Button back;

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