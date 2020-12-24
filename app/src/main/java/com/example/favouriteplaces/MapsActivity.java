package com.example.favouriteplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    FloatingActionButton save, help;

    //Defining the user defined method
    private void getTheLocation(Location location, String title)
    {
        if (location != null)
        {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,19));
        }
    }

    //Checking for permission results after user grants permission & receiving current location info from line #49 if location permission was granted
    //This method is for one time execute only i.e. this method will execute only when user grants permission after being asked at first time run. It will be ignored from the rest of time.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5,locationListener);

                //Redirecting the pointer at user's current location. These two lines will make the application to work smoother by instant pointing at location at map
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                getTheLocation(lastLocation,"Your Current Location");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        save= findViewById(R.id.savedLocationsFloatingActionButton);
        help= findViewById(R.id.helpFloationgActionButton);


        /***************** START: CHECKING IF INTERNET & GPS ARE ENABLED AT THE START OF APP EVERY TIME *****************/
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            showAlertMessageForNoGPS();
        }
        if (isNetworkAvailable()==false)
        {
            showAlertMessageForNoInternet(); //Userdefined method to check if GPS is enabled
        }
        /***************** END: CHECKING IF INTERNET & GPS ARE ENABLED AT THE START OF APP EVERY TIME *****************/

        /***************** Onclick events start *****************/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getApplicationContext(), MyPinnedLocations.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this); //Pass any view to continue
                builder.setTitle("Tips");
                builder.setMessage("Press & hold on a location on map to pin it as your 'Memorable Place'(s) ");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
        /***************** Onclick events end *****************/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /**** To select & pin a location on map, use this code below (line #113), then type GoogleMap.OnMapLongClickListener at the start of main class (here line #33)
         //         after "OnMapReadyCallback" (for this case) & Implement method "onMapLongClick" (Line #162) outside of "OnMapReady" method ****/
        mMap.setOnMapLongClickListener(this);

        Intent intent= getIntent();
        //Checking if 0th item of the listview was selected. If yes, then zoom in on user's current location
        if (intent.getIntExtra("place_number",0)==0)
        {
            //Zoom in on user's current location
            locationManager= (LocationManager) this.getSystemService(LOCATION_SERVICE);
            locationListener= new LocationListener() {
                @Override
                public void onLocationChanged(Location location)
                {
                    //user defined method to find location
                    getTheLocation(location,"You are here");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            //Checking if location access permission was not granted at start of application
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5,locationListener);

                //Redirecting the pointer at user's current location. These two lines will make the application to work smoother by instant pointing at location at map
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                getTheLocation(lastLocation,"Your Current Location");
            }
        }
//        else
//        {
//            Location placeInfo = new Location(LocationManager.GPS_PROVIDER);
//            placeInfo.setLatitude(MainActivity.locations.get(intent.getIntExtra("place_number",0)).latitude);
//            placeInfo.setLongitude(MainActivity.locations.get(intent.getIntExtra("place_number",0)).longitude);
//
//            getTheLocation(placeInfo, MainActivity.places.get(intent.getIntExtra("place_number",0)));
//        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault()); //Fetches the current address info of the user
        String address = "";
        try
        {
            //This will return maximum 1 address information of the current location of the user
            List<Address> list_of_addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            if (list_of_addresses != null && list_of_addresses.size() > 0)
            {
                //This will show address of the current location & it will show the first result of the list.
                if (list_of_addresses != null && list_of_addresses.size() > 0)
                {
                    /* Display Process: This process will filter specific information, i.e. customized data */
                    if (list_of_addresses.get(0).getThoroughfare() != null) //Street name etc.
                    {
                        if (list_of_addresses.get(0).getSubThoroughfare() != null) //A particular area of the street name etc.
                        {
                            address += list_of_addresses.get(0).getSubThoroughfare()+", ";
                        }
                        address += list_of_addresses.get(0).getThoroughfare();
                    }

                    //if address is not found by googlemap
                    if (address.equals("") || address.equals("Unnamed Road"))
                    {
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm dd-MM-yyyy");
                        address = simpleDateFormat.format(new Date());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        addRecord(address, latLng);
    }

    private void addRecord(String address, LatLng latLng)
    {
        String res= new SQLiteDB_Manager(this).addRecord(address, latLng.toString());

        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
    }

    /************************************ Implementation of the userdefined methods start ************************************/
    private void showAlertMessageForNoGPS()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this); //Pass any view to continue
        builder.setTitle("GPS Disabled");
        builder.setMessage("This application requires GPS to run. Enable GPS?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAlertMessageForNoInternet()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this); //Pass any view to continue
        builder.setTitle("No Connection");
        builder.setMessage("This application requires Internet Connection to run. Enable Internet Connection?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
            }
        })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /************************************ Implementation of user defined methods end ************************************/

}