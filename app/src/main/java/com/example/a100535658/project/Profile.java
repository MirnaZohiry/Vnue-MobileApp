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
import android.widget.AdapterView;
import android.widget.ListView;
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

//Profile for existing users
public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Layout for Navigation Drawer
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    String message, fullName;
    int id;
    float win,draw,loss;

    ArrayList<String> myList = new ArrayList<>();

    //Firebase instance
    private FirebaseAuth firebaseAuth;

    TextView profile;
    //Scores for the Pie Chart
    float score[]={};
    String scoretype[] = {"Win", "Loss", "Tie"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get Firebase Instance
        firebaseAuth= FirebaseAuth.getInstance();

        //Handles Navigation Drawer display
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Allow clickable links in Navigation Drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayHeader();
        displayTeams();
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
            Intent intent_createTeam = new Intent(Profile.this,CreateTeam.class);
            startActivity(intent_createTeam);
        }
        if(id == R.id.joinTeam_click){

            //Starts JoinTeam activity class
            Intent intent_joinTeam = new Intent(Profile.this,MapsActivity.class);
            startActivity(intent_joinTeam);
        }
        if(id == R.id.activities_click){

            //Starts FriendlyGame activity class
            Intent intent_friendlyGame = new Intent(Profile.this,FriendlyGame.class);
            startActivity(intent_friendlyGame);
        }
        if(id == R.id.logout_click){

            //Handles Logout
            Intent intent_logout = new Intent(Profile.this,MainActivity.class);
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

    //Display user Team List
    private void displayTeams() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.teamName)).addListenerForSingleValueEvent(ListTeams);
    }
    //Retrieve data from Database
    ValueEventListener ListTeams = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                message = teams.getValue(String.class);
                myList.add(message);
                displayResults();
            }
            populateList((ListView) findViewById(R.id.listViewTeams));
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    //Populate List of user teams
    private void populateList(final ListView listView) {
        final TeamListAdapter teamListAdapter = new TeamListAdapter(this, myList);
        listView.setAdapter(teamListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> customListAdapter, View source, int position, long id)
            {
                String value = (String) customListAdapter.getItemAtPosition(position);
                Intent intent = new Intent(Profile.this,TeamMembers.class);
                intent.putExtra(getString(R.string.Team_Name), value);
                startActivityForResult(intent, 1);
            }
        });
    }

    //Retrieve the data for Pie Chart
    private void displayResults() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.team)).orderByChild(getString(R.string.teamName)).equalTo(message).addListenerForSingleValueEvent(drawPie);
    }
    ValueEventListener drawPie = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                win += teams.child(getString(R.string.db_win)).getValue(float.class);
                loss += teams.child(getString(R.string.db_loss)).getValue(float.class);
                draw += teams.child(getString(R.string.db_draw)).getValue(float.class);
            }

            score= new float[]{win, loss, draw};
            //Draw Pie Chart
            setupPieChart();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    //Handles Pie Chart Setup
    private void setupPieChart() {
        //populate a list of Pie entries
        List<PieEntry> pieEntry = new ArrayList<>();
        PieChart chart = (PieChart) findViewById(R.id.chart);
        for(int i =0; i < score.length; i++){

            //If no games and score for user
            if (score[0]==0f && score[1]==0f && score[2]==0){
                score[0]=100f;
                scoretype[0]= "No Score";
                pieEntry.add(new PieEntry(score[0], scoretype[0]));

                PieDataSet dataSet = new PieDataSet(pieEntry, getString(R.string.space));
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                //Get the chart
                chart.setData(data);
                chart.getDescription().setEnabled(false);
                chart.invalidate();
                break;

            }
            //if user has scores
            else {
                scoretype[0]= "Win";
                pieEntry.add(new PieEntry(score[i], scoretype[i]));
                PieDataSet dataSet = new PieDataSet(pieEntry, getString(R.string.space));
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                //Get the chart
                chart.setData(data);
                chart.getDescription().setEnabled(false);
                chart.animateY(1000);
                chart.invalidate();
            }
        }}
}