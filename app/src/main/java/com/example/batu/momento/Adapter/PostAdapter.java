package com.example.batu.momento.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Post post = mPost.get(position);
        Glide.with(mContext).load(post.getPostSender()).into(holder.userPostPicture);

        if (post.getPostAbout().equals("")){
            holder.userPostAbout.setVisibility(View.GONE);
        } else {
            holder.userPostAbout.setVisibility(View.VISIBLE);
            holder.userPostAbout.setText(post.getPostAbout());
        }

        postSender(holder.userProfilePhoto,holder.userFullName,post.getPostSender());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userProfilePhoto,userPostPicture;
        public TextView userPostAbout,userFullName,postLikeNumber;
        public Button likeButton,commentButton;

        public ViewHolder(View itemView) {
            super(itemView);

            userPostPicture = itemView.findViewById(R.id.user_post_picture);
            userPostAbout = itemView.findViewById(R.id.user_post_about);
            userFullName = itemView.findViewById(R.id.user_full_name);
            postLikeNumber = itemView.findViewById(R.id.post_like_number);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
        }
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
