package com.example.a100535658.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText Email;
    EditText Password;
    TextView Link;
    Button Login;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading login process
        progressBar=new ProgressDialog(this);

        //Firebase instance
        firebaseAuth= FirebaseAuth.getInstance();

        //Handles login and signup clicks
        LoginPressed();
        LinkPressed();
    }

    //Disable back click in Login Page
    @Override
    public void onBackPressed() {
        if (true) {
            //do nothing
        } else {
            super.onBackPressed();
        }
    }

    //Handles Login button click
    public void LoginPressed() {
        Email = (EditText) findViewById(R.id.input_email);
        Password = (EditText) findViewById(R.id.input_password);
        Login = (Button) findViewById(R.id.btn_login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starts form verification
                startNew();
            }
        });
    }

    //For verification for entered data
    private void startNew() {
        //Get input text
        String email = Email.getText().toString().trim();
        String password= Password.getText().toString().trim();

        //Data entry validation
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, R.string.enterEmail,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, R.string.enterPassword,Toast.LENGTH_LONG).show();
            return;
        }

        //Set progress bar
        progressBar.setMessage(getString(R.string.verifyCredentials));
        progressBar.show();

        //Handles incorrect data entered
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),Profile.class));

                }
                else {
                    Toast.makeText(MainActivity.this, R.string.wrongCredentials,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Handles Create Account Link pressed
    public void LinkPressed() {
        Link = (TextView) findViewById(R.id.text_link);
        Link.setText(R.string.one);

        Link.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startLink();
            }
        });
    }

    //Starts Signup activity
    private void startLink(){
        Intent intent2 = new Intent(this, Signup.class);
        startActivity(intent2);
    }

}