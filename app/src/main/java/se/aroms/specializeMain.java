package se.aroms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import se.aroms.Devdroids.order_queue;
import se.aroms.DevelopersDotCo.CustomRecyclerViewOrder;

public class specializeMain extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomRecyclerViewOrder adapter;
    private DatabaseReference DB;
    private ArrayList<order_queue> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialize_main);

        orders = new ArrayList<>();
        DB= FirebaseDatabase.getInstance().getReference().child("Orders Queue");
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    orders.add(postSnapshot.getValue(order_queue.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter = new CustomRecyclerViewOrder(orders,R.layout.kitchen_manager_order_row,this);
        recyclerView = (RecyclerView) findViewById(R.id.OrderDisplay);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}



