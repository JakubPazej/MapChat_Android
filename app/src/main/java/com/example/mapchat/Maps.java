package com.example.mapchat;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //FirebaseDatabase.getInstance().getReference().child("Location");
        database = FirebaseDatabase.getInstance();
        myRef =database.getReference().child("Location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);

                getInfo(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //37.4219983!-122.084!Eoin McDonough!Message
    public void getInfo(String fromData){
        int[] amount =new int[3];
        //System.out.println("Tyestung");
        int j=0;

            for(int i=0;i<fromData.length();i++){

                if (fromData.charAt(i) == '!') {
                    System.out.println("Found ! at "+i);
                    amount[j] = i;
                    j++;
                }

        }
        String s1,s2,s3,s4;
        s1=fromData.substring(0,amount[0]);
        s2=fromData.substring(amount[0]+1,amount[1]);
        s3=fromData.substring(amount[1]+1,amount[2]);
        s4=fromData.substring(amount[2]+1);


        System.out.println("thing="+s1);
        //System.out.println(amount);
        System.out.println("thing="+s2);
        System.out.println("thing="+s3);
        System.out.println("thing="+s4);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}