package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class Login_Signup_Activity extends AppCompatActivity implements Signup_Fragment.OnFragmentInteractionListener {

    Button btnLogin, btnLoginQR, btnSignup;
    TextInputLayout txtInputUsernameLogin, txtInputPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginQR = findViewById(R.id.btnLoginQR);
        btnSignup = findViewById(R.id.btnSignup);
        txtInputUsernameLogin = findViewById(R.id.txtInputUsernameLogin);
        txtInputPasswordLogin = findViewById(R.id.txtInputPasswordLogin);

        btnSignup.setOnClickListener(view -> {
            new Signup_Fragment().show(getSupportFragmentManager(), "SIGNUP_FRAGMENT");
        });

    }


    @Override
    public void onOkPressed(String username, String pass, String passConf, String email, String phone) {
        // TODO: code to create new user
    }
}