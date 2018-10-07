package com.yobro.JavaClasses;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseHelper {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userDataBaseRef;

    private FirebaseDatabase firebaseDatabase;
    private final StorageReference storageReference;
    private String userID;
    private String returnUri;
    UserProfile profileFromDatabse;



    private FirebaseAuth getmAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    private FirebaseUser getmUser() {
        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            userID = mUser.getUid();
        }
        return mUser;
    }

    public FirebaseHelper(){

        getmAuth();
        getmUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();


    }



    public boolean saveUserData(UserProfile profileInfo){


        userDataBaseRef = firebaseDatabase.getReference("Users");

        try{

            userDataBaseRef.child(userID).child("ProfileInfo").setValue(profileInfo);

            return true;

        }catch (DatabaseException e){
            e.printStackTrace();
            return false;
        }

    }


   public void saveHobby(String hobby)
   {
       userDataBaseRef =firebaseDatabase.getReference("Users");
       try{
           userDataBaseRef.child(userID).child("Hobby List").setValue(hobby);

       }catch (DatabaseException e)
       {
           e.printStackTrace();
       }
   }

    public boolean saveLocation(Coordinates mcordinates)
    {
        String mlatitude, mlongtitude;
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!"); */

        userDataBaseRef = firebaseDatabase.getReference("Users");
        try{

            userDataBaseRef.child(userID).child("Location").setValue(mcordinates);

            return true;

        }catch (DatabaseException e){
            e.printStackTrace();
            return false;
        }


    }



    public String saveProfilePic(final Uri UserProfilePic){



        //saving the reference to the Database

        storageReference.child("Profile Photo").child(userID).child(UserProfilePic.getLastPathSegment() );

        storageReference.putFile(UserProfilePic)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Get a URL to the uploaded Profile Pic
                       Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        returnUri = downloadUrl.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //
                        returnUri = " ";
                    }
                });
        return returnUri;

    }



    public boolean makeUserOnline(Coordinates cord){

        userDataBaseRef = firebaseDatabase.getReference("Online Users");


        try{

            userDataBaseRef.child(userID).setValue(cord);

            return true;

        }catch (DatabaseException e){
            e.printStackTrace();
            return false;
        }

    }

    public void makeUserOffline()
    {
        userDataBaseRef = firebaseDatabase.getReference("Online Users");
        try
        {
            userDataBaseRef.child(userID).removeValue();
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getUserInfo(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        userDataBaseRef = firebaseDatabase.getReference();
        ArrayList<String> userInfo = new ArrayList<>();

        FirebaseAuth mauth = getmAuth();
        FirebaseUser user = getmUser();
        final String id = user.getUid();


        userInfo.add(mUser.getDisplayName());
        Uri photoUri = mUser.getPhotoUrl();
        userInfo.add(photoUri.toString());
        userInfo.add(mUser.getEmail());

        return userInfo;
    }


    public ArrayList<String> getUserInformation(final String userID){

        firebaseDatabase = FirebaseDatabase.getInstance();
        userDataBaseRef = firebaseDatabase.getReference("Users");
        final ArrayList<String> userInfo = new ArrayList<>();

        FirebaseAuth mauth = getmAuth();
        FirebaseUser user = getmUser();
        final String id = user.getUid();

        userDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID))
                {
                    Log.d(userID,"Checking if it is userID");
                    UserProfile proInfo = dataSnapshot.child(userID).getValue(UserProfile.class);

                    userInfo.add(proInfo.getPersonName());
                    userInfo.add(proInfo.getPersonPhoto());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userInfo.add(mUser.getDisplayName());
        Uri photoUri = mUser.getPhotoUrl();
        userInfo.add(photoUri.toString());
        userInfo.add(mUser.getEmail());

        return userInfo;
    }





}
