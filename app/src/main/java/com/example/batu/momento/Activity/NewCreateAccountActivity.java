package com.example.batu.momento.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.batu.momento.Fragment.FragmentCommentOptionsDialog;
import com.example.batu.momento.Fragment.FragmentProfile;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;

public class NewCreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_account);

        auth = FirebaseAuth.getInstance();

        binding.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String createAccountEmail = binding.createAccountEmail.getText().toString();
                String createAccountPassword = binding.createAccountPassword.getText().toString();
                String createAccountConfirmPassword = binding.createAccountConfirmPassword.getText().toString();
                
                if (TextUtils.isEmpty(createAccountEmail) || TextUtils.isEmpty(createAccountPassword) || TextUtils.isEmpty(createAccountConfirmPassword)) {
                    //Toast.makeText(NewCreateAccountActivity.this, "Alanlar Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewCreateAccountActivity.this);
                    builder.setTitle("Hata!");
                    builder.setMessage("Alanlar Boş Bırakılamaz.");

                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();

                }
                else if (!createAccountPassword.equals(createAccountConfirmPassword)){
                    //Toast.makeText(NewCreateAccountActivity.this, "Şifreler Uyuşmuyor!", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewCreateAccountActivity.this);
                    builder.setTitle("Hata!");
                    builder.setMessage("Şifreler Uyuşmuyor.");

                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                }
                else
                    register(createAccountEmail, createAccountPassword, createAccountConfirmPassword);

            }
        });
    }

    private void register(final String createAccountEmail, final String createAccountPassword, String createAccountConfirmPassword) {
        auth.createUserWithEmailAndPassword(createAccountEmail, createAccountPassword)
                .addOnCompleteListener(NewCreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Email", createAccountEmail.toLowerCase());
                            hashMap.put("Sifre", createAccountPassword);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent setUpProfileActivity = new Intent(getApplicationContext(), SetUpProfileActivity.class);
                                    setUpProfileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(setUpProfileActivity);
                                }
                            });
                        } else {
                            Toast.makeText(NewCreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
