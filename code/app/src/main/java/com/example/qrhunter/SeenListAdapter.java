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

public class SeenListAdapter extends ArrayAdapter<User> {
    ArrayList<User> users;
    Context context;

    public SeenListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.seen_content, parent, false);
        }

        User user = users.get(position);
        TextView seenUserName = view.findViewById(R.id.seenUser);

        seenUserName.setText(user.getUserName());
        return view;
    }
}
