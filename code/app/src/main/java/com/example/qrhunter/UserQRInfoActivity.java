// This activity will display information about a given qr code that a player has scanned as well as comments

package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for displaying QR information from a selected user QR code
 */
public class UserQRInfoActivity extends BaseNavigatableActivity {
    private ListView commentList;
    private ArrayList<Comment> commentDataList;
    private ArrayAdapter<Comment> commentAdapter;
    private ListView seenList;
    private ArrayList<String> seenDataList;
    private ArrayAdapter<String> seenAdapter;
    private FirebaseFirestore  db = FirebaseFirestore.getInstance();
    final CollectionReference collectionUserReference = db.collection("User");
    ImageView scanned;

    public void name_clicked(View v) {
        TextView t = (TextView) v;
        String name = String.valueOf(t.getText());
        Intent intent = new Intent(this, OtherProfileView.class);
        intent.putExtra("leaderBoardUserNameIntent",name);
        startActivity(intent);
    }
    /**
     * This is called by the base activity to get the layout
     * @return returns the layout for this activity
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user_qrinfo;
    }

    /**
     * This is called by the base activity to get the selected item on create
     * @return returns item id corresponding to the activity
     */
    @Override
    protected int getSelectedItemId() {
        return R.id.qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commentList = findViewById(R.id.commentList);
        commentDataList = new ArrayList<>();
        ImageButton back;
        Button delete;
        EditText addComment;
        ImageButton sendComment;
        TextView loc;
        TextView date;
        TextView points;

        loc = findViewById(R.id.QRGeolocation);
        scanned = findViewById(R.id.QRScannedImage);
        points = findViewById(R.id.QRPoints);
        seenList = findViewById(R.id.seenList);

        String[] commentsInDB;
        seenDataList = new ArrayList<>();


        // finding the code
        Bundle extras = getIntent().getExtras();
        String hash = extras.getString("hash");
        Double score = extras.getDouble("score");
        int scans = extras.getInt("scans");

        points.setText(score.toString());


        // finding the user using local storage
        SharedPreferences sharedPref = getSharedPreferences("USERNAME-sharedPrefs", MODE_PRIVATE);
        String user = sharedPref.getString("USERNAME-key", null);


        /* from eclass
        at https://github.com/AbdulAli/FirestoreTutorialW22/
        by https://eclass.srv.ualberta.ca/mod/page/view.php?id=5825425 */
        final CollectionReference collectionReference = db.collection("comments");
        final CollectionReference collectionQrReference = db.collection("qrcodes");
        final String TAG = "UserQRInfoActivity";
        TextView locationText = (TextView) findViewById(R.id.QRGeolocation);
        collectionQrReference.document(hash).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData().containsKey("image")) {
                    new ImageFile(findViewById(R.id.qr_info_image))
                            .execute((String) documentSnapshot.getData().get("image"));
                }
                if (documentSnapshot.getData().containsKey("location")) {
                    DocumentReference location = (DocumentReference) documentSnapshot.getData().get("location");
                    location.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            GeoPoint thisLocation = documentSnapshot.getGeoPoint("location");
                            locationText.setText("Lat: " + thisLocation.getLatitude() + "\nLng: " + thisLocation.getLongitude());
                        }
                    });
                }
            }
        });

        back = findViewById(R.id.backQR);
        delete = findViewById(R.id.deleteQR);
        addComment = findViewById(R.id.addComment);
        sendComment = findViewById(R.id.sendComment);

        delete.setOnClickListener(new View.OnClickListener() {
            /**
             * This method deletes the following QR code from the database for the current user
             * @param view
             */
            @Override
            public void onClick(View view) {
                String user = sharedPref.getString("USERNAME-key", null);
                DocumentReference deleteCode =  collectionQrReference.document(hash);
                DocumentReference userRef = collectionUserReference.document(user);
                deleteCode.update("scanners", FieldValue.arrayRemove(userRef));
                userRef.update("codes", FieldValue.arrayRemove(deleteCode))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is succeeded

                                Log.d("deleted QR", "QRCode has been deleted successfully!");
                                Toast.makeText(getApplicationContext(),"QRCode has been deleted successfully!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d("deleted QR", "QRCode could not be deleted!" + e.toString());
                                Toast.makeText(getApplicationContext(),"QRCode could not be deleted!",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        sendComment.setOnClickListener(new View.OnClickListener() {
            /**
             * Allows a comment to be added to the database and the user qr code comments
             *
             * @param view The view used
             */
            @Override
            public void onClick(View view) {
                // we add to the database based on the user passed from the previous activity
                String commentData = addComment.getText().toString();
                if (commentData != "") {
                    commentDataList.add(new Comment("test2", commentData));

                    collectionReference.document(hash).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                collectionReference.document(hash).update("comments", FieldValue.arrayUnion(commentData));
                            } else {
                                List<String> newComments = new ArrayList<String>();
                                newComments.add(commentData);
                                CommentHelper newComment = new CommentHelper(newComments);
                                collectionReference.document(hash).set(newComment);
                            }
                        }
                    });

                    commentAdapter.notifyDataSetChanged();
                    addComment.setText("");
                }

            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * Here we update our data with database data from comments collection
             *
             * @param value used for querying database
             * @param error exception for error functionality
             */
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                commentDataList.clear();

                for(QueryDocumentSnapshot doc: value)
                {
                    if (doc.exists() && doc.getId().equals(hash)) {
                        CommentHelper commentHelper = doc.toObject(CommentHelper.class);
                        for (String comment: commentHelper.getComments()) {
                            commentDataList.add(new Comment("test2", comment));
                        }
                    }
                }

                // notify adapters of data update
                commentAdapter.notifyDataSetChanged();
            }
        });

        collectionUserReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * Here we update our data with database data from qrcodes collection
             *
             * @param value used for querying database
             * @param error exception for error functionality
             */
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                seenDataList.clear();

                for(QueryDocumentSnapshot doc: value)
                {
                    if (doc.exists() && doc.getId().equals(hash)) {
                        SeenHelper seenHelper = doc.toObject(SeenHelper.class);
                        if (seenHelper.getScanners() != null) {
                            for (DocumentReference seen: seenHelper.getScanners()) {
                                seen.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        final String seenName = task.getResult().getData().get("username").toString();
                                        seenHelper.getCreatedBy().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (seenName != task.getResult().getData().get("username").toString()) {
                                                    scanned.setImageResource(R.drawable.check);
                                                }
                                            }
                                        });
                                        // update data and notify adapters of data update
                                        seenDataList.add(seenName);
                                        seenAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            /**
             * Allows the user to return back to the previous activity(qr code list)
             *
             * @param view the view used
             */
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserQRInfoActivity.this, ListCodesActivity.class);
                startActivity(intent);
            }
        });

        commentAdapter = new CommentListAdapter(this, commentDataList);
        commentList.setAdapter(commentAdapter);
        seenAdapter = new ArrayAdapter<>(this, R.layout.seen_content, seenDataList);
        seenList.setAdapter(seenAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> scanners = getIntent().getExtras().getStringArrayList("scanners");
        for (String seen: scanners) {
            collectionUserReference.document( seen).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (!task.isSuccessful() || !task.getResult().exists()) {
                        return;
                    }
                    final String seenName = task.getResult().getData().get("username").toString();
                    if (seenName != task.getResult().getData().get("username").toString()) {
                        scanned.setImageResource(R.drawable.check);
                    }
                    // update data and notify adapters of data update
                    Integer index = seenDataList.indexOf(seenName);
                    if(index < 0) { seenDataList.add(seenName); }
                    seenAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}