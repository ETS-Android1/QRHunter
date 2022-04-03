package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.spec.ECField;

import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseNavigatableActivity {
    private static final String SHARED_PREFS = "USERNAME-sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Button login = findViewById(R.id.login);
        String user = this.loadData();
        Intent intent;
        if (user.equals("default-empty-string")) {
            intent = new Intent(MainActivity.this, Login_Signup_Activity.class);
        } else {
            intent = new Intent(MainActivity.this, QRScanActivity.class);
        }
        startActivity(intent);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login_Signup_Activity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * This is called by the base activity to get the layout
     * @return returns the layout for this activity
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    /**
     * This is called by the base activity to get the selected item on create
     * @return returns item id corresponding to the activity
     */
    @Override
    protected int getSelectedItemId() {
        return R.id.camera;
    }

    /**
     * loads USERNAME from SharedPreferences
     * @return username used to login
     */
    public String loadData() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getString("USERNAME-key", "default-empty-string");
    }
}