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
import android.widget.TextView;

public class BookingSummary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    int id;

    TextView teamUsed, secondTeam,dateStarted,startTime,endTime,venueSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        summary();

    }

    public void summary(){
        teamUsed=(TextView)findViewById(R.id.teamUsed);
        secondTeam=(TextView)findViewById(R.id.secondTeam);

        dateStarted=(TextView)findViewById(R.id.dateStarted);
        startTime=(TextView)findViewById(R.id.startTime);
        endTime=(TextView)findViewById(R.id.endTime);

        venueSelected=(TextView)findViewById(R.id.venueSelected);

        teamUsed.setText(getIntent().getStringExtra("My Team"));
        secondTeam.setText(getIntent().getStringExtra("Opponent"));

        dateStarted.setText(getIntent().getStringExtra("Date"));
        startTime.setText(getIntent().getStringExtra("Starting Time"));
        endTime.setText(getIntent().getStringExtra("End Time"));

        venueSelected.setText(getIntent().getStringExtra("Venue"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        id = item.getItemId();
        Log.i("ID is: ", id + "");

        if(id == R.id.profile_click){

            Intent intent_profile = new Intent(this,Profile.class);
            startActivity(intent_profile);

        }
        if(id == R.id.createTeam_click){

            Intent intent_createTeam = new Intent(this,CreateTeam.class);
            startActivity(intent_createTeam);
        }
        if(id == R.id.joinTeam_click){

            Intent intent_joinTeam = new Intent(this,MapsActivity.class);
            startActivity(intent_joinTeam);
        }
        if(id == R.id.activities_click){

            Intent intent_friendlyGame = new Intent(this,FriendlyGame.class);
            startActivity(intent_friendlyGame);
        }
        if(id == R.id.logout_click){

            Intent intent_logout = new Intent(this,MainActivity.class);
            startActivity(intent_logout);
        }


        return false;
    }

}