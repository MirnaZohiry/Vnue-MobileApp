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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

//Friendly Game class
public class FriendlyGame extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Layout for Navigation Drawer
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    int id;


    String fullName;
    TextView profile;

    Spinner sportsSpinner;
    Spinner teamSpinner;
    Spinner myTeamSpinner;
    EditText location;
    String place="";
    String choice=" ";
    String team="";
    String teamValue="";
    String myTeams;
    ArrayList<String> myList = new ArrayList<>();
    ArrayList<String> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_game);

        //Handles Navigation Drawer display
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sportsSpinner = (Spinner)findViewById(R.id.spinner_sport);
        teamSpinner = (Spinner)findViewById(R.id.friendly_game_spinner_opponentTeam);
        myTeamSpinner=(Spinner)findViewById(R.id.spinner3);
        location=(EditText)findViewById(R.id.friendly_game_field_location);

        populateSpinner();
        displayMyTeams();
        displayHeader();

        //Handles spinner clicks for sportSpinner
        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displaySpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Populates a list with array data
    private void populateSpinner() {
        String[] data = getResources().getStringArray(R.array.choices);
        ArrayList<String> arrayChoice = new ArrayList<String>(Arrays.asList(data));

        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(this,arrayChoice);
        sportsSpinner.setAdapter(customSpinnerAdapter);
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

    //display Spinner on screen
    private void displaySpinner() {
        choice = sportsSpinner.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference().child("team").orderByChild("sport").equalTo(choice).addListenerForSingleValueEvent(eventListener);
    }

    //Retrieve data from database
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            myList.clear();
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                teamValue = teams.child("teamName").getValue(String.class);
                myList.add(teamValue);
            }

            CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(FriendlyGame.this,myList);
            teamSpinner.setAdapter(customSpinnerAdapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    ValueEventListener eventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                myTeams = teams.getValue(String.class);
                teamList.add(myTeams);

            }

            CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(FriendlyGame.this,teamList);
            myTeamSpinner.setAdapter(customSpinnerAdapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    //display teams on screen
    private void displayMyTeams() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("teamName").addListenerForSingleValueEvent(eventListener2);
    }

    //Handels Choose Venue button
    public void chooseVenue(View view){
        place=location.getText().toString();
        team=teamSpinner.getSelectedItem().toString();
        Intent intent = new Intent(this,BookVenue.class);
        intent.putExtra("Location",place);
        intent.putExtra("Choice",choice);
        intent.putExtra("My Team",myTeamSpinner.getSelectedItem().toString());
        intent.putExtra("Team Chosen",team);
        startActivity(intent);

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

            //Starts CreateTeam activity class
            Intent intent_createTeam = new Intent(this,CreateTeam.class);
            startActivity(intent_createTeam);
        }
        if(id == R.id.joinTeam_click){

            //Starts JoinTeam activity class
            Intent intent_joinTeam = new Intent(this,MapsActivity.class);
            startActivity(intent_joinTeam);
        }
        if(id == R.id.activities_click){

            //Closes Navigation Drawer
            mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerlayout.closeDrawers();
        }
        if(id == R.id.logout_click){

            //Handles Logout
            Intent intent_logout = new Intent(this,MainActivity.class);
            startActivity(intent_logout);
        }


        return false;
    }

    //Display header in Navigation Drawer
    private void displayHeader() {
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(headerProfile);
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