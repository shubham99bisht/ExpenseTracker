package com.example.shadrak.expensestracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.bumptech.glide.Glide;

class recyclerviewAdapter extends RecyclerView.Adapter<recyclerviewAdapter.ViewHolder> {

    bills_fragment context;
    private static ArrayList<newBill> list;
    newBill bill;
    int index;
    Context context1;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i)
    {
        viewHolder.v_name.setText(list.get(i).getVendor());
        viewHolder.date.setText(list.get(i).getDate());
        viewHolder.cost.setText(list.get(i).getAmount());

        bill = list.get(i);
        Glide.with(context).load(bill.getLink()).into(viewHolder.icon);

        if(list.get(i).getStatus().equals("0")) {
            viewHolder.verified.setImageResource(R.drawable.ic_not_verified);
        } else {
            viewHolder.verified.setImageResource(R.drawable.ic_verified_user);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView v_name, date, cost, place, category;
        private ImageView verified, icon;
//        public RelativeLayout background;
        public CardView foreground;
//        public FrameLayout frameLayout;


        public ViewHolder(View view) {
            super(view);

            v_name = view.findViewById(R.id.vendor_name);
            date = view.findViewById(R.id.date);
            cost = view.findViewById(R.id.cost);
            verified = view.findViewById(R.id.verified);
            icon = view.findViewById(R.id.bill_icon);
//            background = view.findViewById(R.id.view_background);
            foreground = view.findViewById(R.id.view_foreground);
//            frameLayout = view.findViewById(R.id.framelayout);
//            place = view.findViewById(R.id.place);
//            category = view.findViewById(R.id.category);

        }
    }
}
