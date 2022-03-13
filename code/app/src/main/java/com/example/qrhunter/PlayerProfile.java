package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PlayerProfile extends AppCompatActivity {

    TextView ProfileName, Total, Scanned, Highest, Lowest, RankOfTotal, RankOfScanned, RankOfHighest;
    RecyclerView recyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> codes;
    PlayerProfileAdapter adapter;
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

        getData();

    }

    public void getData(){
        //Chnage the name to user name in the .document
        firestore.collection("User").document("zJDWDHbGJkpMQdqE5zs2").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Map<String, Object> data = value.getData();
                codes = (ArrayList<String>) data.get("codes");
                ProfileName.setText("username: "+data.get("username").toString());
                Total.setText("total: "+data.get("worth").toString());
                Highest.setText("highest: "+data.get("highest").toString());
                Lowest.setText("lowest: "+data.get("lowest").toString());
                Scanned.setText("scanned: " + codes.size());
                initRecycleView();

            }
        });

    }
    private void initRecycleView(){
        recyclerView = findViewById(R.id.recyclerViewPlayerProfile);
        adapter = new PlayerProfileAdapter(codes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
