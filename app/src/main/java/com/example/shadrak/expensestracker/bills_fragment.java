package com.example.shadrak.expensestracker;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
//public class bills_fragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
public class bills_fragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    recyclerviewAdapter adapter;         //recyclerviewAdapter class
    DatabaseReference rootref;//firebase connections
//    private FirebaseRecyclerAdapter<newBill, MyViewHolder> firebaseRecyclerAdapter;        //firebase connections
    ArrayList<newBill> list;
    String amount, date, place, vendor, category, status, link, items, bill_id;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String userId;
    ImageView verified, icon;
    LinearLayout bills_fragment;

    private Drawable dlt_icon;
    private ColorDrawable background;

    FloatingActionButton fab;


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.bills_fragment, container, false);
        recyclerView = rootview.findViewById(R.id.bills_recyclerview);
        adapter = new recyclerviewAdapter(this, list,getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


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

//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//        populate_list(userId);
        populate_list(userId);


    }

    private void populate_list(String userId) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootref = database.getReference().child("Bills").child(userId);



        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    amount = data.child("amount").getValue().toString();
                    date = data.child("date").getValue().toString();
                    place = data.child("place").getValue().toString();
                    vendor = data.child("vendor").getValue().toString();
                    category = data.child("category").getValue().toString();
                    status = data.child("status").getValue().toString();
                    link = data.child("link").getValue().toString();
                    items = data.child("items").getValue().toString();
                    bill_id = data.getKey();

                    list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {
        if (viewHolder instanceof recyclerviewAdapter.ViewHolder)
        {
            String bill_id = list.get(position).getBillId();
//            final newBill deletedItem = list.get(viewHolder.getAdapterPosition());
//            final int deletedIndex = viewHolder.getAdapterPosition();

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
}
