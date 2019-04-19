package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivitySignInBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordPage = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(resetPasswordPage);
            }
        });

        binding.signInHome.setOnClickListener(new View.OnClickListener() {
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
