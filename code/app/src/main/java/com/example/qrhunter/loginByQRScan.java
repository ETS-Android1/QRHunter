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
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

public class loginByQRScan extends AppCompatActivity {

    Intent intent;
    CodeScannerView scannerView;
    private CodeScanner mCodeScanner;
    FirebaseFirestore db;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_qrscan);
        this.getSupportActionBar().hide();

        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        scannerView = findViewById(R.id.scanner_view);

        try {
//            Toast.makeText(QRScanActivity.this, , Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            else {
                Toast.makeText(loginByQRScan.this, "HERERER", Toast.LENGTH_LONG).show();
                setup();
                scannerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCodeScanner.startPreview();
                    }
                });
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("ERROR IN QRSCANE", msg);
        }
    }

    private void setup() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sha256hex = DigestUtils.sha256Hex(result.toString());
                        String res = result.toString();
                        LatLng location = null;
                        Toast.makeText(loginByQRScan.this, result.getText(), Toast.LENGTH_SHORT).show();
//                        QRCode.uploadQRCode(user, sha256hex, QRScanActivity.this, db);
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                setup();
                mCodeScanner.startPreview();
        }
    }

}