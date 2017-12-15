package com.example.a100535658.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/*
References:
    PlaceHolder Fragment: https://developers.google.com/places/android-api/autocomplete
    Custom Spinner: http://abhiandroid.com/ui/custom-spinner-examples.html
 */
public class CreateTeam extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Layout for Navigation Drawer
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    int id;
    String fullName;
    TextView profile;

    EditText teamName;
    double lat;
    double lon;

    //Firebase instance
    FirebaseUser currentFirebaseUser;
    FirebaseAuth firebaseAuth;
    //Database Reference
    DatabaseReference databaseTeam;
    DatabaseReference databaseUser;
    CharSequence location;

    Spinner sportsSpinner;
    //Search Fragment
    PlaceAutocompleteFragment placeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        //Handles Navigation Drawer display
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Allow clickable links in Navigation Drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        teamName=(EditText)findViewById(R.id.input_teamName);

        //Populate Spinner for sports type
        sportsSpinner = (Spinner)findViewById(R.id.spinnerCustom);
        populateSpinner();

        //Reference database
        databaseUser= FirebaseDatabase.getInstance().getReference(getString(R.string.users));
        databaseTeam=FirebaseDatabase.getInstance().getReference(getString(R.string.db_team));
        firebaseAuth=firebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Handles PlaceHolder fragment
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng queriedLocation = place.getLatLng();
                lat= queriedLocation.latitude;
                lon= queriedLocation.longitude;
                location=place.getName();
            }

            @Override
            public void onError(Status status) {
                Log.d(getString(R.string.Maps), getString(R.string.errorOccured) + status);
            }
        });

        displayHeader();
    }

    //Handles Spinner
    private void populateSpinner() {
        // populate spinner
        String[] data = getResources().getStringArray(R.array.choices);
        ArrayList<String> arrayChoice = new ArrayList<String>(Arrays.asList(data));

        //Displays in a custom spinner
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(this,arrayChoice);
        sportsSpinner.setAdapter(customSpinnerAdapter);

        //Handles spinner clicks
        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    //Handles CREATE button click
    public void createTeam(View view){
        addTeam();
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }

    //Adds new teams to database
    public void addTeam(){

        String choice= sportsSpinner.getSelectedItem().toString();
        String tn= teamName.getText().toString().trim();
        String uid= currentFirebaseUser.getUid();

        String id = databaseTeam.push().getKey();

        Team team = new Team(id,choice,tn,lat,lon,location,0f,0f,0f);

        databaseTeam.child(id).setValue(team);
        databaseUser.child(uid).child("teamName").child(id).setValue(tn);

    }

    //Handles Open/Close Navigation Drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Handles Options clicks in Navigation Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        id = item.getItemId();

        if(id == R.id.profile_click){

            //Starts Profile activity class
            Intent intent_profile = new Intent(this,Profile.class);
            startActivity(intent_profile);

        }
        if(id == R.id.createTeam_click){

            //Closes Navigation drawer when clicked
            mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerlayout.closeDrawers();
        }
        if(id == R.id.joinTeam_click){

            //Starts JoinTeam activity class
            Intent intent_joinTeam = new Intent(this,MapsActivity.class);
            startActivity(intent_joinTeam);
        }
        if(id == R.id.activities_click){

            //Starts FriendlyGame activity class
            Intent intent_friendlyGame = new Intent(this,FriendlyGame.class);
            startActivity(intent_friendlyGame);
        }
        if(id == R.id.logout_click){
            //Handles Logout
            Intent intent_logout = new Intent(this,MainActivity.class);
            startActivity(intent_logout);
        }
        return false;
    }

    //Display header in Navigation Drawer
    public void displayHeader() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).orderByChild(getString(R.string.email)).equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(headerProfile);
    }
    //Retrieve data from Database
    ValueEventListener headerProfile = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {

                fullName = teams.child(getString(R.string.fullName)).getValue(String.class);
                profile=(TextView)findViewById(R.id.profileUser);
                profile.setText(fullName);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}