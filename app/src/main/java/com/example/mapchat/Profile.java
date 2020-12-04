package com.example.mapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    FirebaseStorage storage = FirebaseStorage.getInstance();;
    private StorageReference mStorageRef;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Intent Privdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = storage.getReferenceFromUrl("gs://mapchat-d7f34.appspot.com");

        setContentView(R.layout.activity_profile);

        final ImageView profo = findViewById(R.id.profo);   //Setting Profile Picture if it exists

        final Bitmap[] bitmap = new Bitmap[1];

        StorageReference islandRef = mStorageRef.child("images/" + user);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                System.out.println("Success Byte");
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profo.setImageBitmap(bitmap[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Failure Byte");
            }
        });
        TextView profileName =findViewById(R.id.topText);
        profileName.setText(user);

    }

    public void uploadProfo(View view) {            //To set profile Image

        chooseImage();
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Privdata = data;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView profo = findViewById(R.id.profo);
                profo.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



    private void uploadImage() {            //Uploads image to Firebase Storage
        if(filePath != null)
        {
            //final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Uploading...");
            //progressDialog.show();
            System.out.println("1 :" +filePath.toString());
            StorageReference ref = mStorageRef.child("images/"+ user);
            System.out.println(mStorageRef.child("images/"+ user));
            System.out.println(filePath.toString());
            System.out.println(ref);
            Uri file =  Uri.parse(filePath.getPath());
            System.out.println(file);
            ref.putFile(filePath)
                   .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Success");
                           // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                           // progressDialog.dismiss();
                            //Toast.makeText(Profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Failure");
                            //progressDialog.dismiss();
                            //Toast.makeText(Profile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("OnProgress");
                            //double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                   // .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}




