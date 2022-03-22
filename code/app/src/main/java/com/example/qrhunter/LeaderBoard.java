package com.example.qrhunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LeaderBoard extends BaseNavigatableActivity implements LeaderBoardAdapter.OnItemListener {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<LeaderBoardHolder> leaderBoardHolders = new ArrayList<>();
    LeaderBoardAdapter leaderBoardAdapter;
    RecyclerView recyclerView;
    private TextView rank;
    private static final String TAG = "LeaderBoard";
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_leader_board;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.leaderboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layouttLayoutResourceId();.activity_leader_board);
        rank = findViewById(R.id.leaderBoardTextViewRankByScore);
        getFireStoreContent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_item , menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                leaderBoardAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

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
                for(int i = 0; i<leaderBoardHolders.size(); i++){
                    leaderBoardHolders.get(i).setUserRank(i+1);
                }
                for(int i = 0; i < leaderBoardHolders.size(); i++){
                    if(leaderBoardHolders.get(i).getUserName().equals(userNameLoad())){
                        rank.setText("USER RANK BY SCORE: "+leaderBoardHolders.get(i).getUserRank());
                    }
                }
                initRecycleView();
            }
        });
    }

    private void initRecycleView(){
        recyclerView = findViewById(R.id.leaderBoardRecycleView);
        leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardHolders, this,this);
        recyclerView.setAdapter(leaderBoardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    public void OnItemClick(int position) {
        Log.d(TAG, "OnItemClick: clicked");
        String s= leaderBoardHolders.get(position).getUserName();
        Intent intent = new Intent(this, OtherProfileView.class);
        intent.putExtra("leaderBoardUserNameIntent",s);
        startActivity(intent);
    }

    public String userNameLoad() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString("USERNAME-key", "default-empty-string");
    }

}