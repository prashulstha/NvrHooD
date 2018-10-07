package com.yobro.JavaClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ArrayList<String> retrieveInfo = new ArrayList<>();
    SharedPreferences preferences;
    double markerLatitude, markerLongitude;

    public CustomeInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_popup, null);
        geocoder = new Geocoder(context, Locale.getDefault());
        preferences = context.getSharedPreferences("YoBro", Context.MODE_PRIVATE);
    }

    private void rendowWindowText(Marker marker, final View view){
        TextView dialogName = view.findViewById(R.id.DailogName);
        TextView dialogAddress = view.findViewById(R.id.DailogAddress);
        final TextView dialogActivity = view.findViewById(R.id.daialogActivity);
        final EditText dialogEditActivity = view.findViewById(R.id.daialogEditActivity);
        Button updateActivity = view.findViewById(R.id.updateActivity);

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
                dialogActivity.setVisibility(View.GONE);
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
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        LatLng latLng = marker.getPosition();
        markerLatitude = latLng.latitude;
        markerLongitude = latLng.longitude;

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
