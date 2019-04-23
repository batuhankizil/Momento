package com.example.batu.momento.Activity;

import android.os.Bundle;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityCreateAccountBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class NewCreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);
    }
}
