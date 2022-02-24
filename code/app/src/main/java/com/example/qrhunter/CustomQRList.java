package com.example.qrhunter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomQRList  extends ArrayAdapter<OtherQRCodes>{
    private ArrayList<OtherQRCodes> otherCodes;
    private Context context;

    public CustomQRList(Context context, ArrayList<OtherQRCodes> otherCodes) {
        super(context, 0, otherCodes);
        this.otherCodes= otherCodes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        OtherQRCodes codes = otherCodes.get(position);
        TextView location = view.findViewById(R.id.location_text);
        TextView scanners = view.findViewById(R.id.scanners_text);
        TextView date= view.findViewById(R.id.date_text);
        TextView points= view.findViewById(R.id.points_text);
        location.setText(codes.getLocation());
        scanners.setText(String.valueOf(codes.getNumScanners()));
        date.setText(String.valueOf( codes.getDate()));
        points.setText(String.valueOf(codes.getPoints()));
        return view;
    }

}
