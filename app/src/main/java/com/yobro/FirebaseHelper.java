package com.yobro;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseHelper {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference userDataBaseRef;
    private FirebaseDatabase firebaseDatabase;
    private final StorageReference storageReference;
    private String userID;
    private String returnUri;

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
        userDataBaseRef = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    boolean checkIt()
    {
       return false;
    }

    public boolean saveUserData(UserProfile profileInfo){


        try{

            userDataBaseRef.child(userID).child("ProfileInfo").setValue(profileInfo);

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


}
