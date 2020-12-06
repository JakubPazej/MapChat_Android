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

import java.util.HashMap;
import java.util.Map;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    FirebaseDatabase database;
    DatabaseReference myRef;
    String temp1,temp2,temp3,temp4;

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
                //Map<String, String> dataset = new HashMap<String, String>();
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                for (String i : map.values()) {
                    Log.d(TAG, "Value is: " + i);
                    getInfo(i);

                }
               //



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    //37.4219983!-122.084!Eoin McDonough!Message                 //This is what the data looks like, so we must split it to its components
    public void getInfo(String fromData){
        int[] amount =new int[3];
        //System.out.println("Tyestung");
        int j=0;

            for(int i=0;i<fromData.length();i++){

                if (fromData.charAt(i) == '!') {
                    System.out.println("Found ! at "+i);
                    amount[j] = i;
                    j++;
                    if(j==3){
                        break;
                    }
                }

        }

        temp1=fromData.substring(0,amount[0]);
        temp2=fromData.substring(amount[0]+1,amount[1]);
        temp3=fromData.substring(amount[1]+1,amount[2]);
        temp4=fromData.substring(amount[2]+1);
        addToMap(temp1,temp2,temp3,temp4);

        //System.out.println(temp1);
        //System.out.println(temp2);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        System.out.println(temp1);
        System.out.println(temp2);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //LatLng first = new LatLng(Double.parseDouble(temp1),Double.parseDouble(temp2));
        LatLng first = new LatLng(53,7.6);

        //mMap.addMarker(new MarkerOptions().position(first).title("temp4"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(first));
    }
    public void addToMap(String lat, String lon, String UserName,String message){
        LatLng map = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        mMap.addMarker(new MarkerOptions().position(map).title(message));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(map));
    }
}
