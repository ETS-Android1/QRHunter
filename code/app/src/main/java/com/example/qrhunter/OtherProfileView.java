package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OtherProfileView extends BaseNavigatableActivity implements AdapterView.OnItemClickListener {

    TextView ProfileName, Total, Scanned, Highest, Lowest;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ListView QRCode;
    ArrayList<QRCode> codes = new ArrayList<>();
    ArrayAdapter<QRCode> adapter;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";




    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_other_profile_view;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QRCode = findViewById(R.id.list_of_codes);
        ProfileName = findViewById(R.id.name);
        Total = findViewById(R.id.total);
        Scanned = findViewById(R.id.scan);
        Highest = findViewById(R.id.highest);
        Lowest = findViewById(R.id.lowest);

        getData();

    }
    /**
     * gets the data of user from the database
     */
    public void getData(){
        firestore.collection("User").document(getIntent().getStringExtra("leaderBoardUserNameIntent")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Map<String, Object> data = value.getData();
                try {
                    ArrayList<DocumentReference> codeRefs = (ArrayList<DocumentReference>) data.get("codes");
                    for (DocumentReference docRef : codeRefs) {
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                DocumentSnapshot document = task.getResult();
                                QRCode myCode = new QRCode(document.getReference(), (String) document.getId(), (ArrayList<DocumentReference>) document.getData().get("scanners")
                                        , (ArrayList<DocumentReference>) document.getData().get("scans"), (DocumentReference)
                                        document.getData().get("createdBy"), null);
                                codes.add(myCode);

                                double sum = 0;
                                for (com.example.qrhunter.QRCode x: codes){
                                    sum += x.getScore();
                                }
                                Total.setText("total: " + sum);
                                Collections.sort(codes, new Comparator<com.example.qrhunter.QRCode>() {
                                    @Override
                                    public int compare(com.example.qrhunter.QRCode qrCode, com.example.qrhunter.QRCode t1) {
                                        return Double.compare(t1.getScore(), qrCode.getScore());
                                    }
                                });

                                Scanned.setText("scanned: " + codes.size());
                                Lowest.setText("lowest: " +codes.get(codes.size()-1).getScore().toString());
                                Highest.setText("highest: "+codes.get(0).getScore().toString());
                                adapter = new CustomQRList(OtherProfileView.this, codes);;
                                QRCode.setAdapter(adapter);
                            }
                        });
                    }
                } catch(Exception e) {
                    String msg = e.getMessage();
                }
                ProfileName.setText("username: "+data.get("username").toString());
                Total.setText("total: "+data.get("worth").toString());
                Scanned.setText("scanned: " + codes.size());
                adapter = new CustomQRList(OtherProfileView.this, codes);;
                QRCode.setAdapter(adapter);
                QRCode.setOnItemClickListener(OtherProfileView.this);

            }
        });

    }

    /**
     * Use this method to get info of the clicked item and swicthes to the UserQRInfoActivity intent
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(OtherProfileView.this, UserQRInfoActivity.class);
        QRCode code = codes.get(i);
        intent.putExtra("score", code.getScore());
        intent.putExtra("scans", code.getNumScans());
        intent.putExtra("hash", code.getUniqueHash());
        ArrayList<String> ids = new ArrayList<>();
        for (DocumentReference el : code.scanners) {
            ids.add(el.getId());
        }
        intent.putExtra("scanners", ids);
        startActivity(intent);
    }
}
