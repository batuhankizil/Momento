package com.example.batu.momento.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.batu.momento.Fragment.FragmentProfile;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.UserItemLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private UserItemLayoutBinding binding;

    private Context mContext;
    private List<Users> mUsers;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<Users> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Users users = mUsers.get(position);
        holder.userFollow.setVisibility(View.VISIBLE);

        holder.userFullName.setText(users.getFullName());
        Glide.with(mContext).load(users.getProfilePhoto()).into(holder.userProfileImage);
        beingFollowed(users.getUserId(),holder.userFollow);

        if (users.getUserId().equals(firebaseUser.getUid())){
            holder.userFollow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",users.getUserId());
                editor.apply();

                ((FragmentActivity)mContext)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new FragmentProfile())
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.userFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.userFollow.getText().toString().equals("Takip Et")){

                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(users.getUserId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("follow").child(users.getUserId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                } else {

                    FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid())
                            .child("following").child(users.getUserId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("follow").child(users.getUserId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userFullName;
        public CircleImageView userProfileImage;
        public Button userFollow;

        public ViewHolder(View itemView) {
            super(itemView);

            userFullName = itemView.findViewById(R.id.user_fullname);
            userProfileImage = itemView.findViewById(R.id.user_profile_image);
            userFollow = itemView.findViewById(R.id.user_follow);
        }
    }

    private void beingFollowed (final String userId, final Button userFollow){
        DatabaseReference userFollowWay = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");

        userFollowWay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()){
                    userFollow.setText("Takip Ediliyor");
                }else {
                    userFollow.setText("Takip Et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
