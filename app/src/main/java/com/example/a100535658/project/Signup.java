package com.example.a100535658.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Activity for New Users Signing Up
public class Signup extends AppCompatActivity {

    EditText firstName, lastName, email, password;
    String fs, ls, mail, pw;

    private FirebaseAuth firebaseAuth;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    //Reference of Database
    DatabaseReference databaseUser;

    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialize Progress Bar for signup
        progressBar=new ProgressDialog(this);

        //Firebase instance
        firebaseAuth= FirebaseAuth.getInstance();

        //Reference "users" tree in database
        databaseUser= FirebaseDatabase.getInstance().getReference(getString(R.string.users));

        firstName=(EditText)findViewById(R.id.first);
        lastName=(EditText)findViewById(R.id.last);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
    }

    //Handles Sign Up button click
    public void firstProfile(View view){
        registerUser();
    }

    //Add new user to Database under Unique Key
    private void addUser(){
        //Get user input entered
        fs = firstName.getText().toString().trim();
        ls = lastName.getText().toString().trim();
        mail = email.getText().toString();
        pw = password.getText().toString().trim();

        //Create Unique Key for user
        String uid = currentFirebaseUser.getUid();

        //User FullName initialization
        String fullName = fs + " " + ls ;

        //Add User data to Database
        User user = new User(uid, fs, ls, fullName, mail, pw, "");
        databaseUser.child(uid).setValue(user);
    }

    //Verify new user entered data
    private void registerUser(){
        //Get user input entered
        fs = firstName.getText().toString().trim();
        ls = lastName.getText().toString().trim();
        mail = email.getText().toString();
        pw = password.getText().toString().trim();

        if (TextUtils.isEmpty(fs)){
            Toast.makeText(this, R.string.enter_first_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ls)){
            Toast.makeText(this, R.string.enter_last_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mail)){
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pw)){
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }

        //Set progress bar
        progressBar.setMessage(getString(R.string.register_user));
        progressBar.show();

        //Handles incorrect user entered data
        firebaseAuth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                //starts Profile activity if correct data entered
                if(task.isSuccessful()){
                    Toast.makeText(Signup.this, R.string.registered,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Signup.this, FirstProfile.class);
                    startActivity(intent);

                    addUser();
                }else {
                    Toast.makeText(Signup.this, R.string.noSuccess,Toast.LENGTH_SHORT).show();
                }
                progressBar.dismiss();
            }

        });
    }
}