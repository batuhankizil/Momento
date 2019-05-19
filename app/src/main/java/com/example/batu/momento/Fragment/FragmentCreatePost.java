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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.batu.momento.R;
import com.example.batu.momento.databinding.FragmentCreatePostBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class FragmentCreatePost extends Fragment {

    private FragmentCreatePostBinding binding;

    Uri resimUri;
    String myUri;

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

        /*CropImage.activity()
                .setAspectRatio(1,1)
                .start(getContext(),this);*/


        return binding.getRoot();
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
            Uri storageReference = result.getUri();

            binding.userPostPicture.setImageURI(resimUri);
        } else {
            Toast.makeText(getActivity(), "Resim Seçilemedi.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pictureUpload(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();
    }
}
