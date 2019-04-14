package com.example.batu.momento;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.batu.momento.Fragment.FragmentComments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*Button signinButton = findViewById(R.id.signin_button);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInPage = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInPage);
            }
        });*/

        Button commentsButton = findViewById(R.id.menu_button);

        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment, new FragmentComments());
                ft.addToBackStack(null);
                ft.commit();

                /*TextView layone= (TextView) findViewById(R.id.comments_title);// change id here

                layone.setVisibility(View.VISIBLE);

                TextView layone2= (TextView) findViewById(R.id.app_name);// change id here

                layone2.setVisibility(View.GONE);*/
            }
        });
    }


}
