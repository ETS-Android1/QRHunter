// This activity will display information about a given qr code that a player has scanned as well as comments

package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for displaying QR information from a selected user QR code
 */
public class UserQRInfoActivity extends BaseNavigatableActivity {
    private ListView commentList;
    private ArrayList<Comment> commentDataList;
    private ArrayAdapter<Comment> commentAdapter;
    private FirebaseFirestore db;

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
        ImageView scanned;
        TextView points;

        loc = findViewById(R.id.QRGeolocation);
        date = findViewById(R.id.QRDate);
        scanned = findViewById(R.id.QRScannedImage);
        points = findViewById(R.id.QRPoints);


        String[] commentsInDB;

        // finding the code
        Bundle extras = getIntent().getExtras();
        String hash = extras.getString("hash");
        Double score = extras.getDouble("score");
        int scans = extras.getInt("scans");

        points.setText(score.toString());
        if (scans > 1) {
            scanned.setImageResource(R.drawable.check);
        } else {
            scanned.setImageResource(R.drawable.not_check);
        }


        // finding the user using local storage
        SharedPreferences sharedPref = getSharedPreferences("USERNAME-sharedPrefs", MODE_PRIVATE);


        /* from eclass
        at https://github.com/AbdulAli/FirestoreTutorialW22/
        by https://eclass.srv.ualberta.ca/mod/page/view.php?id=5825425 */
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("comments");
        final CollectionReference collectionQRReference = db.collection("qrcodes");
        final String TAG = "UserQRInfoActivity";


        back = findViewById(R.id.backQR);
        delete = findViewById(R.id.deleteQR);
        addComment = findViewById(R.id.addComment);
        sendComment = findViewById(R.id.sendComment);

        delete.setOnClickListener(new View.OnClickListener() {
            /**
             * This method deletes the following QR code from the database and updates the list
             * @param view
             */
            @Override
            public void onClick(View view) {
                collectionQRReference
                        //how are we passing the QRCode in
                        .document()
                        .delete()
                        //.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is succeeded

                                Log.d(TAG, "QRCode has been deleted successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d(TAG, "QRCode could not be deleted!" + e.toString());
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
                String user = sharedPref.getString("USERNAME-key", null);
                if (commentData != "") {
                    commentDataList.add(new Comment(commentData));

                    collectionReference.document(hash).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                collectionReference.document(hash).update("comments", FieldValue.arrayUnion(commentData));
                            } else {
                                List<String> newComments = new ArrayList<String>();
                                newComments.add(commentData);;
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
             * Here we update our data with database data
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
                            commentDataList.add(new Comment(comment));
                        }
                    }
                }
                // add each of the comments to the commentData list from the database

                commentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
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
    }
}