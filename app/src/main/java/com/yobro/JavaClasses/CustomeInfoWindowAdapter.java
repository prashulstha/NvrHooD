package com.yobro.JavaClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yobro.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomeInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private Geocoder geocoder;
    private double markerLatitude,markerLongitude;
    private double mlatitude = 0.0, mlongitude = 0.0;



    DatabaseReference muserDataBaseRef;
    FirebaseDatabase firebaseDatabase;

    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ArrayList<String> retrieveInfo = new ArrayList<>();
    SharedPreferences preferences;

    public CustomeInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_popup, null);
        geocoder = new Geocoder(context, Locale.getDefault());
        preferences = context.getSharedPreferences("YoBro", Context.MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        muserDataBaseRef = firebaseDatabase.getReference("Users");
    }

    private void rendowWindowText(Marker marker, final View view){
        TextView dialogName = view.findViewById(R.id.DailogName);
        TextView dialogAddress = view.findViewById(R.id.DailogAddress);
        final TextView dialogActivity = view.findViewById(R.id.daialogActivity);
        final EditText dialogEditActivity = view.findViewById(R.id.daialogEditActivity);

        CircleImageView imageView = view.findViewById(R.id.profile_image);

        LatLng cordToAdd = marker.getPosition();
        String addressInString = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(cordToAdd.latitude, cordToAdd.longitude, 1);
            addressInString = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String activity = preferences.getString("Activity", "Tell us what you are doing.");


        dialogActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActivity.setVisibility(View.INVISIBLE);
                dialogEditActivity.setVisibility(View.VISIBLE);

                String act = dialogActivity.getText().toString().trim();

                preferences.edit().putString("Activity", act).apply();
            }
        });

        activity = preferences.getString("Activity", "Tell us what you are doing.");
        if(getUserProfile()){
            dialogName.setText(retrieveInfo.get(0));
            dialogActivity.setText(activity);
            Uri parseduserProfilepic = Uri.parse(retrieveInfo.get(1));
            Picasso.get().load(parseduserProfilepic).noFade().into(imageView);
        }


        dialogAddress.setText(addressInString);
    }



    @Override
    public View getInfoWindow(Marker marker)
    {
        rendowWindowText(marker, mWindow);
        LatLng latLng = marker.getPosition();

        markerLatitude= latLng.latitude;
        markerLongitude=latLng.longitude;
        Log.d(Double.toString(markerLatitude),Double.toString(markerLongitude));
        Log.d("Checking","latitudes of marker");
        final ArrayList<String> userInfo = new ArrayList<>();



        muserDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Coordinates cord = ds.getValue(Coordinates.class);




                    if (cord != null) {
                        String latitude = cord.getLatitude();
                        String longitude = cord.getLongitude();

                        mlatitude = Double.parseDouble(latitude);
                        mlongitude = Double.parseDouble(longitude);

                    }
                    else
                        Toast.makeText(mContext, "Error getting Location", Toast.LENGTH_SHORT).show();


                    Log.d(Double.toString(mlatitude),Double.toString(mlongitude));
                    Log.d("Checking","latitudes inside loop");

                    if (mlatitude==markerLatitude && mlongitude==mlongitude)
                    {
                        final String key = ds.getKey();
                        Log.d(key,"this is key");

                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(mContext, "Error getting Data", Toast.LENGTH_SHORT).show();
            }
        });


        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    private boolean getUserProfile() {


        retrieveInfo = firebaseHelper.getUserInfo();
        if(retrieveInfo!= null)
            return true;
        else
            return false;

    }
}
