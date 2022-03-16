package com.example.qrhunter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanCompleteDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanCompleteDialog extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "location_enabled";
    private static final String ARG_PARAM2 = "score";
    private static final String ARG_PARAM3 = "complete_listener";
    private boolean location_enabled = false;
    private OnCompleteListener l;
    private Double score = 0.0;

    public ScanCompleteDialog() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location_enabled Whether the location was enabled.
     * @param score The points scored.
     * @return A new instance of fragment ScanCompleteDialog.
     */
    public static ScanCompleteDialog newInstance(Double score, boolean location_enabled) {
        ScanCompleteDialog fragment = new ScanCompleteDialog();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM2,score);
        args.putBoolean(ARG_PARAM1, location_enabled);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

    }
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri uri;
    private ActivityResultLauncher<Uri> mGetContent =  registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("images/"+uri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(uri);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            task.isSuccessful();

                        }
                    });
                    // do what you need with the uri here ...
                }
            });
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You scored " + Double.valueOf(this.getArguments().getDouble(ARG_PARAM2, 0.0)).toString() + "\nLocation was" + (this.getArguments().getBoolean(ARG_PARAM1) ? "" : " not") + " recorded")
                    .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // START THE CAMERA
                            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                            File file = new File(ScanCompleteDialog.this.getContext().getExternalFilesDir(null),  mDateFormat.format(new Date()) + ".jpg");
                            uri = FileProvider.getUriForFile(ScanCompleteDialog.this.requireContext().getApplicationContext(),
                                    BuildConfig.APPLICATION_ID + ".provider", file);
                            ((QRScanActivity) getActivity()).setUri(uri);
                            mGetContent.launch(uri);
                        }
                    })
                    .setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_complete_dialog, container, false);
    }
}