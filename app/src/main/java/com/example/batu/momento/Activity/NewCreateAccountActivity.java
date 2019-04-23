package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityCreateAccountBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class NewCreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);

        binding.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setUpProfileActivity = new Intent(getApplicationContext(), SetUpProfileActivity.class);
                startActivity(setUpProfileActivity);
            }
        });
    }
}
