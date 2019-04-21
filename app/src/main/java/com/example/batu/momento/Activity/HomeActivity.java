package com.example.batu.momento.Activity;

import android.os.Bundle;


import com.example.batu.momento.Fragment.FragmentHome;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.ActivityHomeBinding;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);


        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle("MOMENTO");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FragmentHome())
                .commit();
    }
}
