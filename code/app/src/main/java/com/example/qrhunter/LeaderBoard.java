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
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// the leaderboard activity displayed to users
public class LeaderBoard extends BaseNavigatableActivity implements LeaderBoardAdapter.OnItemListener {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList<LeaderBoardHolder> leaderBoardHolders = new ArrayList<>();
    LeaderBoardAdapter leaderBoardAdapter;
    RecyclerView recyclerView;
    private TextView rank;
    private static final String TAG = "LeaderBoard";
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";
    HashMap<DocumentReference, Double> myMapOfCodes = new HashMap<>();
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
        getFireStoreContent("TOTAL");

    }

    /**
     * the options menu creator
     * @param menu parent menu
     */
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

    /**
     * sort by highest score
     * @param view parent view
     */
    public void set_rank_highest_score(View view) {
        rank.setText("USER RANK BY SCORE: ");
        leaderBoardHolders.clear();
        getFireStoreContent("TOTAL");
    }

    /**
     * sort by highest code
     * @param view parent view
     */
    public void set_rank_highest_code(View view) {
        rank.setText("USER RANK BY HIGHEST CODE: ");
        leaderBoardHolders.clear();
        getFireStoreContent("HIGHEST");
    }

    /**
     * sort by number of codes scanned
     * @param view parent view
     */
    public void set_rank_number_codes_scanned(View view) {
        rank.setText("USER RANK BY NUMBER SCANS: ");
        leaderBoardHolders.clear();
        getFireStoreContent("AMOUNT");
    }
    /**
     * this is called to get the users from firestore
     * @param method what to sort by
     */
    public void getFireStoreContent(String method){
        firestore.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                final Integer[] sizeOfUsers = {value.size()};
                final Integer[] countOfUsers = {0};
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    Map<String, Object> map = snapshot.getData();
                    if(map.get("codes") == null || ((ArrayList<DocumentReference>) map.get("codes")).isEmpty() || method.equals("AMOUNT")) {
                        LeaderBoardHolder holder;
                        if(map.get("codes") == null) {
                            holder = new LeaderBoardHolder(map.get("username").toString(),
                                    String.valueOf(0));
                        } else {
                            holder = new LeaderBoardHolder(map.get("username").toString(),
                                    String.valueOf(((ArrayList<DocumentReference>) map.get("codes")).size()));
                        }
                        leaderBoardHolders.add(holder);
                        countOfUsers[0] = countOfUsers[0] + 1;
                        if (countOfUsers[0] == (sizeOfUsers[0])) {
                            sortUsers();
                        }
                        continue;
                    }
                    ArrayList<DocumentReference> myList = (ArrayList<DocumentReference>) map.get("codes");
                    final Integer[] count = { myList.size() };
                    final Integer[] count2 = { 0};
                    final Double[] score = {0.0};
                    final Double[] highestRanking = {0.0};
                    for(DocumentReference el : myList) {
                        if(myMapOfCodes.get(el) != null) {
                            if (highestRanking[0] < myMapOfCodes.get(el)) {
                                highestRanking[0] = myMapOfCodes.get(el);
                            }
                            score[0] = score[0] + myMapOfCodes.get(el);
                            count2[0] = count2[0] + 1;
                            if (count2[0] == count[0]) {
                                LeaderBoardHolder holder;
                                if (method.equals("TOTAL")) {
                                    holder = new LeaderBoardHolder(map.get("username").toString(),
                                            String.valueOf(score[0]));
                                } else {
                                    holder = new LeaderBoardHolder(map.get("username").toString(),
                                            String.valueOf(highestRanking[0]));
                                }
                                leaderBoardHolders.add(holder);
                                countOfUsers[0] = countOfUsers[0] + 1;
                                if (countOfUsers[0] == (sizeOfUsers[0])) {
                                    sortUsers();
                                }
                            }
                        } else {
                            el.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    try {
                                        Double scoreFromDoc = (Double) documentSnapshot.getData().get("score");
                                        if (highestRanking[0] < (scoreFromDoc)) {
                                            highestRanking[0] = myMapOfCodes.get(el);
                                        }
                                        score[0] = score[0] + scoreFromDoc;
                                        myMapOfCodes.put(el, scoreFromDoc);
                                        count2[0] = count2[0] + 1;
                                        if (count2[0] == count[0]) {
                                            LeaderBoardHolder holder;
                                            if (method.equals("TOTAL")) {
                                                holder = new LeaderBoardHolder(map.get("username").toString(),
                                                        String.valueOf(score[0]));
                                            } else {
                                                holder = new LeaderBoardHolder(map.get("username").toString(),
                                                        String.valueOf(highestRanking[0]));
                                            }
                                            leaderBoardHolders.add(holder);
                                            countOfUsers[0] = countOfUsers[0] + 1;
                                            if (countOfUsers[0] == (sizeOfUsers[0])) {
                                                sortUsers();
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /**
     * this is called to sort the leaderboard holders
     */
    private void sortUsers() {
        Collections.sort(leaderBoardHolders, Collections.reverseOrder());
        for(int i = 0; i<leaderBoardHolders.size(); i++){
            leaderBoardHolders.get(i).setUserRank(i+1);
        }
        for(int i = 0; i < leaderBoardHolders.size(); i++){
            if(leaderBoardHolders.get(i).getUserName().equals(userNameLoad())){
                rank.setText(rank.getText().toString() +leaderBoardHolders.get(i).getUserRank());
            }
        }
        initRecycleView();
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