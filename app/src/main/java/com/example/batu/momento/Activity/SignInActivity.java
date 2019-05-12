package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

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

        if (TextUtils.isEmpty(eMail) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignInActivity.this, "Lütfen Bilgilerinizi Giriniz.", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(eMail, password)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(auth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);*/

                                       /* Users user = new Users();
                                        user.seteMail(dataSnapshot.child(eMail).getValue(Users.class).geteMail());*/
                                        //user.fullName(dataSnapshot.child());




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
                            } else
                                Toast.makeText(SignInActivity.this, "Giriş Başarısız.", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

}
