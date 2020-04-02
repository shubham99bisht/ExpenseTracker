package com.example.shadrak.expensestracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class bills_fragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    recyclerviewAdapter adapter;         //recyclerviewAdapter class
    DatabaseReference rootref;//firebase connections

    ArrayList<newBill> list;
    String amount, date, place, vendor, category, status, link, items, bill_id;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String userId, cat;
    Spinner category_spin;
    Boolean flag = false;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.bills_fragment, container, false);
        recyclerView = rootview.findViewById(R.id.bills_recyclerview);
        adapter = new recyclerviewAdapter(this, list,getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String memberSince = simpleDateFormat.format(new Date());
//        Log.d("random", "Current Timestamp: " + memberSince);

        // Spinner code -------- Starts
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> cate = new ArrayList<>();
                category_spin = rootview.findViewById(R.id.bills_category);

                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    String cat = categorySnapshot.getValue(String.class);
                    cate.add(cat);
                }

                ArrayAdapter<String> cat_adap = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cate);
                cat_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category_spin.setAdapter(cat_adap);

                category_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        cat = (String) adapterView.getSelectedItem();
                        cat = new String(adapterView.getItemAtPosition(i).toString());
                        Log.d("spinner","value "+cat);
                        adapter.filter(cat);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
//                        adapter.filter("none");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Spinner code ---------- Ends

        return rootview;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        list = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootref = database.getReference().child("Bills").child(userId);


        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    amount = data.child("Amount").getValue().toString();
                    date = data.child("Date").getValue().toString();
                    place = data.child("Address").getValue().toString();
                    vendor = data.child("Company").getValue().toString();
                    category = data.child("Category").getValue().toString();
                    status = data.child("Status").getValue().toString();
                    link = data.child("Link").getValue().toString();
                    items = data.child("Items").getValue().toString();
                    bill_id = data.getKey();

//                    if(flag){
//                        list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));
//                    } else if(category.equals(cat)) {
//                        list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));
//                    }
                    list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, final int position)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        if (viewHolder instanceof recyclerviewAdapter.ViewHolder)
                        {
                            String bill_id = list.get(position).getBillId();
                            if(bill_id != null)
                            {
                                // remove the item from recycler view
                                recyclerviewAdapter.removeItem(viewHolder.getAdapterPosition());

                                firebaseAuth = FirebaseAuth.getInstance();
                                user = firebaseAuth.getCurrentUser();
                                String userID = user.getUid();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Bills").child(userID).child(bill_id);
                                databaseReference.removeValue();
                            }

                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Log.d("random", "No");
//                        String bill_id = list.get(position).getBillId();
                        newBill bill = new newBill();
                        recyclerviewAdapter.restoreItem(bill, viewHolder.getAdapterPosition());
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to Delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

//        if (viewHolder instanceof recyclerviewAdapter.ViewHolder)
//        {
//            String bill_id = list.get(position).getBillId();
//            if(bill_id != null)
//            {
//                // remove the item from recycler view
//                recyclerviewAdapter.removeItem(viewHolder.getAdapterPosition());
//
//                firebaseAuth = FirebaseAuth.getInstance();
//                user = firebaseAuth.getCurrentUser();
//                String userID = user.getUid();
//                firebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Bills").child(userID).child(bill_id);
//                databaseReference.removeValue();
//            }
//
//        }
    }
}
