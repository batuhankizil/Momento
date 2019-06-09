package com.example.batu.momento.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.batu.momento.Activity.HomeActivity;
import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentCreatePostBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;


import static android.app.Activity.RESULT_OK;

public class FragmentCreatePost extends Fragment {

    private FragmentCreatePostBinding binding;

    Uri resimUri;
    String mUri = "";

    StorageTask storageTask;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false);


        storageReference = FirebaseStorage.getInstance().getReference("posts");

        binding.postShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureUpload();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(getContext(),this);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private String fileExtention(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimUri = result.getUri();

            binding.userPostPicture.setImageURI(resimUri);
        } else {
            Toast.makeText(getActivity(), "Resim Seçilemedi.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pictureUpload(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();

        if (resimUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + fileExtention(resimUri));

            storageTask = fileReference.putFile(resimUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        mUri = downloadUri.toString();

                        DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("posts");

                        String postId = dataReference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("postId",postId);
                        hashMap.put("postPicture",mUri);
                        hashMap.put("postAbout",binding.about.getText().toString());
                        hashMap.put("postSender", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        dataReference.child(postId).setValue(hashMap);

                        progressDialog.dismiss();

                        startHomePage();

                    } else {
                        Toast.makeText(getActivity(), "Gönderi Paylaşılamadı.", Toast.LENGTH_SHORT).show();

                        startHomePage();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getActivity(), "Seçilen Resim Yok.", Toast.LENGTH_SHORT).show();
        }

    }

    private void startHomePage(){
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }
}
