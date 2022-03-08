package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
// https://github.com/yuriy-budiyev/code-scanner
public class QRScanActivity extends BaseNavigatableActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 50;
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
                        Toast.makeText(QRScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
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
}