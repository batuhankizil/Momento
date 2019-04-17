package com.example.batu.momento.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.batu.momento.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        /*Button signInHome = findViewById(R.id.signInHome);

        signInHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInHome = new Intent(SignInActivity.this, FragmentHome.class);
                startActivity(signInHome);
            }
        });*/

    }

}
