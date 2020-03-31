package com.example.shadrak.expensestracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class BillFormEditActivity extends AppCompatActivity
{
    private EditText edit_vender,edit_address,edit_date,edit_category,edit_items,edit_amount;
    private Button edit_billSubmit;
    String billID;
    Spinner category_spin;

    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, rootref;
    FirebaseAuth firebaseAuth;
    String userID;
    FirebaseUser user;
    DataSnapshot dataSnapshot;
    String date, amount, place, items, category, cat, vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_form_edit);

        // Spinner code -------- Starts
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> cate = new ArrayList<>();
                category_spin = findViewById(R.id.category);

                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    String cat = categorySnapshot.getValue(String.class);
                    cate.add(cat);
                }

                ArrayAdapter<String> cat_adap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cate);
                cat_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category_spin.setAdapter(cat_adap);

                category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        cat = (String) adapterView.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Spinner code ---------- Ends

        edit_vender=(EditText) findViewById(R.id.edit_vender);
        edit_address=(EditText) findViewById(R.id.edit_address);
        edit_date=(EditText) findViewById(R.id.edit_date);
//        edit_category=(EditText) findViewById(R.id.edit_category);
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
                amount = dataSnapshot.child("Amount").getValue().toString();
                category = dataSnapshot.child("Category").getValue().toString();
                date = dataSnapshot.child("Date").getValue().toString();
                place = dataSnapshot.child("Address").getValue().toString();
                vendor = dataSnapshot.child("Company").getValue().toString();
                items = dataSnapshot.child("Items").getValue().toString();
                System.out.println("testingData:"+amount +" "+category + " " +date);

                edit_vender.setText(vendor);
//                edit_category.setText(category);
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
//                final String category_ = edit_category.getText().toString().trim();
                final String items_ = edit_items.getText().toString().trim();
                final String amount_ = edit_amount.getText().toString().trim();

                if(vender_.equals("") || address_.equals("") || date_.equals("") || cat.equals("") || items_.equals("") || amount_.equals(""))
                {
                    Toast.makeText(BillFormEditActivity.this,"Kindly fill all the paramteres", Toast.LENGTH_SHORT).show();
                }
                else {

                    databaseReference.child("Company").setValue(vender_);
                    databaseReference.child("Address").setValue(address_);
                    databaseReference.child("Date").setValue(date_);
                    databaseReference.child("Category").setValue(cat);
                    databaseReference.child("Items").setValue(items_);
                    databaseReference.child("Amount").setValue(amount_);
                    databaseReference.child("Status").setValue("1");

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
