package com.example.batu.momento.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.batu.momento.Adapter.PhotoAdapter;
import com.example.batu.momento.CommunicationInterface;
import com.example.batu.momento.Model.Post;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentProfile extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mDatabase;

    private FirebaseUser firebaseUser;
    String profileId;

    RecyclerView postPhotoRecycler;
    PhotoAdapter photoAdapter;
    List<Post> postList;

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(authListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        setHasOptionsMenu(true);
        createPostButton();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = preferences.getString("profileid", "none");

        postPhotoRecycler = binding.postPhotos;
        postPhotoRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        postPhotoRecycler.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(),postList);
        postPhotoRecycler.setAdapter(photoAdapter);

        userInformation();
        followersReference();
        photos();

        if (firebaseUser.getUid().equals(firebaseUser.getUid())){
            binding.followButton.setVisibility(View.GONE);
        } else {
            followControl();
        }


        binding.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonFollow = binding.followButton.getText().toString();

                if (buttonFollow.equals("Takip Et")){

                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                }else if (buttonFollow.equals("Takip Ediliyor")){

                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.application_name);

        //userProfileInformation();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_edit_profile:

                CommunicationInterface communicationInterface = (CommunicationInterface) getActivity();
                communicationInterface.openEditProfileFragment();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void createPostButton() {
        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.full_container, new FragmentCreatePost());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void userProfileInformation() {
        mAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            binding.fullName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                            binding.about.setText(String.valueOf(dataSnapshot.child("about").getValue()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        };
    }

    private void userInformation(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }

                Users user = dataSnapshot.getValue(Users.class);
                Glide.with(getContext()).load(user.getProfilePhoto()).into(binding.userProfileImage);
                binding.fullName.setText(user.getFullName());
                binding.about.setText(user.getAbout());

                /*Users user = new Users();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String profilephoto = preferences.getString("profilephoto",user.profilePhoto);
                String fullname = preferences.getString("fullname",user.fullName);
                String about = preferences.getString("about",user.about);


                Glide.with(getContext()).load(profilephoto).into(binding.userProfileImage);
                binding.fullName.setText(fullname);
                binding.about.setText(about);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void followControl(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users user = new Users();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String userid = preferences.getString("userid",user.getUserId());

                if (dataSnapshot.child(userid).exists()){
                    binding.followButton.setText("Takip Ediliyor");
                    binding.followButton.setVisibility(View.VISIBLE);
                } else {
                    binding.followButton.setText("Takip Et");
                    binding.followButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void followersReference(){
        DatabaseReference followerReference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("followers");
        followerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.followerNumber.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference followingReference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        followingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.followNumber.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void photos(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPostSender().equals(firebaseUser.getUid())){
                        postList.add(post);
                    }
                }

                Collections.reverse(postList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
