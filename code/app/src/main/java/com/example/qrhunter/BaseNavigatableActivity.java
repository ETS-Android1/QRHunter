package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
// https://stackoverflow.com/questions/8821240/android-how-to-create-my-own-activity-and-extend-it

/**
 * This class handles the navigation logic
 */
public abstract class BaseNavigatableActivity extends AppCompatActivity {
    protected abstract int getLayoutResourceId();
    protected abstract int getSelectedItemId();

    @Override
    protected void onRestart() {
        super.onRestart();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        MenuItem activeItem = bottomNav.getMenu().findItem(getSelectedItemId());
        activeItem.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        MenuItem activeItem = bottomNav.getMenu().findItem(getSelectedItemId());
        activeItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
        MenuItem activeItem = bottomNav.getMenu().findItem(getSelectedItemId());
        activeItem.setChecked(true);
        /**
         * The method that is called when a nav item is clicked in this activity
         * @param item
         * The item that gets selected
         */
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.search) {
                Intent intent = new Intent(BaseNavigatableActivity.this, MapActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.camera) {
                Intent intent = new Intent(BaseNavigatableActivity.this, QRScanActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.qrcode) {
                Intent intent = new Intent(BaseNavigatableActivity.this, ListCodesActivity.class);
                startActivity(intent);
            }else if (item.getItemId() == R.id.profile) {
                Intent intent = new Intent(BaseNavigatableActivity.this, PlayerProfile.class);
                startActivity(intent);
            }
            return true;
        });
    }
}