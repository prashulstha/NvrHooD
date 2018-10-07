package com.yobro.JavaClasses;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.yobro.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomeInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    Geocoder geocoder;

    public CustomeInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_popup, null);
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    private void rendowWindowText(Marker marker, View view){
        TextView dialogName = view.findViewById(R.id.DailogName);
        TextView dialogAddress = view.findViewById(R.id.DailogAddress);

        CircleImageView imageView = view.findViewById(R.id.profile_image);

        LatLng cordToAdd = marker.getPosition();
        String addressInString = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(cordToAdd.latitude, cordToAdd.longitude, 1);
            addressInString = addresses.get(0).getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Picasso.get().load(parseduserProfilepic).noFade().into(imageView);
        dialogAddress.setText(addressInString);
    }



    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
