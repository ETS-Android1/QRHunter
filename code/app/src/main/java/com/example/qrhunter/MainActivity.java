package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnLoginQR, btnSignup;
    TextInputLayout txtInputUsernameLogin, txtInputPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
        btnSignup = findViewById(R.id.btnSignup);
        txtInputUsernameLogin = findViewById(R.id.txtInputUsernameLogin);
        txtInputPasswordLogin = findViewById(R.id.txtInputPasswordLogin);

        btnSignup.setOnClickListener(view -> {
            new Signup_Fragment().show(getSupportFragmentManager(), "SIGNUP_FRAGMENT");
        });

    }
}