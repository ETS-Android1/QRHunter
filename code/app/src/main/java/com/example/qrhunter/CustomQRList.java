package com.example.qrhunter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
/**
 * This class creates an array adapter on the QRcode class to map to the ListCodesActivity
 */
public class CustomQRList  extends ArrayAdapter<QRCode>{
    private ArrayList<QRCode> qrCodes;
    private Context context;

    /**
     * This Method creates the CustomQRList addapter
     * @param context the activity that is being passed in
     * @param qrCodes the QRCodes Array list
     */
    public CustomQRList(Context context, ArrayList<QRCode> qrCodes) {
        super(context, 0, qrCodes);
        this.qrCodes= qrCodes;
        this.context = context;
    }


    /**
     * This method sets the listview textviews from content.xml as the QRCode Class Object values
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        QRCode codes = qrCodes.get(position);
        TextView qrRef = view.findViewById(R.id.qr_ref_text);
        TextView locationText = view.findViewById(R.id.location_text);
        TextView scanners = view.findViewById(R.id.scanners_text);
        //TextView date = view.findViewById(R.id.date_text);
        TextView points= view.findViewById(R.id.points_text);
        TextView playerName = view.findViewById(R.id.player_text);
        qrRef.setText("QR code "+codes.getUniqueHash());
        scanners.setText("Scanners: "+String.valueOf(codes.getNumScans()));
        //date.setText("Time Created: "+String.valueOf( codes.getTime()));
        playerName.setText("Created By: "+ String.valueOf(codes.getPlayer().getId()));
        points.setText("Points: "+String.valueOf(codes.getScore()));
        ImageView qrCodeImage = view.findViewById(R.id.qrcode_image);
        codes.getQrCodeRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData().containsKey("image")) {
                    new ImageFile(qrCodeImage)
                            .execute((String) documentSnapshot.getData().get("image"));
                }
                if (documentSnapshot.getData().containsKey("location")) {
                    DocumentReference location = (DocumentReference) documentSnapshot.getData().get("location");
                    location.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            GeoPoint thisLocation = documentSnapshot.getGeoPoint("location");
                            locationText.setText("Lat: " + thisLocation.getLatitude() + "\nLng: " + thisLocation.getLongitude());
                        }
                    });
                }
            }
        });
        return view;
    }


    /**
     * This method is for testing, it will get us the size of the list of QRCodes
     * @return the number of QRcodes that we have
     */
    public int getCount() {
        return qrCodes.size();
    }


    /**
     * This method will add a qrcodes object to the list
     * @param qrCode takes in QRcode to be added
     */
    public void addQR(QRCode qrCode) {
        qrCodes.add(qrCode);
        return;
    }

}
