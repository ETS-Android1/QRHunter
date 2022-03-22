package com.example.qrhunter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;
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

/**
 * This class is presented to allow the user to scan a qrcode
 */
public class QRScanActivity extends BaseNavigatableActivity implements ListensToQrUpload {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    // TO DO REPLACE WITH Real username
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    // TODO fix with logged in user
    private DocumentReference user;
    private DocumentReference scanRef;
    private DocumentReference locationRef;
    private LatLng currentLocation = null;
    private Snackbar onUpload;
    private Snackbar onComplete;
    private Snackbar onFail;
    private Snackbar onImage;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 50;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";
    private String USERNAME;
    private QRCode myCode;
    private String viewProfile = "";

    public String getViewProfile () {
      return viewProfile;
    };

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
            USERNAME = loadData();
            // TODO Replace with logged in user
            Toast.makeText(QRScanActivity.this, "Welcome user: " + USERNAME, Toast.LENGTH_LONG).show();
            user = db.collection("User").document(USERNAME);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            else {
                setup();
            }
            onImage = Snackbar.make(findViewById(R.id.bottom_nav), R.string.MESSAGE_ON_SCAN, BaseTransientBottomBar.LENGTH_LONG);
            onUpload = Snackbar.make(findViewById(R.id.bottom_nav), R.string.MESSAGE_ON_SCAN, BaseTransientBottomBar.LENGTH_LONG);
            onComplete = Snackbar.make(findViewById(R.id.bottom_nav), R.string.MESSAGE_ON_UPLOAD, BaseTransientBottomBar.LENGTH_LONG);
            onFail = Snackbar.make(findViewById(R.id.bottom_nav), R.string.MESSAGE_ON_FAIL, BaseTransientBottomBar.LENGTH_LONG);
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e("ERROR IN QRSCANE", msg);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLastLocation();
    }

    public  void checkLastLocation() {
        if (ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }
    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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
    /**
     * loads USERNAME from SharedPreferences
     * @return username used to login
     */
    public String loadData() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString("USERNAME-key", "default-empty-string");
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
                        if(result.toString().contains("QRHUNTERSTATUS:")) {
                            String profileName = result.toString().replace("QRHUNTERSTATUS:","");
                            db.collection("User").document(profileName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(!documentSnapshot.exists()) {
                                        Toast.makeText(QRScanActivity.this, "Could not find user", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent intent;
                                    if (USERNAME.equals(profileName)) {
                                        intent = new Intent(QRScanActivity.this, PlayerProfile.class);
                                        startActivity(intent);
                                    } else {
                                        String sha256hex = DigestUtils.sha256Hex(result.toString());
                                        String res = result.toString();
                                        LatLng location = null;
                                        Toast.makeText(QRScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                                        onUpload.show();
                                        viewProfile = profileName;
                                        myCode = new QRCode(db.collection("qrcodes").document(sha256hex), sha256hex, null, user, QRScanActivity.this);
                                        //QRCode myCode = new QRCode(db.collection("qrcodes").document(sha256hex), sha256hex, user, QRScanActivity.this);
                                        myCode.uploadQRCode();

                                        //intent = new Intent(QRScanActivity.this, OtherProfileView.class);
                                        //intent.putExtra("leaderBoardUserNameIntent", profileName);
                                    }
                                }
                            });
                        } else {
                            String sha256hex = DigestUtils.sha256Hex(result.toString());
                            String res = result.toString();
                            LatLng location = null;
                            Toast.makeText(QRScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                            viewProfile = "";
                            onUpload.show();
                            myCode = new QRCode(db.collection("qrcodes").document(sha256hex), sha256hex, null, user, QRScanActivity.this);
                            //QRCode myCode = new QRCode(db.collection("qrcodes").document(sha256hex), sha256hex, user, QRScanActivity.this);
                            myCode.uploadQRCode();
                        }
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
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                setup();
                mCodeScanner.startPreview();
                checkLastLocation();
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // premission granted
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        getLastLocation();
                    }
                }
            }
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

    public void setUri (Uri uri) {
        this.uri = uri;
    }
    @Override
    public void onQrUploadFail() {
        Log.e("ERROR", "BIGERROR");
        onUpload.dismiss();
        onFail.show();
    }
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageRef =  storage.getReferenceFromUrl("gs://qrhunter.appspot.com");
        StorageReference riversRef = storageRef.child("images/"+USERNAME+uri.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(uri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("image",uri.toString());
                            QRScanActivity.this.myCode.getQrCodeRef().update(data);
                            scanRef.update(data);
                            Uri downloadUrl = uri;
                            //Do what you want with the url
                        }
                    });
                    onImage.show();
                    onImage.setText("Image uploaded");
                } else {
                    onImage.show();
                    onImage.setText("Image upload failed");
                }

            }
        });
    }
    /**
     * called on a successful scan upload
     */
    private void onSuccessfulUpload() {
        onUpload.dismiss();
        onComplete.show();
        if (myCode != null) {
            ScanCompleteDialog dialog = ScanCompleteDialog.newInstance( myCode.getScore(), currentLocation != null);
            dialog.show(this.getSupportFragmentManager(), "DIALOG");
        }
    }


    @Override
    public void onQrUpload(DocumentReference qrCode) {
        locationRef = db.collection("location").document();
        scanRef = db.collection("scans").document();
        if (this.currentLocation != null) {
            Geolocation.uploadGeolocationForScan(locationRef, this.currentLocation, "", scanRef, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Scan.uploadScan(scanRef, locationRef, qrCode, user, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("location",locationRef);
                                qrCode.update(data);
                                QRScanActivity.this.onSuccessfulUpload();
                            }
                        });
                    } else {
                        //TODO on scan upload fail
                        onUpload.dismiss();
                        onFail.show();
                    }
                }
            });
        } else {
            Scan.uploadScan(scanRef, null, qrCode, user, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        QRScanActivity.this.onSuccessfulUpload();
                    } else {
                        //TODO on scan upload fail
                        onUpload.dismiss();
                        onFail.show();
                    }
                }
            });
        }
    }
}