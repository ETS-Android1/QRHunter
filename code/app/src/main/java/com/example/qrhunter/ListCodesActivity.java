package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Refrences: https://www.youtube.com/watch?v=NhiUTjm2BrE
public class ListCodesActivity extends BaseNavigatableActivity implements AdapterView.OnItemClickListener {

    ListView QRCode;
    ArrayAdapter<QRCode> codeAdapter;
    ArrayList<QRCode> dummyQRlist = new ArrayList<>();
    //final String TAG = "Sample";
    FirebaseFirestore db;
    ArrayList<String> qrCodeRef = new ArrayList<>();
    ArrayList<Double> score = new ArrayList<>();


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activitylist_main;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        //final CollectionReference collectionReference = db.collection("Score");
        QRCode = findViewById(R.id.qr_list);


        //Backend firestore
        db.collection("qrcodes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        QRCode myCode = new QRCode(document.getReference(),  (String) document.getId(), (ArrayList<DocumentReference>) document.getData().get("scans"), (DocumentReference)
                                document.getData().get("createdBy"), null);
                        dummyQRlist.add(myCode);
                    }
                    codeAdapter = new CustomQRList(ListCodesActivity.this, dummyQRlist);
                    QRCode.setAdapter(codeAdapter);

                } else {
                    Log.d("ListActivity", "Error getting documents: ", task.getException());
                }
            }
        });

        //now once the user clicks on the listed item
        QRCode.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //use this to get info of the clicked item
        // adapterView.getItemAtPosition(i);
        Intent intent = new Intent(ListCodesActivity.this, UserQRInfoActivity.class);

        startActivity(intent);
    }
}