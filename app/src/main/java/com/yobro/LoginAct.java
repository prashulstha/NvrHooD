package com.yobro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yobro.JavaClasses.FirebaseHelper;
import com.yobro.JavaClasses.UserProfile;

public class LoginAct extends BaseActivity implements View.OnClickListener {

    //Variables Deceleration
    private String TAG = "Sign In Activity";


    private static final int RC_SIGN_IN = 7;
    private GoogleSignInClient mGoogleSignInClient;

    //Firebase Variables
    FirebaseAuth mAuth;
    DatabaseReference userDatabase;
    FirebaseHelper firebaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login2);

        // Button listeners
        findViewById(R.id.gSignInButton).setOnClickListener(this);
        findViewById(R.id.SignoutBtn).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("/Users");  //Getting Reference to the Child User of the Firebase
        userDatabase.keepSynced(true);   //Keeps the Database Synced
        // [END initialize_auth]


        //Snackbar.make(findViewById(android.R.id.content),"Sign in Error",Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        // [START_EXCLUDE silent]


        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                              updateUI();
                            //Start the Map Activity
                            Intent homeMapIntent = new Intent(LoginAct.this, MapActivityHome.class);
                            startActivity(homeMapIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.signin_Layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });
    }
    //End auth_with_google

    private void updateUI() {


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String personLatitude = "53.2734";
            String personLongitude = "-7.77832031";
            String userProfileUpload;




            //Firebase Helper Object Deceleration
            //Firebase Helper to Upload the Object to the Firebase Database and into the Firebase Storage
            firebaseHelper = new FirebaseHelper();

            //Check if the Person PP is not Null

            //Getting the returned URL from storing the user Profile pic in Firebase Storage
                //userProfileUpload  = firebaseHelper.saveProfilePic(personPhoto);
            userProfileUpload = personPhoto.toString();


            //Saving all the Data into an Object
            if (!userProfileUpload.equals(" ")) {

                UserProfile Profile_Info = new UserProfile(personName, personGivenName, personFamilyName, personEmail, personId, userProfileUpload, personLatitude, personLongitude);



                if (firebaseHelper.saveUserData(Profile_Info))
                    Snackbar.make(findViewById(R.id.signin_Layout), "Authentication Success." , Snackbar.LENGTH_SHORT).show();



                else
                    Snackbar.make(findViewById(R.id.signin_Layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

            }
            else
                Snackbar.make(findViewById(R.id.signin_Layout), " Couldn't Upload Picture.", Snackbar.LENGTH_SHORT).show();

        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        /*FindHobby fragment = new FindHobby();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction Replace = fragmentManager.beginTransaction().replace(R.id.signin_Layout, fragment );
        Replace.addToBackStack(null).commit();*/
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.gSignInButton) {
            signIn();
        }
        else if(i == R.id.SignoutBtn){
            showProgressDialog();
            signOut();

    }

    }
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        hideProgressDialog();
                        Snackbar.make(findViewById(R.id.signin_Layout), "Sign Out Successful", Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        }
}