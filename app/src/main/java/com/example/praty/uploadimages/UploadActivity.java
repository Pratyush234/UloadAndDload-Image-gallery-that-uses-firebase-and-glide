package com.example.praty.uploadimages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private static final int RC_UPLOAD = 1;
    private Button mChooseButton;
    private Button mUploadButton;
    private ImageView mImageView;
    private Uri filePath;
    private FirebaseStorage mStorage;
    private StorageReference mRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        
        mChooseButton=(Button) findViewById(R.id.chooseButton);
        mUploadButton=(Button) findViewById(R.id.uploadButton);
        mImageView=(ImageView) findViewById(R.id.imageView);

        mStorage=FirebaseStorage.getInstance();
        mRef=mStorage.getReference();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference();

        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromPhone();
            }
        });
        
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });
    }

    private void chooseImageFromPhone() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),RC_UPLOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== RC_UPLOAD && resultCode== RESULT_OK && data!=null && data.getData()!=null){
            filePath=data.getData();
            GlideApp.with(this)
                    .load(filePath)
                    .fitCenter()
                    .into(mImageView);


        }
    }

    private void uploadImageToFirebase() {
        if(filePath!=null)
        {
            final ProgressDialog progressDialog= new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference mReference= mRef.child("images/"+ UUID.randomUUID().toString());
            mReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            mReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UrlModel mObject=new UrlModel();
                                    mObject.setmUrl(uri.toString());
                                    mDatabaseRef.child("URL_LINKS").push().setValue(mObject);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double Progress=(100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)Progress+"%");

                        }
                    });
        }

    }
}
