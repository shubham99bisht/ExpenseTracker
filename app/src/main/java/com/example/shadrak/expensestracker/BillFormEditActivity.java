package com.example.shadrak.expensestracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BillFormEditActivity extends AppCompatActivity
{
    private EditText edit_vender,edit_address,edit_date,edit_category,edit_items,edit_amount;
    private Button edit_billSubmit;
    String billID;

    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userID;
    FirebaseUser user;
    DataSnapshot dataSnapshot;
    String date, amount, place, items, category, vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_form_edit);

        edit_vender=(EditText) findViewById(R.id.edit_vender);
        edit_address=(EditText) findViewById(R.id.edit_address);
        edit_date=(EditText) findViewById(R.id.edit_date);
        edit_category=(EditText) findViewById(R.id.edit_category);
        edit_items=(EditText) findViewById(R.id.edit_items);
        edit_amount=(EditText) findViewById(R.id.edit_amount);
        edit_billSubmit=(Button) findViewById(R.id.edit_billSubmit);

        Intent intent = getIntent();
        billID = intent.getExtras().getString("bill_id");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Bills").child(userID).child(billID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                amount = dataSnapshot.child("amount").getValue().toString();
                category = dataSnapshot.child("category").getValue().toString();
                date = dataSnapshot.child("date").getValue().toString();
                place = dataSnapshot.child("place").getValue().toString();
                vendor = dataSnapshot.child("vendor").getValue().toString();
                items = dataSnapshot.child("items").getValue().toString();
                System.out.println("testingData:"+amount +" "+category + " " +date);

                edit_vender.setText(vendor);
                edit_category.setText(category);
                edit_date.setText(date);
                edit_address.setText(place);
                edit_items.setText(items);
                edit_amount.setText(amount);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_billSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String vender_ = edit_vender.getText().toString().trim();
                final String address_ = edit_address.getText().toString().trim();
                final String date_ = edit_date.getText().toString().trim();
                final String category_ = edit_category.getText().toString().trim();
                final String items_ = edit_items.getText().toString().trim();
                final String amount_ = edit_amount.getText().toString().trim();

                if(vender_.equals("") || address_.equals("") || date_.equals("") || category_.equals("") || items_.equals("") || amount_.equals(""))
                {
                    Toast.makeText(BillFormEditActivity.this,"Kindly fill all the paramteres", Toast.LENGTH_SHORT).show();
                }
                else {

                    databaseReference.child("vendor").setValue(vender_);
                    databaseReference.child("place").setValue(address_);
                    databaseReference.child("date").setValue(date_);
                    databaseReference.child("category").setValue(category_);
                    databaseReference.child("items").setValue(items_);
                    databaseReference.child("amount").setValue(amount_);
                    databaseReference.child("status").setValue("1");

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();

                    Intent intent = new Intent(BillFormEditActivity.this,Homeactivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
