package com.example.qrhunter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    private TextInputLayout txtInputUsername, txtInputPass, txtInputConfirmPass, txtInputEmail, txtInputPhone;
    private Button btnConfirm, btnCancel;
    FirebaseFirestore db;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = FirebaseFirestore.getInstance();
        CollectionReference cr = db.collection("User");

        txtInputUsername = findViewById(R.id.txtInputUsername);
        txtInputPass = findViewById(R.id.txtInputPassword);
        txtInputConfirmPass = findViewById(R.id.txtInputPasswordConfirm);
        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputPhone = findViewById(R.id.txtInputPhone);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

//        Log.d(TAG, "onCreate: " + username);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtInputUsername.getEditText().getText().toString().trim();
                String pass = txtInputPass.getEditText().getText().toString();
                String passConf = txtInputConfirmPass.getEditText().getText().toString();
                String email = txtInputEmail.getEditText().getText().toString().trim();
                String phone = txtInputPhone.getEditText().getText().toString().trim();

                boolean validPass, validPassConf, validEmail, validPhone;
                validEmail = email.length() > 0;
                if (!validEmail) { txtInputEmail.setError("Email cannot be empty"); } else { txtInputEmail.setErrorEnabled(false); }
                validPhone = phone.length() > 0;
                if (!validPhone) { txtInputPhone.setError("Phone number cannot be empty"); } else { txtInputPhone.setErrorEnabled(false); }

                validPass = pass.length() > 0;
                if (!validPass) { txtInputPass.setError("Password cannot be empty"); } else { txtInputPass.setErrorEnabled(false); }
                validPassConf = passConf.length() > 0 && pass.equals(passConf);
                if (!validPassConf) {
                    txtInputConfirmPass.setError("Passwords must match");
                    txtInputPass.setError("Passwords must match");
                } else {
                    txtInputConfirmPass.setErrorEnabled(false);
                    txtInputPass.setErrorEnabled(false);
                }

                if (username.length() > 0) {
                    DocumentReference docRef = cr.document(username);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    txtInputUsername.setError("Username already exists");
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    txtInputUsername.setErrorEnabled(false);
                                    if (validPass && validPassConf && validEmail && validPhone) {
                                        User u = new User(username, pass, email, phone);
                                        saveData(username);
                                        registerNewUser(u, cr);
                                        Log.d(TAG, "No such document");
                                    }
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    txtInputUsername.setError("Username cannot be empty");
                }
            }
        });
    }


    /**
     * Saves the logged in username to local storage for other activities to access
     * @param username
     * String object representing the username
     */
    public void saveData(String username) {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USERNAME-key", username);
        editor.apply();
    }

    /**
     * Registers a new user to "User" collection with the username as the key for the new document
     * And moves to QRScanActivity if successful
     * @param u
     * User object
     * @param cr
     * Collection reference to "Users" in the Firebase database
     */
    public void registerNewUser(User u, CollectionReference cr) {

        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("codes", u.getScannedQRCodes());
        newUserMap.put("highest", u.getHighest());
        newUserMap.put("lowest", u.getLowest());
        newUserMap.put("password", u.getPassword());
        newUserMap.put("username", u.getUsername());
        newUserMap.put("phoneNum", u.getPhoneNum());
        newUserMap.put("email", u.getEmail());
        newUserMap.put("uniqueLoginHash", u.getUniqueQRHash());
        newUserMap.put("worth", u.getWorth());

        cr.document(u.getUsername()).set(newUserMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(signup.this, u.getUsername() + " added successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(signup.this, QRScanActivity.class);
                intent.putExtra("USERNAME", u.getUsername());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup.this, "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
