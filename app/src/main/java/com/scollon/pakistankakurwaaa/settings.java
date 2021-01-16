package com.scollon.pakistankakurwaaa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class settings extends AppCompatActivity {
    private Button saveBtn;
    private EditText userNameEt, userBioET;
    private ImageView profileImageView;
    private static int galleryPick = 1;
    private Uri ImageUri;
    private StorageReference userProfileImgRef;
    private String downloadUrl;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        userRef = FirebaseDatabase.getInstance().getReference("Users");


        saveBtn = findViewById(R.id.save_settings_btn);
        userBioET = findViewById(R.id.bio_settings);
        userNameEt = findViewById(R.id.username_settings);
        profileImageView = findViewById(R.id.settings_profile_image);
        progressDialog = new ProgressDialog(this);



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select an image from galley
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, galleryPick);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUserData();
                RetriveUserInfo();
            }
        });


        RetriveUserInfo();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode == RESULT_OK && data != null){
            ImageUri = data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }
    private void saveUserData() {
        final String getUserName = userNameEt.getText().toString();
        final String getUserStatus = userBioET.getText().toString();

        if(ImageUri == null){

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                        //  saveInfoOnly();

                    }else{
                        Toast.makeText(settings.this, "select an image first", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }else if (getUserName.equals("")){
            Toast.makeText(this, "username is Mandatory", Toast.LENGTH_SHORT).show();
        }else if (getUserStatus.equals("")){
            Toast.makeText(this, "Bio is Mandatory", Toast.LENGTH_SHORT).show();
        }else{

            progressDialog.setTitle("Account settings");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            final StorageReference filePath =userProfileImgRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filePath.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }


                    downloadUrl = filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(settings.this, "jeszcze dziala", Toast.LENGTH_SHORT).show();
                         downloadUrl = task.getResult().toString();

                        HashMap<String, Object> profileMap = new HashMap<>();
                        profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name", getUserName);
                        profileMap.put("status", getUserStatus);
                        profileMap.put("image", downloadUrl);
                        Log.d("zapisywanie", "dziala");

                        Toast.makeText(settings.this, "hash map", Toast.LENGTH_SHORT).show();
                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(settings.this, "chyba działa", Toast.LENGTH_SHORT).show();
                                if(task.isSuccessful()){

                                    Intent  intent = new Intent(settings.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    progressDialog.dismiss();
                                    Toast.makeText(settings.this, "update successfull", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }
                }
            });
        }


    }

/*
    private void saveInfoOnly() {

        final String getUserName = userNameEt.getText().toString();
        final String getUserStatus = userBioET.getText().toString();

        if (getUserName.equals("")) {
            Toast.makeText(this, "username is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (getUserStatus.equals("")) {
            Toast.makeText(this, "Bio is Mandatory", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("Account settings");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();


            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name", getUserName);
            profileMap.put("status", getUserStatus);


            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Intent intent = new Intent(settings.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(settings.this, "update successfull", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    } */



    //}

    private void RetriveUserInfo(){

        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String imageDb = snapshot.child("image").getValue().toString();
                    String NameDb = snapshot.child("name").getValue().toString();
                    String BioDb = snapshot.child("status").getValue().toString();

                    userNameEt.setText(NameDb);
                    userBioET.setText(BioDb);

                    Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(profileImageView);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





}