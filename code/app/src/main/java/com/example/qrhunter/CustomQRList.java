package com.example.qrhunter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomQRList  extends ArrayAdapter<QRCode>{
    private ArrayList<QRCode> qrCodes;
    private Context context;

    public CustomQRList(Context context, ArrayList<QRCode> qrCodes) {
        super(context, 0, qrCodes);
        this.qrCodes= qrCodes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        QRCode codes = qrCodes.get(position);
        TextView qrRef = view.findViewById(R.id.location_text);
        TextView scanners = view.findViewById(R.id.scanners_text);
        //TextView date = view.findViewById(R.id.date_text);
        TextView points= view.findViewById(R.id.points_text);
        TextView playerName = view.findViewById(R.id.player_text);
        qrRef.setText("QR code "+codes.getUniqueHash());
        scanners.setText("Scanners: "+String.valueOf(codes.getNumScans()));
        //date.setText("Time Created: "+String.valueOf( codes.getTime()));
        playerName.setText("Player: "+ String.valueOf(codes.getPlayer()));
        points.setText("Points: "+String.valueOf(codes.getScore()));
        return view;
    }

}
