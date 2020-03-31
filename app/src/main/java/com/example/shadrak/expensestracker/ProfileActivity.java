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

public class ProfileActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public TextView username;
    public TextView email, since_when, bills_no;
    public String uid, uname, count;
    public DatabaseReference mdb;
    public FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.profile_name);
        email = findViewById(R.id.Email);
        since_when = findViewById(R.id.since_when);
        bills_no = findViewById(R.id.bills_uploaded);

        show();
    }

    private void show() {

        database = FirebaseDatabase.getInstance();
        mdb = database.getReference();
        FirebaseUser userdata = FirebaseAuth.getInstance().getCurrentUser();
        uid = userdata.getUid();
        mdb.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uname = dataSnapshot.child("Name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mdb.child("Bills").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = String.valueOf(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("random","count "+count+" username "+uname);
        Log.d("random","uid "+uid);
//        String uname = userdata.getDisplayName();
        String emaild = userdata.getEmail();
        String since = String.valueOf(userdata.getMetadata().getCreationTimestamp());
        System.out.println(userdata.getMetadata().getCreationTimestamp());
        since_when.setText(since);
        username.setText(uname);
        email.setText(emaild);
        bills_no.setText(count);

    }
}
