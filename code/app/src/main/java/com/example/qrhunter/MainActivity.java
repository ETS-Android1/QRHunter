package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button userQRInfo;
    Button profileQRInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userQRInfo = findViewById(R.id.userQRInfo);
        profileQRInfo = findViewById(R.id.profileQRInfo);

        userQRInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserQRInfoActivity.class);
                startActivity(intent);
            }
        });

        profileQRInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileQRInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}