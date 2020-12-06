package com.example.mapchat;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.*;


import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        //getActionBar().hide();
        setContentView(R.layout.activity_main);


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