package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.batu.momento.Fragment.FragmentChats;
import com.example.batu.momento.Fragment.FragmentHome;
import com.example.batu.momento.Fragment.FragmentProfile;
import com.example.batu.momento.Fragment.FragmentSaved;
import com.example.batu.momento.R;
import com.example.batu.momento.Utils.PreferenceUtils;
import com.example.batu.momento.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference userReference;

    private NavigationView navigationView;

    public TextView userFullName;
    public TextView userEmail;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        ActionBarDrawer();
        FragmentHomePage();
        UserNavigationHeader();



        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentHome())
                    .commit();
            binding.navView.setCheckedItem(R.id.nav_home_button);
        }
    }

    private void UserNavigationHeader() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navView = navigationView.inflateHeaderView(R.layout.nav_header);

        userFullName = (TextView) navView.findViewById(R.id.user_full_name);
        userEmail = (TextView) navView.findViewById(R.id.user_email);

        userReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("fullName").getValue().toString();
                    String eMail = dataSnapshot.child("eMail").getValue().toString();

                    userFullName.setText(fullName);
                    userEmail.setText(eMail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void FragmentHomePage() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FragmentHome())
                .commit();
    }

    private void ActionBarDrawer() {
        setSupportActionBar(binding.toolbar);
        binding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home_button:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new FragmentHome())
                        .commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new FragmentProfile())
                        .commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new FragmentChats())
                        .commit();
                break;
            case R.id.nav_notification:
                break;
            case R.id.nav_saved:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, new FragmentSaved())
                        .commit();
                break;
            case R.id.nav_info:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.savePassword(null, this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return true;
    }
}
