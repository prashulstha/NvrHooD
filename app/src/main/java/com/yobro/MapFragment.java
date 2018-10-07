package com.yobro;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yobro.JavaClasses.Coordinates;
import com.yobro.JavaClasses.CustomeInfoWindowAdapter;
import com.yobro.JavaClasses.FirebaseHelper;
import com.yobro.JavaClasses.UserProfile;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "map1";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 9001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9901;


    //Saving User Info in Global Variable
    private String userProfilePic;
    private String userName;
    private Uri parseduserProfilepic;

    //Google Map Variables
    private GoogleMap mMap;
    private Marker myMarker;

    //Google Map Request Integers
    private static final int MY_LOCATION_REQUEST_CODE = 9001;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    //Location Variables
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    GeofencingClient mGeoDataClient;
    //PlaceDetectionClient mPlaceDetectionClient;

    //Boolean Flag to save it's instance in the Bundle
    boolean mRequestingLocationUpdates;
    boolean mLocationPermissionGranted = false;
    static boolean calledAlready = false;


    //Saving the User Location as a Global Variable to make it accessible
    double latitude;
    double longitude;

    Dialog dialog;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    Context context = getActivity();

    private final static String TAG = "GOOGLE MAP ACTIVITY";
    private final static String key = "UserData";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    private View mapView;

    private Location mLastLocation;



    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;

    /**
     * The formatted location address.
     */
    private String mAddressOutput;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;

    /**
     * Displays the location address.
     */
    private TextView mLocationAddressTextView;
    FirebaseHelper firebaseHelper = new FirebaseHelper();


    //to retrieve array of cordinates from database
   // ArrayList<Coordinates> latlong = new ArrayList<>();
    private List<LatLng> locationList;
    private List<String> userKeys;
    DatabaseReference userDataBaseRef;
    DatabaseReference muserDataBaseRef;
    FirebaseDatabase firebaseDatabase;

     String mlatitude;
     String mlongitude;



    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();

            //User Info From Activity
            List<String> list = firebaseHelper.getUserInfo();
            if (list != null) {
                userName = list.get(0);
                userProfilePic = list.get(1);
                parseduserProfilepic = Uri.parse(userProfilePic);
            }
            else
            {
                userName = "User";
                userProfilePic = "https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg";
                parseduserProfilepic = Uri.parse(userProfilePic);
            }
        }
        dialog = new Dialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);



        // Construct a GeoDataClient.
        //mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        firebaseDatabase = FirebaseDatabase.getInstance();


        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            buildLocationRequest();
            buildLocationCallBack();


            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        }

        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressRequested = false;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);

        Switch aSwitch = view.findViewById(R.id.onlineSwtich);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    makeUserOnline();
                } else
                    makeUserOffline();
            }
        });



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                showSnackbar(mAddressOutput);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(myMarker.equals(marker)) {

            try{
                if(myMarker.isInfoWindowShown()){
                    myMarker.hideInfoWindow();
                }else{

                    myMarker.showInfoWindow();
                }
            }catch (NullPointerException e){
                Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
            }
        }

        return false;
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(com.yobro.Constants.RESULT_DATA_KEY);
            showSnackbar(mAddressOutput);

            // Show a toast message if an address was found.
            if (resultCode == com.yobro.Constants.SUCCESS_RESULT) {
                showSnackbar(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;

        }
    }

    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(com.yobro.Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(com.yobro.Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }




    private void makeUserOffline()
    {
        firebaseHelper.makeUserOffline();
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Set Offline", Snackbar.LENGTH_SHORT).show();
        myMarker.remove();
    }

    private void makeUserOnline()
    {


        String personLatitude = Double.toString(mLastLocation.getLatitude());
        String personLongitude = Double.toString(mLastLocation.getLongitude());



        Coordinates cord = new Coordinates(personLatitude, personLongitude);

        if(firebaseHelper.makeUserOnline(cord))
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Set Online", Snackbar.LENGTH_SHORT).show();


    }



    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(getActivity(), Double.toString(latitude) + " / " +Double.toString(longitude),     Toast.LENGTH_LONG).show();
                }
            }
        };

    }



    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setSmallestDisplacement(10);


    }


    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mRequestingLocationUpdates)

            stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void startLocationUpdates() {


        if (checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null /* Looper */);
        }
    }

    private boolean checkPermission() {

        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {

            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setOnMarkerClickListener(this);
            mMap.setInfoWindowAdapter(new CustomeInfoWindowAdapter(getActivity()));
            getDeviceLocation();
            if (mapView != null &&
                    mapView.findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 30);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    mMap.setMyLocationEnabled(true);

                }

            } else {
                // Permission was denied. Display an error message.

                Toast.makeText(getContext(), "Permission Denied",     Toast.LENGTH_LONG).show();
            }
        }
        else {
            mMap.setMyLocationEnabled(true);

        }

    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        final float DEFAULT_ZOOM = 16.0f;





        try {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        locationList = new ArrayList<>();

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(),
                                        location.getLongitude()), DEFAULT_ZOOM));

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        /*//to retrieve array of cordinates from database
                        ArrayList<Coordinates> latlong = new ArrayList<>();

                        latlong =firebaseHelper.getLatLong();
                        Coordinates mvalue = latlong.get(0);
                        String mlatitude = mvalue.getLatitude();
                        String mlongitude = mvalue.getLongitude();

                        latitude = Double.parseDouble(mlatitude);
                        longitude = Double.parseDouble(mlongitude);*/
                        mLastLocation = location;




                        userKeys = new ArrayList<>();


                        //When Map Loads Successfully
                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded()
                            {



                                userDataBaseRef = firebaseDatabase.getReference("Online Users");

                                userDataBaseRef.keepSynced(true);

                                muserDataBaseRef = firebaseDatabase.getReference("Users");
                                muserDataBaseRef.keepSynced(true);



                                userDataBaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                                    {
                                        for (DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            //User Info From Activity




                                            Coordinates cord = ds.getValue(Coordinates.class);

                                                mlatitude = cord.getLatitude();
                                                mlongitude = cord.getLongitude();

                                                latitude = Double.parseDouble(mlatitude);
                                                longitude = Double.parseDouble(mlongitude);
                                                locationList.add(new LatLng(latitude,longitude));


                                            final LatLng latLng = new LatLng(latitude, longitude);


                                            mMap.addMarker(new MarkerOptions().position(latLng).title("Title can be anything"));


                                            final String key = ds.getKey();
                                            userKeys.add(key);

                                            //function to get the information of users
                                            muserDataBaseRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot ds)
                                                {
                                                    if(ds.hasChild(key))
                                                    {
                                                        UserProfile proInfo = ds.child(key).child("ProfileInfo").getValue(UserProfile.class);

                                                            userProfilePic = proInfo.getPersonPhoto();
                                                            userName = proInfo.getPersonName();

                                                       //     userProfilePic = "https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg";

                                                        Uri parsedUri = Uri.parse(userProfilePic);


                                                        myMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(getActivity(),parsedUri, userName))));

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError)
                                                {

                                                }
                                            });
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        Toast.makeText(getContext(), "Coudlnt Load Locations.", Toast.LENGTH_SHORT).show();
                                    }




                                });

//                                mMap.addMarker(new MarkerOptions().position(locationList.get(0)).title("Title can be anything"));

                                /*

                                for(LatLng latLng: locationList) {
                                    mMap.addMarker(new MarkerOptions().position(latLng).title("Title can be anything"));


                                }
*/
                                if(locationList.size() > 0){
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(locationList.get(0)); //Taking Point B (Second LatLng) //Taking Point A (First LatLng)
                                    builder.include(locationList.get(locationList.size() - 1));
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                                    mMap.moveCamera(cu);
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                }



                            }
                        });
                    }
                });





        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }




    }




    @Override
    public void onMyLocationClick (@NonNull Location location){
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
        startIntentService();

    }

    @Override
    public boolean onMyLocationButtonClick () {
        Toast.makeText(getContext(), "You are here", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    public static Bitmap createCustomMarker(Context context, Uri imageUri, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        Picasso.get().load(imageUri).noFade().into(markerImage);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    private void showSnackbar(final String text) {
        View container = getActivity().findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }




}
