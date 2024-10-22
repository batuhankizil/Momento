package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.batu.momento.R;
import com.example.batu.momento.Utils.PreferenceUtils;
import com.example.batu.momento.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initViews();

        binding.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInActivity();
            }
        });


        binding.createNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCreateAccountActivity();
            }
        });

    }

    private void SignInActivity() {
        Intent signInPage = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(signInPage);
    }

    private void NewCreateAccountActivity() {
        Intent newCreateAccount = new Intent(getApplicationContext(), NewCreateAccountActivity.class);
        startActivity(newCreateAccount);
    }

    public void initViews(){
        if (PreferenceUtils.getEmail(this) != null /*|| !PreferenceUtils.getEmail(this).equals("")*/){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);*/
        }
    }

}
