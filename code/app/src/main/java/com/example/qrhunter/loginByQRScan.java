package com.example.qrhunter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

public class loginByQRScan extends AppCompatActivity {

    Intent intent;
    CodeScannerView scannerView;
    private CodeScanner mCodeScanner;
    FirebaseFirestore db;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 50;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_qrscan);
        this.getSupportActionBar().hide();

        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        scannerView = findViewById(R.id.scanner_view_login);

        try {
            Toast.makeText(loginByQRScan.this, "Hehehehe", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                setup();
                Toast.makeText(loginByQRScan.this, "HERERER", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("ERROR IN QRSCANE", msg);
        }
    }

    private void setup() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view_login);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sha256hex = DigestUtils.sha256Hex(result.toString());
                        String res = result.toString();
                        LatLng location = null;
//                        Toast.makeText(loginByQRScan.this, result.getText(), Toast.LENGTH_SHORT).show();
//                        boolean hashFound = matchScannedUsername(result.getText());
                        CollectionReference cr = db.collection("User");
                        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean matchFound = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        Toast.makeText(loginByQRScan.this, document.getId() + ": " + document.getData(), Toast.LENGTH_LONG).show();
                                        if (result.getText().equals(document.get("uniqueLoginHash"))) {
                                            // login successfully
                                            matchFound = true;
                                            saveData(document.getId());
                                            Intent intent = new Intent(loginByQRScan.this, QRScanActivity.class);
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                    if (!matchFound) {
                                        Toast.makeText(loginByQRScan.this, "No matching hash found in database", Toast.LENGTH_SHORT).show();
                                        loginByQRScan.this.recreate();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
//                        QRCode.uploadQRCode(user, sha256hex, QRScanActivity.this, db);
                    }
                });
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


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA:
//                setup();
//                mCodeScanner.startPreview();
//        }
//    }



}