package com.example.mapchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

//import com.google.firebase.firestore.instance;
//import com.google.firebase.firestore.getId;


public class Reading extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase dataBase;
    DatabaseReference myRef;

    String user = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    String temp1,temp2,temp3,temp4;
    private static final String TAG = "";


    public static String Message = "ie.ul.myfirstapp.EXTRA_MESSAGE";
    public static ArrayList<String> values = new ArrayList<String>();
    DatabaseReference database;// = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataBase = FirebaseDatabase.getInstance();
        myRef =dataBase.getReference().child("Location");


        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                values.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Map<String, String> dataset = new HashMap<String, String>();
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                if(map.size()>5){
                    int j =0;
                    for (String i : map.values()) {
                         Log.d(TAG, "Value is: " + i);
                         getInfo(i);
                         j++;
                         if(j==5){
                             break;
                        }
                    }
                }
                else{
                    int j =0;
                    for (String i : map.values()) {
                        //while(j<map.size()) {
                         Log.d(TAG, "Value is: " + i);
                         getInfo(i);
                         j++;
                         if(j==map.size()){
                             break;
                         }
                    }
                }
                updateData();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




        setContentView(R.layout.activity_reading);

    }

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
        values.add(temp4);
        Log.d(TAG, "Values is: " + values);

        //updateData();


        //System.out.println(temp1);
        //System.out.println(temp2);


    }

    public void onClickPost(View view){


        Intent intent = new Intent(this,Post.class);

        startActivity(intent);
        //finish();
    }

    public void onClickProfile(View view) {
        Intent intent = new Intent(this,Profile.class);

        startActivity(intent);
    }

    public void onClickMap(View view) {
        Intent intent = new Intent(this,Maps.class);

        startActivity(intent);
    }


    public void update() {
        db = FirebaseFirestore.getInstance();
        values.clear();
        db.collection("Posts")
                //.orderBy("Posts", Query.Direction.DESCENDING)

                .get()



                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("1");
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot value : task.getResult()) {
                                System.out.println("2");
                                Message = value.getData().toString();
                                cleanUp();
                                values.add(Message);
                            }
                            System.out.println(values);

                            TextView welcome =findViewById(R.id.welcome);
                            welcome.setText("Welcome "+user+"!");

                            TextView profileClick =findViewById(R.id.profileClick);
                            profileClick.setText(user);

                            TextView textView = findViewById(R.id.textView2);
                            textView.setText(values.get(0));

                            TextView textView1 = findViewById(R.id.textView3);
                            textView1.setText(values.get(1));

                            TextView textView2 = findViewById(R.id.textView4);
                            textView2.setText(values.get(2));

                            TextView textView3 = findViewById(R.id.textView5);
                            textView3.setText(values.get(3));

                            TextView textView4 = findViewById(R.id.textView6);
                            textView4.setText(values.get(4));
                        }
                        else{
                            Log.w("tag", "Error getting Posts.",task.getException()) ;
                        }
                    }
                });



    }

    public void updateData(){
        //values.clear();
        TextView profileClick =findViewById(R.id.profileClick);
        profileClick.setText(user);
        TextView welcome =findViewById(R.id.welcome);
        welcome.setText("Welcome "+user+"!");
        if(values.size()>0) {
            TextView textView = findViewById(R.id.textView2);
            textView.setText(values.get(0));
        }
        if(values.size()>1) {
            TextView textView1 = findViewById(R.id.textView3);
            textView1.setText(values.get(1));
        }
        if(values.size()>2) {
            TextView textView2 = findViewById(R.id.textView4);
            textView2.setText(values.get(2));
        }
        if(values.size()>3) {
            TextView textView3 = findViewById(R.id.textView5);
            textView3.setText(values.get(3));
        }
        if(values.size()>4) {
            TextView textView4 = findViewById(R.id.textView6);
            textView4.setText(values.get(4));
        }

    }

    public void cleanUp(){
        String redo = "";
        char b = '=';
        char c = '}';
        boolean print = false;
        for(int i=0;i<Message.length();i++) {
            if(Message.charAt(i)==c ) {
                print = false;
            }

            if (print == true) {
                redo+=(Message.charAt(i));
            }

            if(Message.charAt(i)==b) {
                print = true;
            }
        }
        Message = redo;
    }

    public void postData(View view){
        System.out.println("postData");
        writeNewUser(userId,user,userEmail);
    }
    private void writeNewUser(String userId, String name, String email) {
        System.out.println(userId);
        System.out.println(name);
        System.out.println(email);

        User user = new User(name, email);
        System.out.println("Made User");

        database.child("users").child(userId).setValue(user);
        System.out.println("Added User");
    }





}