package com.example.batu.momento.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.batu.momento.Fragment.FragmentComments;
import com.example.batu.momento.Fragment.FragmentHome;
import com.example.batu.momento.Model.Comment;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mComments;

    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_comment_layout, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComments.get(position);

        holder.userComment.setText(comment.getComment());

        userInformation(holder.userProfilePicture,holder.userName,comment.getSender());

        holder.userComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("senderId", comment.getSender());
                editor.apply();

                AppCompatActivity activity = (AppCompatActivity) mContext;
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FragmentHome());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView userProfilePicture;
        public TextView userName, userComment, userCommentTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfilePicture = itemView.findViewById(R.id.user_comment_picture);
            userName = itemView.findViewById(R.id.user_name);
            userComment = itemView.findViewById(R.id.user_comment);
        }
    }

    private void userInformation(final ImageView imageView, final TextView userName, String senderId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(senderId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);

                Glide.with(mContext).load(users.getProfilePhoto()).into(imageView);
                userName.setText(users.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
