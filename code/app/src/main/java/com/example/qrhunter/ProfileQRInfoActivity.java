// This activity is used to show who has seen a qr code

package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class is responsible for displaying who has seen a qr code
 */
public class ProfileQRInfoActivity extends BaseNavigatableActivity {
    private ListView seenList;
    private ArrayList<User> seenDataList;
    private ArrayAdapter<User> seenAdapter;

    /**
     * This is called by the base activity to get the layout
     * @return returns the layout for this activity
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile_qrinfo;
    }

    /**
     * This is called by the base activity to get the selected item on create
     * @return returns item id corresponding to the activity
     */
    @Override
    protected int getSelectedItemId() {
        return R.id.qrcode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_qrinfo);

        seenList = findViewById(R.id.seenList);
        seenDataList = new ArrayList<>();
        ImageButton back;


        back = findViewById(R.id.backProfile);

        back.setOnClickListener(new View.OnClickListener() {
            /**
             * Allows the user to return back to the previous activity(profile)
             *
             * @param view the view used
             */
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        seenAdapter = new SeenListAdapter(this, seenDataList);
        seenList.setAdapter(seenAdapter);
    }
}