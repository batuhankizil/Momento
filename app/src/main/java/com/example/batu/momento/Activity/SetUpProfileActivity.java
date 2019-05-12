package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivitySetUpProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;

public class SetUpProfileActivity extends AppCompatActivity {

    private ActivitySetUpProfileBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference userReference;

    String currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_profile);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        binding.signInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);*/

                SetupProfile();
            }
        });
    }

    private void SetupProfile(){
        String userFullName = binding.userFullName.getText().toString();
        String userBirthday = binding.userBirthday.getText().toString();
        String userGender = binding.userGender.getText().toString();
        String userAbout = binding.userAbout.getText().toString();

        if (TextUtils.isEmpty(userFullName) || TextUtils.isEmpty(userBirthday) || TextUtils.isEmpty(userGender) || TextUtils.isEmpty(userAbout)){
            Toast.makeText(SetUpProfileActivity.this, "Alanlar Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap userMap = new HashMap();
            userMap.put("fullName",userFullName);
            userMap.put("birthday",userBirthday);
            userMap.put("gender",userGender);
            userMap.put("about",userAbout);
            //userMap.put("FullName",userFullName);
           // userMap.put("FullName",userFullName);
            userReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(SetUpProfileActivity.this, "Anılarınızı Paylaşabilirsiniz.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SetUpProfileActivity.this, "Başarısız!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
