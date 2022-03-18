// This activity will display information about a given qr code that a player has scanned as well as comments

package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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
             * This button deletes the following QR code from the qrcode firebase collection
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
                String user = "test1";
                String uniqueQR = "fnefnioerfnoiFEFSEFfesfs";
                HashMap<String, String> comment = new HashMap<>();
                if (commentData != "") {
                    commentDataList.add(new Comment(user, commentData));
                    comment.put("Comment Data", commentData);
                    comment.put("User", user);

                    commentAdapter.notifyDataSetChanged();
                    addComment.setText("");
                }
                collectionReference.document(uniqueQR).set(comment);
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * This sets the new centre of the map to the selected place
             *
             * @param value used for querying database
             * @param error exception for error functionality
             */
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                commentDataList.clear();
                // add each of the comments to the commentData list from the database
                for(QueryDocumentSnapshot doc: value)
                {
                    String user = (String) doc.getData().get("User");
                    String commentData = (String) doc.getData().get("Comment Data");
                    commentDataList.add(new Comment(user, commentData));
                }
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
            }
        });

        commentAdapter = new CommentListAdapter(this, commentDataList);
        commentList.setAdapter(commentAdapter);
    }
}