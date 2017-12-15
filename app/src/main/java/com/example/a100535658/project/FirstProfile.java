package com.example.a100535658.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
References:
    Navigation Drawer: https://github.com/mikepenz/MaterialDrawer
    PieChart Drawer: https://github.com/PhilJay/MPAndroidChart
*/

//Profile for first time entry users
public class FirstProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Layout for Navigation Drawer
    DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    String fullName;
    TextView profile;

    int id;
    //Scores for the Pie Chart
    float score[] = {100f};
    String scoretype[] = {"No Score"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_profile);

        //Handles Navigation Drawer display
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Allow clickable links in Navigation Drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setup for the Pie Chart
        setupPieChart();

        //Display Header in Navigation Drawer
        displayHeader();
    }

    //Handles Pie Chart Setup
    private void setupPieChart() {

        //populate a list of Pie entries
        List<PieEntry> pieEntry = new ArrayList<>();
        pieEntry.add(new PieEntry(score[0], scoretype[0]));

        //Set Pie colors
        PieDataSet dataSet = new PieDataSet(pieEntry, getString(R.string.space));
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        PieData data = new PieData(dataSet);

        //Get the chart
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
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

            //Closes Navigation drawer when clicked
            mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerlayout.closeDrawers();
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

    //Handles Join Team button click
    public void join(View view){
        Intent joinTeam= new Intent(this,MapsActivity.class);
        startActivity(joinTeam);
    }

    //Handles Create Team button click
    public void create(View view){
        Intent createTeam= new Intent(this,CreateTeam.class);
        startActivity(createTeam);

    }
}