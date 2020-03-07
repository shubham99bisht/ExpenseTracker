package com.example.shadrak.expensestracker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    public TextView email;
    public String uid, uname;
    public DatabaseReference mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //data passed
//        Bundle bundle = getIntent().getExtras();
//        assert bundle != null;
//        uid = bundle.getString("uid");

        username = findViewById(R.id.profile_name);
        email = findViewById(R.id.Email);
        show();
    }

    private void show() {

//        mdb = FirebaseDatabase.getInstance().getReference();
        FirebaseUser userdata = FirebaseAuth.getInstance().getCurrentUser();
//        mdb.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //uname = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        String uname = userdata.getDisplayName();
        String emaild = userdata.getEmail();
        username.setText(uname);
        email.setText(emaild);

    }
}
