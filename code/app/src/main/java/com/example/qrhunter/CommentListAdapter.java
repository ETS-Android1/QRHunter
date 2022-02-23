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

public class CommentListAdapter extends ArrayAdapter<Comment> {
    ArrayList<Comment> comments;
    Context context;

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_content, parent, false);
        }

        Comment comment = comments.get(position);
        TextView commentUser = view.findViewById(R.id.playerId);
        TextView commentContent = view.findViewById(R.id.comment);

        commentUser.setText(comment.getUser());
        commentContent.setText(comment.getComment());
        return view;
    }
}
