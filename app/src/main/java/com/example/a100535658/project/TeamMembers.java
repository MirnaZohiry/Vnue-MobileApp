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
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

//Activity displays all teams of user
public class TeamMembers extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Layout for Navigation Drawer
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    int id;

    TextView profile;

    TextView textView;
    ArrayList<String> membersList = new ArrayList<>();
    String userId,teamValue,result,first,last,teamClicked, fullName;
    String[] splitValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_members);

        //Handles Navigation Drawer display
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Allow clickable links in Navigation Drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView = (TextView) findViewById(R.id.label_teamName);
        teamClicked = getIntent().getStringExtra(getString(R.string.Team_Name));
        textView.setText(teamClicked + getString(R.string.team_members));
        displayTeamMembers();
        displayHeader();
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
        Log.i("ID is: ", id + "");

        if(id == R.id.profile_click){

            //Start Profile activity class
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
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.team_members)).orderByChild(getString(R.string.db_email)).equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addListenerForSingleValueEvent(headerProfile);
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

    //Retrieve Team members from Database
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                userId = teams.getKey();

                first=teams.child(getString(R.string.db_firstName)).getValue().toString();
                last=teams.child(getString(R.string.db_lastName)).getValue().toString();
                teamValue = teams.child(getString(R.string.db_teamName)).getValue().toString();

                if(teamValue.contains(getString(R.string.comma))){
                    splitValue = teamValue.split(getString(R.string.comma));

                    for(int i = 0; i < splitValue.length; i++){
                        result = splitValue[i].substring(splitValue[i].lastIndexOf(getString(R.string.equal)) + 1, splitValue[i].length());

                        if(i == splitValue.length - 1){
                            result = splitValue[i].substring(splitValue[i].lastIndexOf(getString(R.string.equal)) + 1, splitValue[i].length() - 1);
                        }

                        if(result.equals(teamClicked)){
                            membersList.add(first+getString(R.string.db_space)+last);
                        }
                    }
                }
                else if(teamValue.equals(getString(R.string.string_blank))){
                    //Skip this part
                }
                else{
                    result = teamValue.substring(teamValue.lastIndexOf(getString(R.string.equal)) + 1, teamValue.length() - 1);
                    if(result.equals(teamClicked)){
                        membersList.add(first+ " " + last);
                    }
                }
            }
            populateList((ListView) findViewById(R.id.listViewUsers));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //populate list of team members
    private void populateList(final android.widget.ListView listView) {
        final CustomListAdapter customListAdapter = new CustomListAdapter(this, membersList);
        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> labelAdapter, View source, int position, long id)
            {
                String memberName = (String) labelAdapter.getItemAtPosition(position);
                Intent intent = new Intent(TeamMembers.this,MainMessage.class);
                intent.putExtra(getString(R.string.Full_Name), memberName);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void displayTeamMembers() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).addListenerForSingleValueEvent(eventListener);
    }
}