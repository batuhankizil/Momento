package com.example.batu.momento.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.batu.momento.Model.Users;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentEditProfileBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class FragmentEditProfile extends Fragment {

    private FirebaseUser firebaseUser;
    private Uri mUri;
    private StorageReference storageRef;
    private StorageTask storageTask;

    TextView fullName,birtday,gender,about;
    ImageView profilePhoto;

    private FragmentEditProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);

        fullName = getActivity().findViewById(R.id.edit_full_name);
        birtday = getActivity().findViewById(R.id.edit_birtday);
        gender = getActivity().findViewById(R.id.edit_gender);
        about = getActivity().findViewById(R.id.edit_about);
        profilePhoto = getActivity().findViewById(R.id.user_profile_photoo);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("profilePhoto");


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editUserInformation();
        changeProfilePhotoClick();
        saveButtonClick();
    }

    private void editUserInformation(){
        Users user = new Users();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String fullname = preferences.getString("fullname",user.fullName);
        String birtday = preferences.getString("birtday",user.birtday);
        String gender = preferences.getString("gender",user.gender);
        String about = preferences.getString("about",user.about);

        binding.editFullName.setText(fullname);
        binding.editBirtday.setText(birtday);
        binding.editGender.setText(gender);
        binding.editAbout.setText(about);

        Glide.with(getContext()).load(user.getProfilePhoto()).into(binding.userProfilePhotoo);



    }

    private void changeProfilePhotoClick(){
        binding.changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(getActivity());
            }
        });

        binding.userProfilePhotoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(getActivity());
            }
        });

    }

    private void saveButtonClick(){
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(binding.editFullName.getText().toString(),binding.editBirtday.getText().toString(),binding.editGender.getText().toString(),binding.editAbout.getText().toString());
            }
        });
    }

    private void updateUserProfile(String fullName, String birthday, String gender, String about){

        Users user = new Users();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userid = preferences.getString("userid",user.userId);

        DatabaseReference asd = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        HashMap<String,Object> userUpdateHashMap = new HashMap<>();
        userUpdateHashMap.put("fullName",fullName);
        userUpdateHashMap.put("birthday",birthday);
        userUpdateHashMap.put("gender",gender);
        userUpdateHashMap.put("about",about);

        asd.updateChildren(userUpdateHashMap);

    }

    private String fileReference(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void photoLoad(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.show();

        if (mUri != null){
            final StorageReference storageReference = storageRef.child(System.currentTimeMillis() + "." + fileReference(mUri));

            storageTask = storageReference.putFile(mUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUrim = downloadUri.toString();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String,Object> photoHashMap = new HashMap<>();
                        photoHashMap.put("profilePhoto","" + mUrim);

                        databaseReference.updateChildren(photoHashMap);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(),"Yükleme Başarısız.",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(),"Resim Seçilemedi..",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mUri = result.getUri();

            photoLoad();
        } else {
            Toast.makeText(getContext(),"Birşeyler Yanlış Gitti.",Toast.LENGTH_SHORT).show();
        }
    }
}
