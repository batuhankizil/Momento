package com.example.batu.momento.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.batu.momento.Fragment.FragmentComments;
import com.example.batu.momento.Fragment.FragmentCreatePost;
import com.example.batu.momento.Model.Post;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.UserPostLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private UserPostLayoutBinding binding;

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_post_layout,parent,false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPost.get(position);
        Glide.with(mContext).load(post.getPostPicture()).into(holder.userPostPicture);

        if (post.getPostAbout().equals("")){
            holder.userPostAbout.setVisibility(View.GONE);
        } else {
            holder.userPostAbout.setVisibility(View.VISIBLE);
            holder.userPostAbout.setText(post.getPostAbout());
        }

        postSender(holder.userProfilePhoto,holder.userFullName,post.getPostSender());

        likes(post.getPostId(), holder.likeButton);
        likeNumber(holder.postLikeNumber, post.getPostId());

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likeButton.getTag().equals("Beğen")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("postId", post.postId);
                editor.apply();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FragmentComments());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userProfilePhoto,userPostPicture,likeButton;
        public TextView userPostAbout,userFullName,postLikeNumber;
        public Button commentButton;

        public ViewHolder(View itemView) {
            super(itemView);

            userProfilePhoto = itemView.findViewById(R.id.user_profile_photo);
            userPostPicture = itemView.findViewById(R.id.user_post_picture);
            userPostAbout = itemView.findViewById(R.id.user_post_about);
            userFullName = itemView.findViewById(R.id.user_full_name);
            postLikeNumber = itemView.findViewById(R.id.post_like_number);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
        }
    }


    private void likes(String postId, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("likes")
                .child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Beğenildi");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Beğen");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void likeNumber(final TextView likes, String postId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("likes")
                .child(postId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postSender(final ImageView userProfilePhoto, final TextView userFullName, String userId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                Glide.with(mContext).load(users.getProfilePhoto()).into(userProfilePhoto);
                userFullName.setText(users.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
