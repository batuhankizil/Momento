package com.example.batu.momento.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.batu.momento.Adapter.CommentAdapter;
import com.example.batu.momento.Model.Comment;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentComments extends Fragment {

    private FragmentCommentsBinding binding;

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    //String senderId;

    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addComment.getText().toString().equals("")){
                    //Toast.makeText(getContext(), "Pasif buton ekle...", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        readComment();

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Yorumlar");

        recyclerView = binding.comments;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(),commentList);
        recyclerView.setAdapter(commentAdapter);

        binding.addComment.addTextChangedListener(commentTextWatcher);

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

    private void readComment(){
        Post post = new Post();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String postId = preferences.getString("postId",post.postId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("comments").child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private TextWatcher commentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String comment = binding.addComment.getText().toString().trim();

            binding.commentButton.setEnabled(!comment.isEmpty());

            if (comment.isEmpty()){
                binding.commentButton.setAlpha(0.5F);
            }else {
                binding.commentButton.setAlpha(1);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
