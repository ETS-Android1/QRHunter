package com.example.qrhunter;
// https://stackoverflow.com/questions/8800919/how-to-generate-a-qr-code-for-an-android-application
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfile extends BaseNavigatableActivity implements AdapterView.OnItemClickListener {

    TextView ProfileName, Total, Scanned, Highest, Lowest;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ListView QRCode;
    ArrayList<QRCode> codes = new ArrayList<>();
    ArrayAdapter<QRCode> adapter;
    DocumentReference user;
    private Bitmap myMap;
    private String message;
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

    /**
     *
     * @return the current bitmap
     */
    public Bitmap getMap() {
        return myMap;
    }

    /**
     *
     * @return the current message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param content the string to make into a code
     */
    public void writeQrCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            myMap = bmp;


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_player_profile;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.profile;
    }

    /**
     * gets a login token for a user
     */
    public void get_individual_token() {
        String  uniqueId = "QRHUNTERTOKEN:" + UUID.randomUUID().toString();
        firestore.collection("User").whereEqualTo("uniqueLoginHash", uniqueId).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    get_individual_token();
                } else {
                    user.update("uniqueLoginHash", uniqueId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PlayerProfile.this,"token generated",Toast.LENGTH_SHORT).show();
                            writeQrCode(uniqueId);
                            message = "Login QRCODE(Do not share):";
                            DisplayCodeFragment dialog = DisplayCodeFragment.newInstance("Login QRCODE(Do not share):");
                            dialog.show(PlayerProfile.this.getSupportFragmentManager(), "DIALOG");
                        }
                    });

                }
            }
        });
    }

    /**
     * gets a status code for the user
     * @param view parent view
     */
    public void generate_status(View view){
        if  (view.getId()==R.id.generate_status){
            String  uniqueId = "QRHUNTERSTATUS:" + loadData();
            writeQrCode(uniqueId);
            message = "Status QRCODE:";
            DisplayCodeFragment dialog = DisplayCodeFragment.newInstance(message);
            dialog.show(PlayerProfile.this.getSupportFragmentManager(), "DIALOG");
        }
    }

    /**
     * gets a token code for the user
     * @param view parent view
     */
    public void generate_token(View view){
        if  (view.getId()==R.id.generate_button){
            get_individual_token();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QRCode = findViewById(R.id.list_of_codes);
        ProfileName = findViewById(R.id.name);
        Total = findViewById(R.id.total);
        Scanned = findViewById(R.id.scan);
        Highest = findViewById(R.id.highest);
        Lowest = findViewById(R.id.lowest);

        getData();

    }

    /**
     * gets the data of user from the database
     */
    public void getData(){
        firestore.collection("User").document(loadData()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                user = value.getReference();
                Map<String, Object> userData = value.getData();
                try {
                    ArrayList<DocumentReference> codeRefs = (ArrayList<DocumentReference>) userData.get("codes");
                    for (DocumentReference docRef : codeRefs) {
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                DocumentSnapshot document = task.getResult();
                                QRCode myCode = new QRCode(document.getReference(), (String) document.getId(), (ArrayList<DocumentReference>) document.getData().get("scanners")
                                        , (ArrayList<DocumentReference>) document.getData().get("scans"), (DocumentReference)
                                        document.getData().get("createdBy"), null);
                                codes.add(myCode);
                                double sum  = 0;
                                for(com.example.qrhunter.QRCode x : codes){
                                    sum += x.getScore();
                                }
                                Total.setText("Total: "+sum);
                                Collections.sort(codes, new Comparator<com.example.qrhunter.QRCode>() {
                                    @Override
                                    public int compare(com.example.qrhunter.QRCode qrCode, com.example.qrhunter.QRCode t1) {
                                        return Double.compare(t1.getScore(), qrCode.getScore());
                                    }
                                });
                                Scanned.setText("scanned: " + codes.size());
                                Lowest.setText("lowest: " +codes.get(codes.size()-1).getScore().toString());
                                Highest.setText("highest: "+codes.get(0).getScore().toString());
                                adapter = new CustomQRList(PlayerProfile.this, codes);;
                                QRCode.setAdapter(adapter);
                            }
                        });
                    }
                } catch(Exception e) {
                    String msg = e.getMessage();
                }
                ProfileName.setText(userData.get("username").toString());
                Total.setText("total: "+userData.get("worth").toString());

                Scanned.setText("scanned: " + codes.size());
                adapter = new CustomQRList(PlayerProfile.this, codes);;
                QRCode.setAdapter(adapter);
                QRCode.setOnItemClickListener(PlayerProfile.this);
            }
        });

    }

    /**
     * Use this method to get info of the clicked item and swicthes to the UserQRInfoActivity intent
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //use this to get info of the clicked item
        // adapterView.getItemAtPosition(i);
        Intent intent = new Intent(PlayerProfile.this, UserQRInfoActivity.class);
        QRCode code = codes.get(i);
        intent.putExtra("score", code.getScore());
        intent.putExtra("scans", code.getNumScans());
        intent.putExtra("hash", code.getUniqueHash());
        ArrayList<String> ids = new ArrayList<>();
        for (DocumentReference el : code.scanners) {
            ids.add(el.getId());
        }
        intent.putExtra("scanners", ids);
        startActivity(intent);
    }

    public String loadData() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString("USERNAME-key", "default-empty-string");
    }



}
