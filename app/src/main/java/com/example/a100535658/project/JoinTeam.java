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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class JoinTeam extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    int id;
    float win,draw,loss;
    float score[]={};
    String scoretype[] = {"Win", "Loss", "Tie"};

    TextView profile;
    String fullName;

    EditText sportView,titleView,locationView;
    DatabaseReference databaseUser;
    FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        databaseUser= FirebaseDatabase.getInstance().getReference("users");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        sportView=(EditText) findViewById(R.id.textView10);
        titleView=(EditText) findViewById(R.id.textView12);
        locationView=(EditText) findViewById(R.id.textView14);

        sportView.setText(getIntent().getStringExtra("sport"));
        titleView.setText(getIntent().getStringExtra("Title"));
        locationView.setText(getIntent().getStringExtra("Location"));
        displayHeader();
        displayResults();


    }
    //query to get results
    private void displayResults() {
        FirebaseDatabase.getInstance().getReference().child("team").orderByChild("teamName").equalTo(getIntent().getStringExtra("Title")).addListenerForSingleValueEvent(drawPie);
    }

    ValueEventListener drawPie = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {


                win += teams.child("win").getValue(float.class);
                loss += teams.child("lost").getValue(float.class);
                draw += teams.child("draw").getValue(float.class);

            }
            //get stats of team
            score= new float[]{win, loss, draw};
            setupPieChart();


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };



    //setup the pie chart according to results pie
    private void setupPieChart() {
        //populate a list of Pie entries
        List<PieEntry> pieEntry = new ArrayList<>();
        PieChart chart = (PieChart) findViewById(R.id.chart);
        for(int i =0; i < score.length; i++){

            if (score[0]==0f && score[1]==0f && score[2]==0){
                score[0]=100f;
                scoretype[0]="No Score";
                pieEntry.add(new PieEntry(score[0], scoretype[0]));

                PieDataSet dataSet = new PieDataSet(pieEntry, "");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                chart.setData(data);
                chart.getDescription().setEnabled(false);
                chart.invalidate();
                break;

            }else {

                scoretype[0]="Win";
                pieEntry.add(new PieEntry(score[i], scoretype[i]));
                PieDataSet dataSet = new PieDataSet(pieEntry, "");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                //Get the chart

                chart.setData(data);
                chart.getDescription().setEnabled(false);
                chart.animateY(1000);
                chart.invalidate();
            }
        }

    }

    public void onClick(View view){
        Intent intent= new Intent(this,Profile.class);
        String uid= currentFirebaseUser.getUid();
        //Create Team In Database
        databaseUser.child(uid).child("teamName").child(getIntent().getStringExtra("id")).setValue(getIntent().getStringExtra("Title"));
        startActivity(intent);

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

            //This added in each page, it's own option
            mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerlayout.closeDrawers();
        }
        if(id == R.id.activities_click){

            Toast.makeText(this, "Friendly Game", Toast.LENGTH_SHORT).show();

            Intent intent_friendlyGame = new Intent(this,FriendlyGame.class);
            startActivity(intent_friendlyGame);
        }
        if(id == R.id.logout_click){

            Intent intent_logout = new Intent(this,MainActivity.class);
            startActivity(intent_logout);
        }

        return false;
    }
    //Display header in Navigation Drawer
    private void displayHeader() {
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