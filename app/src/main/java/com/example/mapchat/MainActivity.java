package com.example.mapchat;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.*;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();;
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userName ;
    DatabaseReference checkPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = storage.getReferenceFromUrl("gs://mapchat-d7f34.appspot.com");

        mAuth = FirebaseAuth.getInstance();
        //getActionBar().hide();
        setContentView(R.layout.activity_main);

        final ImageView profo = findViewById(R.id.icon);

        final Bitmap[] bitmap = new Bitmap[1];

        StorageReference islandRef = mStorageRef.child("images/MAPCHAT.png");

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

<<<<<<< Updated upstream
=======
        final ImageView background = findViewById(R.id.background);

        final Bitmap[] bitmap1 = new Bitmap[1];

        StorageReference islandRef1 = mStorageRef.child("images/background.png");

        final long ONE_MEGABYTE1 = 5000 * 5000;
        islandRef1.getBytes(ONE_MEGABYTE1).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                System.out.println("Success Byte");
                bitmap1[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                background.setImageBitmap(bitmap1[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Failure Byte");
            }
        });
>>>>>>> Stashed changes

        }



    public void signIn(View view) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.mipmap.ic_launcher)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(true)

                        .build(),RC_SIGN_IN);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("Sign in successful!\n" + "name = " + user.getDisplayName() + "\n" +
                        "email = " + user.getEmail() + "\n" +
                        "id = " + user.getUid());
               // final String userName = user.toString();
               // FirebaseDatabase.getInstance().getReference().child("Users")).hasChild(user);
               // checkPost = FirebaseDatabase.getInstance().getReference().child("Users");
                //if(checkPost.c  //(userName));
                //final String finalTst = tst;




                Intent intent = new Intent(this,Reading.class);
                startActivity(intent);
            }
            else{
                if(response ==null) {
                    System.out.println("Sign in cancelled");
                    return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    System.out.println("No internet connection");return;

                }
            }
        }
    }
}