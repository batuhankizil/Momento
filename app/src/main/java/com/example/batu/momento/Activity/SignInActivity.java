package com.example.batu.momento.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batu.momento.Model.Post;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.Utils.PreferenceUtils;
import com.example.batu.momento.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    //private String userId;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        binding.userEmail.addTextChangedListener(loginTextWatcher);
        binding.userPassword.addTextChangedListener(loginTextWatcher);

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
                LoginControl();

            }

        });
    }

    public void LoginControl() {
        final String eMail = binding.userEmail.getText().toString();
        final String password = binding.userPassword.getText().toString();

        binding.loginProgress.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(eMail) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignInActivity.this, "Lütfen Bilgilerinizi Giriniz.", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(eMail, password)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(auth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Users user = new Users();
                                        user.seteMail(dataSnapshot.child("eMail").getValue().toString());
                                        user.setFullName(dataSnapshot.child("fullName").getValue().toString());
                                        user.setAbout(dataSnapshot.child("about").getValue().toString());
                                        user.setBirtday(dataSnapshot.child("birthday").getValue().toString());
                                        user.setGender(dataSnapshot.child("gender").getValue().toString());
                                        user.setProfilePhoto(dataSnapshot.child("profilePhoto").getValue().toString());
                                        user.setUserId(dataSnapshot.child("userId").getValue().toString());

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", user.eMail);
                                        editor.putString("fullname", user.fullName);
                                        editor.putString("about", user.about);
                                        editor.putString("birtday", user.birtday);
                                        editor.putString("gender", user.gender);
                                        editor.putString("profilephoto", user.profilePhoto);
                                        editor.putString("userid", user.userId);
                                        editor.apply();

                                        PreferenceUtils.saveEmail(eMail, getApplicationContext());
                                        PreferenceUtils.savePassword(password, getApplicationContext());

                                        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        homeIntent.putExtra("eMail", binding.userEmail.getText().toString().trim());
                                        startActivity(homeIntent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(SignInActivity.this, "Giriş Başarısız.", Toast.LENGTH_SHORT).show();
                                binding.loginProgress.setVisibility(View.GONE);
                            }
                        }
                    });

        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userEmail = binding.userEmail.getText().toString().trim();
            String userPassword = binding.userPassword.getText().toString().trim();

            binding.signInHome.setEnabled(!userEmail.isEmpty() && userPassword.length() >= 6);
            if (userEmail.isEmpty() || userPassword.length() < 6){
                binding.signInHome.setAlpha(0.5F);
            }else {
                binding.signInHome.setAlpha(1);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
