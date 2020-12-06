package com.example.mapchat;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class User {
    private DatabaseReference mDatabase;


    String name;
    String email;
    Map<String, Boolean> friends;
    Bitmap ProfoPic;

    public User() {

    }

    public  User(String name, String email,Bitmap ProfoPic){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.name = name;
        this.email = email;
        this.ProfoPic=ProfoPic;
    }
    public User(String name,String email){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.name = name;
        this.email = email;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPic(Bitmap ProfoPic) {
        this.ProfoPic = ProfoPic;
    }
    public Bitmap getPic(){
        return ProfoPic;
    }
    public String getEmail(){
        return email;
    }
    public String getName(){
        return name;
    }

}