package com.example.batu.momento.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentEditProfileBinding;

public class FragmentEditProfile extends Fragment {

    private FragmentEditProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);



        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editUserInformation();

    }

    private void editUserInformation(){
        Users user = new Users();
        //user.getUser(getContext());
        /*binding.fullName.setText(user.fullName);*/

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String fullname = preferences.getString("fullname",user.fullName);
        String birtday = preferences.getString("birtday",user.birtday);
        String gender = preferences.getString("gender",user.gender);
        String about = preferences.getString("about",user.about);

        binding.editFullName.setText(fullname);
        binding.editBirtday.setText(birtday);
        binding.editGender.setText(gender);
        binding.editAbout.setText(about);
    }
}
