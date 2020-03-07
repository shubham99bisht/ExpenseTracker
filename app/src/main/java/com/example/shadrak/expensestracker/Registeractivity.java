package com.example.shadrak.expensestracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registeractivity extends AppCompatActivity {
    EditText name, emailid, passwd, confirmpasswd;
    Button signup;
    TextView loginlink;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_registeractivity);

        mFirebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.editText3);
        emailid = findViewById(R.id.editText5);
        passwd = findViewById(R.id.editText4);
        confirmpasswd = findViewById(R.id.editText6);
        loginlink = findViewById(R.id.loginlink);

        signup = findViewById(R.id.button4);

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registeractivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fname = name.getText().toString();
                final String email = emailid.getText().toString();
                final String pwd = passwd.getText().toString();
                String cpwd = confirmpasswd.getText().toString();
                if(fname.isEmpty() && email.isEmpty() && pwd.isEmpty() && cpwd.isEmpty()) {
                    Toast.makeText(Registeractivity.this,"Fields are Empty!!",Toast.LENGTH_SHORT).show();
                }
                else if(fname.isEmpty()) {
                    name.setError("Please enter your Name");
                    name.requestFocus();
                }
                else if(email.isEmpty()) {
                    emailid.setError("Please enter your EmailId");
                    emailid.requestFocus();
                }
                else if(pwd.isEmpty()) {
                    passwd.setError("Please enter your Password!!");
                    passwd.requestFocus();
                }
                else if(cpwd.isEmpty()) {
                    confirmpasswd.setError("Please enter your Password!!");
                    confirmpasswd.requestFocus();
                }
                else if(!pwd.equals(cpwd)) {
                    Toast.makeText(Registeractivity.this,"Passwords don't match!!",Toast.LENGTH_SHORT).show();
                }
                else if(!fname.isEmpty() && !email.isEmpty() && pwd.equals(cpwd)) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(Registeractivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(Registeractivity.this,"SignUp Unsuccessfull!!",Toast.LENGTH_SHORT).show();
                            } else {
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();

                                Toast.makeText(Registeractivity.this,"SignUp Successfull!!",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Registeractivity.this, Homeactivity.class);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                String uid = user.getUid();
                                int prev_id = 0000;
                                if(uid == null)
                                    Toast.makeText(getApplicationContext(), "Some Error occured!!",Toast.LENGTH_SHORT).show();
                                else {
                                    User usr = new User(uid, fname, email, prev_id);
                                    mDatabase.child("users").child(uid).setValue(usr);
                                    i.putExtra("uid", uid); // Passing data between activities
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }

                    });
                }
                else {
                      Toast.makeText(Registeractivity.this,"Error Occurred!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
