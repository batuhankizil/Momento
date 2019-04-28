package com.example.batu.momento.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentChatsBinding;
import com.example.batu.momento.databinding.FragmentSavedBinding;

public class FragmentSaved extends Fragment {
    private FragmentSavedBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved, container, false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Kaydedilenler");

    }
}
