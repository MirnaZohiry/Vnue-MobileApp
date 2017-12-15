package com.example.a100535658.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainMessage extends AppCompatActivity {


    private FirebaseListAdapter<ChatMessage> adapter;
    //send button
    FloatingActionButton send;
    ListView listofMessage;
    String fullName;
    public String message;
    EditText input;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);
        //Get Full Name from TeamMembers
        fullName = getIntent().getStringExtra("Full Name");
        send = (FloatingActionButton) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                input = (EditText) findViewById(R.id.input);
                //create entry in database
                FirebaseDatabase.getInstance().getReference("messages").push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),message));
                //Show the User message sent
                Toast.makeText(MainMessage.this, "You Sent: "+input.getText().toString(), Toast.LENGTH_SHORT).show();

                input.setText("");

            }

        });

        Chat();


    }
    //Query To display chats from team member
    private void Chat() {
        Query q= FirebaseDatabase.getInstance().getReference().child("users").orderByChild("fullName").equalTo(fullName);
        q.addListenerForSingleValueEvent(eventListener);
    }


    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot teams : dataSnapshot.getChildren()) {
                message = teams.child("email").getValue(String.class);
            }
            //initialize list view
            listofMessage = (ListView) findViewById(R.id.list_of_message);


            adapter = new FirebaseListAdapter<ChatMessage>(MainMessage.this,ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference("messages").orderByChild("messageUser").equalTo(message))
            {
                @Override
                protected void populateView(View v, ChatMessage model, int position) {

                    //Get reference to the views of list_item
                    TextView messageText, messageUser, messageTime, messageReceiver;
                    messageText = (TextView) v.findViewById(R.id.message_text);
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);

                    //Set message in listview
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    //Time and Date Format
                    messageTime.setText(DateFormat.format("HH:mm",model.getMessageTime()));


                }
            };
            listofMessage.setAdapter(adapter);


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}