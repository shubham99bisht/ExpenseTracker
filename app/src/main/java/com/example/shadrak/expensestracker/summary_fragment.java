package com.example.shadrak.expensestracker;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class summary_fragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser User;
    String userId;
    DatabaseReference rootref, childref;
    ArrayList<DataSnapshot> bill;
    String[] category;
    Float[] amount;

    ArrayList<PieEntry> values;
    PieChart pieChart;

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.summary_fragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        User = firebaseAuth.getCurrentUser();
        assert User != null;
        userId = User.getUid();

        pieChart = rootview.findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(51f);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //rootref = database.getReference("Bills").child(userId);
        rootref = database.getReference().child("Bills").child(userId);

        values = new ArrayList<>();

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int size = (int) dataSnapshot.getChildrenCount();
                bill = new ArrayList<DataSnapshot>();
                category = new String[size+1];
                amount = new Float[size+1];
                String cat = "";
                Float price = 0f;

                int i = 0, j, index = 0, flag = 0, count = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    values.clear();
                    cat = data.child("category").getValue(String.class);
                    price = Float.valueOf(data.child("amount").getValue(String.class));
//                    Log.d("cat & price:","cat: "+ cat +" price: "+price);
                    for(j = 0; j <= i; j++) {
                        if (Objects.equals(cat, String.valueOf(category[j]))) { // checking if category is already present in array
                            Log.d("tp","2");
                            flag = 1;
                            index = j;
                            break;
                        }
                        Log.d("tp","1");
                    }
                    Log.d("flag","flag="+flag+" i="+i);
                    if (flag == 0) {
                        category[i] = cat;
                        amount[i] = price;
                        count++;
                        i++;
                    } else {
                        amount[index] = amount[index] + price;
                        flag = 0;
                    }
//                    Log.d("array","array-"+category[0]+" "+category[1]+" "+category[2]+" "+category[3]+" "+category[4]+" "+category[5]);
                }

                if(amount != null && category != null) {
                    for(i = 0; i < count; i++) {
                        Log.d("value of pie-entry","value - "+amount[i]);
                        values.add(new PieEntry(amount[i], category[i]));
                    }
                } else {
                    //Static values
                    values.add(new PieEntry(57f, "Food"));
                    values.add(new PieEntry(8f, "Medical"));
                    values.add(new PieEntry(25f, "travel"));
                }

//                Description description = new Description();
//                description.setText("This is PieChart");
//                description.setTextSize(15);
//                pieChart.setDescription(description);

                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                PieDataSet pieDataSet = new PieDataSet(values, "Names");
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.YELLOW);

                pieChart.setData(pieData);
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootview;
    }
}
