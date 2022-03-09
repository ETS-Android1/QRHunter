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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//Refrences: https://www.youtube.com/watch?v=NhiUTjm2BrE
public class ListCodesActivity extends BaseNavigatableActivity implements AdapterView.OnItemClickListener {

    ListView QRCode;
    ArrayAdapter<OtherQRCodes> codeAdapter;
    ArrayList<OtherQRCodes> dummyQRlist;
    final String TAG = "Sample";
    FirebaseFirestore db;


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

        /*String[] locations = {"Edmonton, AB", "Vancouver, BC"};
        Date[] dates = {new Date(), new Date()};
        int[] scanners = {10, 15};
        int[] scores = {1000, 500};

        dummyQRlist = new ArrayList<>();

        for (int i = 0; i < locations.length; i++) {
            dummyQRlist.add((new OtherQRCodes(locations[i], dates[i], scanners[i],scores[i])));
        }

        codeAdapter = new CustomQRList(this, dummyQRlist);
        QRCode.setAdapter(codeAdapter);*/

        //Backend firestore
        loadDatainListview();

        //now once the user clicks on the listed item
        QRCode.setOnItemClickListener(this);
    }

    //Refrences: https://www.geeksforgeeks.org/how-to-create-dynamic-listview-in-android-using-firebase-firestore/
    private void loadDatainListview() {
        // below line is use to get data from Firebase
        // firestore using collection in android.
        db.collection("Score").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are hiding
                            // our progress bar and adding our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                OtherQRCodes data = d.toObject(OtherQRCodes.class);

                                // after getting data from Firebase we are
                                // storing that data in our array list
                                dummyQRlist.add(data);
                            }
                            // after that we are passing our array list to our adapter class.
                            codeAdapter = new CustomQRList(ListCodesActivity.this, dummyQRlist);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            QRCode.setAdapter(codeAdapter);
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(ListCodesActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a toast message
                // when we get any error from Firebase.
                Toast.makeText(ListCodesActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //use this to get info of the clicked item
        // adapterView.getItemAtPosition(i);
        Intent intent = new Intent(ListCodesActivity.this, UserQRInfoActivity.class);
        startActivity(intent);
    }
}