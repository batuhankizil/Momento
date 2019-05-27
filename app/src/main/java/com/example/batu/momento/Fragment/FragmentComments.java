package com.example.batu.momento.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batu.momento.Model.Post;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentCommentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class FragmentComments extends Fragment {

    private FragmentCommentsBinding binding;

    //String postId;
    String senderId;

    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        /*Intent intent = getActivity().getIntent();

        postId = intent.getStringExtra("postId");
        senderId = intent.getStringExtra("senderId");*/

        binding.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addComment.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Pasif buton ekle...", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Yorumlar");

        Post post = new Post();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String postId = preferences.getString("postId",post.postId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("comments").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.commentNumber.setText(dataSnapshot.getChildrenCount() + " Yorum");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void comments(String postId, TextView commentNumber){

    }

    private void addComment(){

        Post post = new Post();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String postId = preferences.getString("postId",post.postId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("comments").child(postId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", binding.addComment.getText().toString());
        hashMap.put("sender", firebaseUser.getUid());

        databaseReference.push().setValue(hashMap);
        binding.addComment.setText("");
    }


}
