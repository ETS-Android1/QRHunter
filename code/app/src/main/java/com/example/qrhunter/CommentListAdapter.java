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

/**
 * This is an adapter for displaying comments on a qr code
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context context;

    /**
     * This is a constructor for the CommentListAdapter
     *
     * @param context initial context
     * @param comments initial comments to initialize list
     */
    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.comments = comments;
        this.context = context;
    }

    /**
     * This sets values for each element in the comment list
     *
     * @param position used for finding position of comment
     * @param convertView the new view that will be returned
     * @param parent used for setting the view
     * @return we get the new view with added comment data
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_content, parent, false);
        }
        // fill the list element with the username and the comment itself
        Comment comment = comments.get(position);
        TextView commentUser = view.findViewById(R.id.playerId);
        TextView commentContent = view.findViewById(R.id.comment);

        commentUser.setText(comment.getUser());
        commentContent.setText(comment.getComment());
        return view;
    }
}
