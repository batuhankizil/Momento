package com.example.batu.momento.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentCommentsOptionsDialogBinding;

public class FragmentCommentOptionsDialog extends Fragment {
    private FragmentCommentsOptionsDialogBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments_options_dialog, container, false);
        return binding.getRoot();
    }
}
