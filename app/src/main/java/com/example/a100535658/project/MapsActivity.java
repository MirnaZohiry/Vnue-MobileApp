package com.example.a100535658.project;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String teamValue,location,sport,id;
    double lat,lon;
    //HashMap to transfer information to JoinTeam page
    HashMap<Marker, Detail> detailMarkerMap= new HashMap<>();;
    ArrayList<String> myList = new ArrayList<>();
    private GoogleMap mMap;
    PlaceAutocompleteFragment placeAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //Search bar in map
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                displayTeamName();
            }

            @Override
            public void onError(Status status) {

                Log.d("Maps", "An error occurred: " + status);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // enable the zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //initialize constructor for marker details
                Detail detail=detailMarkerMap.get(marker);

                Intent intent = new Intent(MapsActivity.this,JoinTeam.class);
                intent.putExtra("id",detail.getId());
                intent.putExtra("Title",detail.getTeamValue());
                intent.putExtra("Location",detail.getLocation());
                intent.putExtra("sport",detail.getSport());
                startActivity(intent);

            }
        });

    }

    //query to get values of the marker
    private void displayTeamName() {
        FirebaseDatabase.getInstance().getReference().child("team").orderByChild("location").addListenerForSingleValueEvent(eventListener);
    }


    ValueEventListener eventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {

                id=teams.child("id").getValue(String.class);
                sport=teams.child("sport").getValue(String.class);
                location= teams.child("location").getValue(String.class);
                teamValue = teams.child("teamName").getValue(String.class);
                lat=teams.child("latitude").getValue(double.class);
                lon=teams.child("longitude").getValue(double.class);

                Detail detail = new Detail(id,lat,lon,sport,location,teamValue);
                //create latitude and longitude from the database
                LatLng team = new LatLng(lat, lon);
                Marker marker = mMap.addMarker(new MarkerOptions().position(team).title(teamValue));
                //add to hashmap
                detailMarkerMap.put(marker,detail);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

}