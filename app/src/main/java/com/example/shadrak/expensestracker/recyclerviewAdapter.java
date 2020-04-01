package com.example.shadrak.expensestracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

class recyclerviewAdapter extends RecyclerView.Adapter<recyclerviewAdapter.ViewHolder> {

    bills_fragment context;
    BillFormEditActivity edit_form;
    private static ArrayList<newBill> list;
    String amount, date, place, vendor, category, status, link, items, bill_id;
    newBill bill;
    Context context1;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference rootref;
    String userId;

    public recyclerviewAdapter(bills_fragment bills_fragment, ArrayList<newBill> list,Context context1)
    {
        this.context = bills_fragment;
        this.list = list;
        this.context1 = context1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dataitems, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i)
    {
        viewHolder.v_name.setText(list.get(i).getVendor());
        viewHolder.date.setText(list.get(i).getDate());
        viewHolder.cost.setText(list.get(i).getAmount());

//        bill = list.get(i);
//        Glide.with(context).load(bill.getLink()).into(viewHolder.icon);

        if(list.get(i).getStatus().equals("0")) {
            viewHolder.verified.setImageResource(R.drawable.ic_not_verified);
//            viewHolder.foreground.setCardBackgroundColor(R.color.cardcolor);
//            viewHolder.foreground.getResources().getColor(R.color.cardcolor);
        } else {
            viewHolder.verified.setImageResource(R.drawable.ic_verified_user);
//            viewHolder.submit.setEnabled(false);
        }
        viewHolder.foreground.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String bill_id = String.valueOf(list.get(i).getBillId());
                Intent intent = new Intent(context1,BillFormEditActivity.class);
                System.out.println("hdskjfh ieyroi:"+bill_id);
                intent.putExtra("bill_id",bill_id);
                context1.startActivity(intent);
            }
        });

        viewHolder.icon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String link_ =list.get(i).getLink();

                if (!link_.startsWith("http://") && !link_.startsWith("https://"))
                    link_ = "http://" + link_;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_));
                context.startActivity(browserIntent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static void removeItem(int position) {
        list.remove(position);
//        notifyItemRemoved(position);
    }

    public static void restoreItem(newBill bill, int position) {
        list.add(position, bill);
//        notifyItemInserted(position);
    }

    public void filter(final String cat) {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
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

                    if(cat.equals("None")){
                        list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));
                    } else if(cat.equals(category)){
                        Log.d("filter","else");
                        list.add(new newBill(bill_id,amount, date, place, vendor, category, status, link, items));
                    }

                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView v_name, date, cost, place, category;
        private ImageView verified, icon;
//        public RelativeLayout background;
        public CardView foreground;
        public Button submit;

        public ViewHolder(View view) {
            super(view);

            v_name = view.findViewById(R.id.vendor_name);
            date = view.findViewById(R.id.date);
            cost = view.findViewById(R.id.cost);
            verified = view.findViewById(R.id.verified);
            icon = view.findViewById(R.id.bill_icon);
//            submit = view.findViewById(R.id.edit_billSubmit);
//            background = view.findViewById(R.id.view_background);
            foreground = view.findViewById(R.id.view_foreground);

        }
    }
}
