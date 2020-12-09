package com.example.mapchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class otherProfile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user ;
    String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    FirebaseStorage storage = FirebaseStorage.getInstance();;
    private StorageReference mStorageRef;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Intent Privdata;
    ArrayList<String> userMessages = new ArrayList<>();
    FirebaseDatabase dataBase;
    DatabaseReference myRef;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = storage.getReferenceFromUrl("gs://mapchat-d7f34.appspot.com");
        user = getIntent().getStringExtra(Reading.userName);
        dataBase = FirebaseDatabase.getInstance();
        Button button = findViewById(R.id.button3);




        try {
           read();
        }
        catch (NullPointerException nully){
            newGuy();
            //myRef.
        }















        setContentView(R.layout.activity_otherprofile);

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

    public void newGuy(){
        String ts=Long.toString(System.currentTimeMillis());
        String tst = "";
        for(int i =0; i<13;i++) {
            tst+=(9- (Character.getNumericValue(ts.charAt(i))));

        }
        myRef.child(user).child(tst).setValue("Im new, Hello!");
        read();
    }

    public void read(){
        myRef = dataBase.getReference().child("Users").child(user);
        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if (dataSnapshot != null) {
                //if(dataSnapshot.exists)
                userMessages.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Map<String, String> dataset = new HashMap<String, String>();

                Map<Integer, String> mapS = (Map<Integer, String>) dataSnapshot.getValue();

                TreeMap<Integer, String> map = new TreeMap<>();
                try {
                    map.putAll(mapS);
                }
                catch(NullPointerException nully){
                }
                if (map.size() > 5) {
                    int j = 0;
                    for (String i : map.values()) {
                        Log.d(TAG, "Value is: " + i);
                        userMessages.add(i);
                        //getInfo(i,true);
                        j++;
                        if (j == 5) {
                            break;
                        }
                    }
                } else {
                    int j = 0;
                    for (String i : map.values()) {
                        //while(j<map.size()) {
                        Log.d(TAG, "Value is: " + i);
                        userMessages.add(i);
                        j++;
                        if (j == map.size()) {
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


    }

    public void updateData(){
        if(userMessages.size()>0) {
            TextView textView = findViewById(R.id.Message1);
            textView.setText(userMessages.get(0));


        }
        if(userMessages.size()>1) {
            TextView textView1 = findViewById(R.id.Message2);
            textView1.setText(userMessages.get(1));

        }
        if(userMessages.size()>2) {
            TextView textView2 = findViewById(R.id.Message3);
            textView2.setText(userMessages.get(2));

        }
        if(userMessages.size()>3) {
            TextView textView3 = findViewById(R.id.Message4);
            textView3.setText(userMessages.get(3));

        }
        if(userMessages.size()>4) {
            TextView textView4 = findViewById(R.id.Message5);
            textView4.setText(userMessages.get(4));

        }
    }

    public void uploadProfo(View view) {
       // if()//To set profile Image

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




