package com.example.mapchat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

public class Post extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

    FusedLocationProviderClient fusedLocationProviderClient;
    double lat,lon;
    TextView textView;
    EditText editText;




    public static final String Message = "ie.ul.myfirstapp.EXTRA_MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(Post.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }else {
            ActivityCompat.requestPermissions(Post.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

        }

        editText = (EditText) findViewById(R.id.Message);

        textView = (TextView) findViewById(R.id.textView7);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = editText.length();
                length = 86-length;
                String convert = String.valueOf(length);
                textView.setText(convert);
            }

            @Override
            public void afterTextChanged(Editable s) {

               /* if(s.length()/140 == 0){
                    textView.setText("86");
                }else{
                    textView.setText("86/"+String.valueOf((s.length()/140)+1));
                }*/
            }
        });




        // System.out.println(loc);


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>(){
            @Override
            public void onComplete(@Nonnull Task<Location> task) {
                Location location = task.getResult();
                if(location !=null){
                    try {
                        Geocoder geocoder = new Geocoder(Post.this,Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        System.out.println("We did it bois");
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void post(View view){
        CollectionReference Posts = db.collection("Posts");

       // double longitude = location.getLongitude();
       // double latitude = location.getLatitude();



        EditText editText = (EditText) findViewById(R.id.Message);
        String PostMessage = editText.getText().toString();
        Map<String,Object> Posts1 = new HashMap<>();
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts=Long.toString(System.currentTimeMillis());

        String name = "";
        for(int i =0; i<13;i++) {
            name+=(9- (Character.getNumericValue(ts.charAt(i))));

        }


        Posts1.put(name,PostMessage);

        Posts.document(name).set(Posts1);

        Intent intent = new Intent(this,Reading.class);

        startActivity(intent);

    }
    public String clean(String ref) {
        String newRef;
        newRef = ref.substring(50);
        return newRef;

    }

    public void postData(View view) {
        String ts=Long.toString(System.currentTimeMillis());
        String tst = "";
        for(int i =0; i<13;i++) {
            tst+=(9- (Character.getNumericValue(ts.charAt(i))));

        }

       //  location.getLongitude();
       // location.getLatitude();


        //DatabaseReference myRef = database.getReference(tst+""+user);
        EditText editText = (EditText) findViewById(R.id.Message);
        String postMessage = editText.getText().toString();

        //String refString = myRef.toString();
        //myRef.setValue(postMessage);
        //String newRef =clean(refString);
       // System.out.println(user);
        //System.out.println(refString);
        //System.out.println(longitude);
       // FirebaseDatabase.getInstance().getReference().child("Users").child(user).child(tst).setValue(postMessage);
        FirebaseDatabase.getInstance().getReference().child("Location").child(tst).setValue(lat+"!"+lon+"!"+user+"!"+postMessage);
        FirebaseDatabase.getInstance().getReference().child("Users").child(user).child(tst).setValue(postMessage);


        Intent intent = new Intent(this,Reading.class);

        startActivity(intent);


    }
}