package com.example.a100535658.project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BookVenue extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    int id;

    String fullName;
    TextView profile;

    EditText edit1;
    String locationTent;
    Spinner venueSpinner;
    String venue;
    String opponentTeam;
    String myTeam;
    ArrayList<String> myList = new ArrayList<>();
    Calendar myCalendar;
    EditText edittext,txtTime;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog timePickerDialog;
    Calendar c;
    int mDay, mHour, mMinute;
    private EditText timeFieldEnd;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_venue);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        edit1=(EditText)findViewById(R.id.book_venue_field_location);
        venueSpinner = (Spinner)findViewById(R.id.book_venue_spinner_venue);
        timeFieldEnd = (EditText)findViewById(R.id.book_venue_field_time_end);
        locationTent= getIntent().getStringExtra("Location");
        opponentTeam=getIntent().getStringExtra("Team Chosen");
        myTeam=getIntent().getStringExtra("My Team");
        edit1.setText(locationTent);
        myCalendar = Calendar.getInstance();
        c=Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        showDate();
        showTime();
        displaySpinner();
        displayHeader();
    }

    public void showDate(){
        edittext= (EditText) findViewById(R.id.book_venue_field_date);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BookVenue.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    public void showTime(){
        txtTime=(EditText)findViewById(R.id.book_venue_field_time_start);
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);

        txtTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MMM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    private void displaySpinner() {

        FirebaseDatabase.getInstance().getReference().child("Venue").orderByChild("venueLocation").equalTo(locationTent).addListenerForSingleValueEvent(eventListener);
    }

    ValueEventListener eventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            myList.clear();
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                venue = teams.child("venueName").getValue(String.class);
                Log.i("Snapshot Value", venue);
                myList.add(venue);
            }
            populateSpinner();

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    public void populateSpinner(){
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(this,myList);
        venueSpinner.setAdapter(customSpinnerAdapter);
        venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    public void endTimePick() {
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeFieldEnd.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, false);

        timePicker.show();

    }
    public void clickedTimeFieldEnd(View view) {
        endTimePick();
    }

    public void clickedBookedVenue(View view){

        Intent intent = new Intent(this,BookingSummary.class);
        intent.putExtra("My Team",myTeam);
        intent.putExtra("Opponent",opponentTeam);
        intent.putExtra("Location",locationTent);
        intent.putExtra("Venue", venueSpinner.getSelectedItem().toString());
        intent.putExtra("Date",edittext.getText().toString());
        intent.putExtra("Starting Time",txtTime.getText().toString());
        intent.putExtra("End Time",timeFieldEnd.getText().toString());

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