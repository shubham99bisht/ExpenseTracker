package com.example.shadrak.expensestracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ProfileActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public TextView username;
    public TextView email, since_when, bills_no;
    public String uid, uname, count, emailid, since;
    public DatabaseReference user_ref, bill_ref;
    public FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.profile_name);
        email = findViewById(R.id.Email);
        since_when = findViewById(R.id.since_when);
        bills_no = findViewById(R.id.bills_uploaded);

//        show();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("users").child(uid);
        bill_ref = database.getReference().child("Bills").child(uid);

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uname = dataSnapshot.child("Name").getValue().toString();
                emailid = dataSnapshot.child("Email").getValue().toString();
                since = dataSnapshot.child("Member_since").getValue().toString();
                username.setText(uname);
                email.setText(emailid);
                since_when.setText(since);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bill_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = String.valueOf(dataSnapshot.getChildrenCount());
                bills_no.setText(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
