package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.batu.momento.Fragment.FragmentHome;
import com.example.batu.momento.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        TextView resetPassword = findViewById(R.id.reset_password);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordPage = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(resetPasswordPage);
            }
        });

        Button signInHome = findViewById(R.id.signInHome);

        signInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FragmentHome());
                ft.addToBackStack(null);
                ft.commit();*/


                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);



            }
        });

    }

}
