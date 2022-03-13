package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LeaderBoard extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<LeaderBoardHolder> leaderBoardHolders = new ArrayList<>();
    LeaderBoardAdapter leaderBoardAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        getFireStoreContent();

    }

    public void getFireStoreContent(){
        firestore.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    Map<String, Object> map = snapshot.getData();
                    for(String key : map.keySet()){
                        if(key.equals("username")){
                            leaderBoardHolders.add(new LeaderBoardHolder(map.get("username").toString(),
                                    map.get("worth").toString()));
                            /*map.get("username").toString() +", "+map.get("worth").toString());
                            string[] arr = list.split(",");*/
                        }
                    }
                }
                Collections.sort(leaderBoardHolders, Collections.reverseOrder());
                initRecycleView();
            }
        });
    }

    private void initRecycleView(){
        recyclerView = findViewById(R.id.leaderBoardRecycleView);
        leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardHolders, this);
        recyclerView.setAdapter(leaderBoardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}