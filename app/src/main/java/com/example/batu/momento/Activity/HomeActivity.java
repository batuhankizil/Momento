package com.example.batu.momento.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.batu.momento.Fragment.FragmentChats;
import com.example.batu.momento.Fragment.FragmentHome;
import com.example.batu.momento.Fragment.FragmentProfile;
import com.example.batu.momento.Fragment.FragmentSaved;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        ActionBarDrawer();
        FragmentHomePage();

    }

    private void FragmentHomePage(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FragmentHome())
                .commit();
    }

    private void ActionBarDrawer(){
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
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
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
