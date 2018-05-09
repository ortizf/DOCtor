package com.example.fedeortiz.doctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText mAddress;

    //textview where the log will be displayed
    //private TextView mLogTextView;
    //int we'll use to keep track of which step we are on for display
    private int mStep;
    //Key to yse to pass and retrieve which step we are on
    private String KEY_STEP = "stepcount";

    private static final boolean USE_FLAG = true;
    private static final int mFlag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAddress = (EditText)findViewById(R.id.addressText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent mIntent;
        switch (item.getItemId()) {
            case R.id.user:
                if (USE_FLAG)
                    mIntent = new Intent(this, LogInPage.class);
                    mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);

                startActivity(mIntent);
                //Toast.makeText(this, "User works", Toast.LENGTH_LONG).show();
                return true;
            case R.id.map:
                if (USE_FLAG)
                    mIntent = new Intent(this, Map2.class);
                    mIntent.addFlags(mFlag);

                mIntent.putExtra(KEY_STEP, mStep + 1);

                startActivity(mIntent);
                //Toast.makeText(this, "Map works", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle myData = getIntent().getExtras();
        if (myData == null) {
            mStep = 0;
        }
        else
            mStep = myData.getInt(KEY_STEP);

        //mLogTextView.setText(mLogTextView.getText() + " " + mStep);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney, Australia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    public void onMapToLogInButtonClick(View v) {
        Intent mIntent = new Intent(this, LogInPage.class);

        if (USE_FLAG)
            mIntent.addFlags(mFlag);

        mIntent.putExtra(KEY_STEP, mStep + 1);

        startActivity(mIntent);
    }

    public void searchButtonClick(View v) {
        double myLong, myLat;
        List<Address> myMatches = null;
        String myAddress;

        //Get the user indicated address
        myAddress = mAddress.getText().toString();


        //If the address is not null
        if(!myAddress.isEmpty()) {
            //Put in a try statement, since accessing the internet
            try {
                myMatches = new Geocoder(this).getFromLocationName(myAddress, 1);
            } catch (IOException e) {
                Toast.makeText(this, "Can't access Internet?", Toast.LENGTH_LONG).show();
            }
            //If a match was returned
            if (!myMatches.isEmpty()) {
                myLat = myMatches.get(0).getLatitude();
                myLong = myMatches.get(0).getLongitude();
                String address = myMatches.get(0).getAddressLine(0);

                //Set map focus to be this coordinate
                LatLng myLatLng = new LatLng(myLat, myLong);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
                mMap.addMarker(new MarkerOptions().position(myLatLng).title(address));
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_LONG).show();
        }
    }
}
