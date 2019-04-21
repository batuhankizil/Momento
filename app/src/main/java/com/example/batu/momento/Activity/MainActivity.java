package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInPage = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInPage);
            }
        });

        binding.createNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCreateAccount = new Intent(getApplicationContext(), NewCreateAccountActivity.class);
                startActivity(newCreateAccount);
            }
        });


    /*@Override
    public void onBackPressed(){
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }*/

    }

}
