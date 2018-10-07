package com.yobro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yobro.JavaClasses.Coordinates;
import com.yobro.JavaClasses.FirebaseHelper;
import com.yobro.JavaClasses.UserProfile;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.yobro.MapFragment.calledAlready;

public class MapActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase Variables
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private Toolbar mToolbar;

    //Button
    Switch onlineBtn;


    //Fragment
    Fragment fragment = new MapFragment();

    //UserProfile Object to Store Retrieved user Info from Database
    private final static String key = "UserData";
    ArrayList<String> retrieveInfo = new ArrayList<>();


    //Navigation Bar Variables
    TextView user_Name;
    TextView user_Email;
    CircleImageView userProfileView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_home);


        //Loading the Default Fragment

         firebaseDatabase = FirebaseDatabase.getInstance();
         databaseReference = firebaseDatabase.getReference("Users");
         mAuth = FirebaseAuth.getInstance();
         String uId = mAuth.getUid();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

         boolean checkFirstTime = sharedPreferences.getBoolean("Islogin", false);


         //checkFirstTime = "No";

         if(!checkFirstTime)
         {
             fragment = new FindHobby();
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction Replace = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
             Replace.commit();
             //Start the Map Activity
             sharedPreferences.edit().putBoolean("Islogin", true).apply();
         }
        else{
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction Replace = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment);
             Replace.commit();

         }

// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        user_Name = headerView.findViewById(R.id.userFirstName);
        user_Email = headerView.findViewById(R.id.userEmail);
        userProfileView = headerView.findViewById(R.id.userProfilePic);

        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser == null){
            Intent loginIntent = new Intent(MapActivityHome.this, LoginAct.class);
            startActivity(loginIntent);
            finish();
        }else
            getUserProfile();
    }

    private void getUserProfile() {


        retrieveInfo = firebaseHelper.getUserInfo();
        Bundle bundle = new Bundle();

        user_Name.setText(retrieveInfo.get(0));
        user_Email.setText(retrieveInfo.get(2));
        Uri uri = Uri.parse(retrieveInfo.get(1));
        Picasso.get()
                .load(uri)
                .noFade()
                .into(userProfileView);
        bundle.putStringArrayList(key, retrieveInfo);
        fragment.setArguments(bundle);

    }

    private void makeUserOffline() {
        Snackbar.make(findViewById(android.R.id.content), "Set Offline", Snackbar.LENGTH_SHORT).show();
    }

    private void makeUserOnline() {

        String personLatitude = "53.00";
        String personLongitude = "-7.77832031";

        Coordinates cord = new Coordinates(personLatitude, personLongitude);

        //if(firebaseHelper.makeUserOnline(cord))
            Snackbar.make(findViewById(android.R.id.content), "Set Online", Snackbar.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            // Handle the camera action
            //startActivity(new Intent(this, GoogleMaps.class));
            fragment = new MapFragment();


        } else if (id == R.id.nav_setting) {


        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_explore) {

        } else if (id == R.id.nav_signout) {

            signOutUser();
        }

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction Replace = fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment );
        //Replace.addToBackStack(null).commit();

        item.setChecked(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //
            // item.setIconTintMode(R.color.colorAccent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void signOutUser() {

        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        Snackbar.make(findViewById(android.R.id.content), "Sign Out Successful", Snackbar.LENGTH_SHORT).show();
                        Intent homeMapIntent = new Intent(MapActivityHome.this, LoginAct.class);
                        startActivity(homeMapIntent);

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!calledAlready)
        {
            firebaseDatabase = FirebaseDatabase.getInstance();
//            firebaseDatabase.setPersistenceEnabled(true);
            calledAlready = true;
        }
    }
}
