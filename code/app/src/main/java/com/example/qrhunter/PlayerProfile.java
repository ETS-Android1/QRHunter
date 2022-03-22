package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfile extends BaseNavigatableActivity {

    TextView ProfileName, Total, Scanned, Highest, Lowest, RankOfTotal, RankOfScanned, RankOfHighest;
    RecyclerView recyclerView;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> codes = new ArrayList<>();
    PlayerProfileAdapter adapter;
    DocumentReference user;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_player_profile;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.profile;
    }

    public void get_individual_token() {
        String  uniqueId = "QRHUNTERTOKEN:" + UUID.randomUUID().toString();
        firestore.collection("User").whereEqualTo("uniqueLoginHash", uniqueId).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    get_individual_token();
                } else {

                    user.update("uniqueLoginHash", uniqueId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PlayerProfile.this,"token generated",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void generate_token(View view){
        if  (view.getId()==R.id.generate_button){
            get_individual_token();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    /**
     * gets the data of user from the database
     */
    public void getData(){
        firestore.collection("User").document(loadData()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                user = value.getReference();
                Map<String, Object> data = value.getData();
                try {
                    ArrayList<DocumentReference> codeRefs = (ArrayList<DocumentReference>) data.get("codes");
                    for (DocumentReference docRef : codeRefs) {
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                codes.add(task.getResult().getId());
                                Scanned.setText("scanned: " + codes.size());
                                initRecycleView();
                            }
                        });
                    }
                } catch(Exception e) {
                    String msg = e.getMessage();
                }
                ProfileName.setText("username: "+data.get("username").toString());
                Total.setText("total: "+data.get("worth").toString());
                Highest.setText("highest: "+data.get("highest").toString());
                Lowest.setText("lowest: "+data.get("lowest").toString());
                Scanned.setText("scanned: " + codes.size());
                initRecycleView();

            }
        });

    }

    /**
     * this function showing qr codes in recycler view
     */
    private void initRecycleView(){
        recyclerView = findViewById(R.id.recyclerViewPlayerProfile);
        adapter = new PlayerProfileAdapter(codes, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public String loadData() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString("USERNAME-key", "default-empty-string");
    }


}
