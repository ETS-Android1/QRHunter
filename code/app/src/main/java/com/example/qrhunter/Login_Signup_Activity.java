package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login_Signup_Activity extends AppCompatActivity {

    Button btnLogin, btnLoginQR, btnSignup;
    TextInputLayout txtInputUsernameLogin, txtInputPasswordLogin;
    FirebaseFirestore db;
    boolean usernameExists;
    private static final String TAG = "DocSnippets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        this.getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

//        final CollectionReference collectionReference = db.collection("User");

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
        btnSignup = findViewById(R.id.btnSignup);
        txtInputUsernameLogin = findViewById(R.id.txtInputUsernameLogin);
        txtInputPasswordLogin = findViewById(R.id.txtInputPasswordLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Signup_Activity.this, signup.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inpUsername = txtInputUsernameLogin.getEditText().getText().toString().trim();
                String inpPass = txtInputPasswordLogin.getEditText().getText().toString();

                if (inpUsername.length() > 0) {
                    DocumentReference docRef = db.collection("User").document(inpUsername);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) { // if username exists
                                    usernameExists = true;
                                    Log.d(TAG, usernameExists + "DocumentSnapshot data: " + document.getData());
                                    // checking password
                                    String actualPass = document.get("password").toString();
                                    if (actualPass.equals(inpPass)) {
                                        //login
//                                    txtInputPasswordLogin.setError("Logged in!");
                                        Intent intent = new Intent(Login_Signup_Activity.this, QRScanActivity.class);
                                        intent.putExtra("USERNAME", inpUsername);
                                        startActivity(intent);
                                    } else {  // if input password does not match with username's password
                                        txtInputPasswordLogin.setError("Wrong username and/or password");
                                        txtInputUsernameLogin.setError("Wrong username and/or password");
                                    }
                                } else {  // if username does not exist
                                    usernameExists = false;
                                    txtInputUsernameLogin.setError("Wrong username and/or password");
                                    txtInputPasswordLogin.setError("Wrong username and/or password");
//                                Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    txtInputUsernameLogin.setError("Wrong username and/or password");
                    txtInputPasswordLogin.setError("Wrong username and/or password");
                }
            }
        });

        btnLoginQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Toast.makeText(Login_Signup_Activity.this, "CLICKED", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login_Signup_Activity.this, loginByQRScan.class);
                startActivity(intent);
            }

        });

    }



}