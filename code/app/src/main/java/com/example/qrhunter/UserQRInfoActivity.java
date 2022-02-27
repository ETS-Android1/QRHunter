package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserQRInfoActivity extends BaseNavigatableActivity {
    ListView commentList;
    ArrayList<Comment> commentDataList;
    ArrayAdapter<Comment> commentAdapter;
    ImageButton back;
    Button delete;
    EditText addComment;
    ImageButton sendComment;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user_qrinfo;
    }

    @Override
    protected int getSelectedItemId() {
        return R.id.profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commentList = findViewById(R.id.commentList);
        commentDataList = new ArrayList<>();

        commentDataList.add(new Comment("1", "AAAAAAA"));
        commentDataList.add(new Comment("2", "BBBBBBB"));
        commentDataList.add(new Comment("3", "fadspfoj"));


        back = findViewById(R.id.backQR);
        addComment = findViewById(R.id.addComment);
        sendComment = findViewById(R.id.sendComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addComment.getText().toString();
                if (text != "") {
                    commentDataList.add(new Comment("3", text));
                    commentAdapter.notifyDataSetChanged();
                    addComment.setText("");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        commentAdapter = new CommentListAdapter(this, commentDataList);
        commentList.setAdapter(commentAdapter);
    }
}