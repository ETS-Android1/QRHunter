package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LoggedinProfile extends AppCompatActivity {

    TextView ProfileName, Total, Scanned, Highest, Lowest, RankOfTotal, RankOfScanned, RankOfHighest;
    RecyclerView recyclerView;
    ArrayList<String> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_profile);
        ProfileName = findViewById(R.id.name);
        Total = findViewById(R.id.total);
        Scanned = findViewById(R.id.scan);
        Highest = findViewById(R.id.highest);
        Lowest = findViewById(R.id.lowest);
        RankOfTotal = findViewById(R.id.rankPlayer1);
        RankOfScanned =  findViewById(R.id.rankPlayer2);
        RankOfHighest = findViewById(R.id.rankPlayer3);
        recyclerView = findViewById(R.id.recyclerView);
        int totalNum = 100;
        int scannedNum = 5;
        int highNum = 20;
        int lowNum = 4;

        Total.setText("Total points " + totalNum);
        Scanned.setText("Scanned " + scannedNum);
        Highest.setText("Highest "+ highNum);
        Lowest.setText("Lowest "+lowNum);
    }
}