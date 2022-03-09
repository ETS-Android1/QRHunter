package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
// https://github.com/yuriy-budiyev/code-scanner

/**
 * @interface ListensToQrUpload
 * has the functions required to process a qr upload
 */
interface ListensToQrUpload {
    /**
     * is called when the upload is unsuccessful
     */
    public void onQrUploadFail();
    /**
     * is called when the upload is successful
     * @param qrCode reference to the document to be uploaded
     */
    public void onQrUpload(DocumentReference qrCode);
}

public class QRScanActivity extends BaseNavigatableActivity implements  ListensToQrUpload {
    // TO DO REPLACE WITH Real username
    FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    // TODO fix with logged in user
    private DocumentReference user;
    private  LatLng currentLocation = null;
    private static final String myUserName = "odawg";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 50;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_qrscan;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.camera;
    }

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            db = FirebaseFirestore.getInstance();
            // TODO Replace with logged in user
            user = db.collection("User").document("zJDWDHbGJkpMQdqE5zs2");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            else {
                setup();
            }
        } catch(Exception e) {
            String msg=  e.getMessage();
            Log.e("ERROR IN QRSCANE", msg);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(QRScanActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = new LatLng(location.getLatitude(),
                                        location.getLongitude());

                            }
                        }
                    });
            }

    }

    /**
     * sets up the qrcode scanner
     */

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
                        Toast.makeText(QRScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        QRCode.uploadQRCode(user, sha256hex, QRScanActivity.this, db);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    /**
     * This is called when the permissions for the application are changed
     * @param requestCode
     * The code that corresponds to a permission change
     * @param permissions
     * the changed permissions
     * @param grantResults
     * whether anything was granted
     */
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
    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner != null) mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if(mCodeScanner != null) mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onQrUploadFail() {
        String msg = "BLOUE";
        Log.e("ERROR", "BIGERROR");
    }

    /**
     * called on a successful scan upload
     */
    private void onSuccessfulUpload() {

    }

    @Override
    public void onQrUpload(DocumentReference qrCode) {
        DocumentReference currentLocation = db.collection("location").document();
        DocumentReference currentScan = db.collection("scans").document();
        if (this.currentLocation != null) {
            Geolocation.uploadGeolocationForScan(currentLocation, this.currentLocation, "", currentScan, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Scan.uploadScan(currentScan, currentLocation, qrCode, user, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                QRScanActivity.this.onSuccessfulUpload();
                            }
                        });
                    } else {
                        //TODO on scan upload fail
                        String msg = "BLOUE";
                    }
                }
            });
        } else {
            Scan.uploadScan(currentScan, null, qrCode, user, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        QRScanActivity.this.onSuccessfulUpload();
                    } else {
                        //TODO on scan upload fail
                        String msg = "BLOUE";
                    }
                }
            });
        }
    }
}