package com.example.batu.momento.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityResetPasswordBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        binding.resetPwEmail.addTextChangedListener(resetPasswordTextWatcher);

        binding.resetPasswordSendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private TextWatcher resetPasswordTextWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userEmail = binding.resetPwEmail.getText().toString().trim();

            binding.resetPasswordSendLinkButton.setEnabled(!userEmail.isEmpty());
            if (userEmail.isEmpty()){
                binding.resetPasswordSendLinkButton.setAlpha(0.5F);
            }else {
                binding.resetPasswordSendLinkButton.setAlpha(1);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
