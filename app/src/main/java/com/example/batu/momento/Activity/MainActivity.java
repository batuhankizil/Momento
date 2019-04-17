package com.example.batu.momento.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.batu.momento.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signinButton = findViewById(R.id.signin_button);
        TextView createNewAccount = findViewById(R.id.create_new_account_link);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInPage = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInPage);
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCreateAccount = new Intent(MainActivity.this, NewCreateAccountActivity.class);
                startActivity(newCreateAccount);
            }
        });

    }


}
