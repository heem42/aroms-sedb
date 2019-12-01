package se.aroms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class order_details extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private order_adapter mAdapter;
    TextView tv;
    private List<order> mUploads;
    private DatabaseReference mDatabase;
    private int i2;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        mRecyclerView = findViewById(R.id.rcv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUploads = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        final List<order> Orders;// list of orders
        final List<myorder> o;///////
        Orders=new ArrayList<>();

        o=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Orders Queue").orderByChild("order_status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Orders.clear();
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final order n=postSnapshot.getValue(order.class);
                    Orders.add(n);
                    int s= Integer.valueOf( n.getOrder_status());
                    if(s!=2)// not complete
                    {

                        int size = n.getOrderItems().size();
                        for (int ii = 0; ii <size; ii++) {
                            i2=ii;
                            final item i = n.getOrderItems().get(ii);
                            final String ordind=n.getOrderId();
                            final int index=ii;
                            final   myorder m = new myorder(n.getOrderId(), i.getItemID(),ii);
                            o.add(m);
                            ///////////////////////////////////chef read "for dish 1 "
                            final DatabaseReference chefQueue=FirebaseDatabase.getInstance().getReference().child("chef");
                            chefQueue.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                    for (DataSnapshot postSnapshot1 : dataSnapshot1.getChildren() ) {

                                        chef_list n1=postSnapshot1.getValue(chef_list.class);



                                        DatabaseReference An = FirebaseDatabase.getInstance().getReference("chef");
                                        String uploadId = An.push().getKey();
                                        String key =postSnapshot1.getKey();
                                        Long queue=n1.getQueue();
                                        if (i.getStatus().equalsIgnoreCase("-1"))//check status -1 //find chef details and remove it
                                        {

                                            //     Toast.makeText(getBaseContext(), "status -1!!!!!!!!!!!"+i.getStatus(), Toast.LENGTH_SHORT).show();

                                            for (DataSnapshot postSnapshot5 : postSnapshot1.child("dishes").getChildren()) {
                                                String d = postSnapshot5.child("dish").getValue(String.class);
                                                String or = postSnapshot5.child("order_id").getValue(String.class);
                                                //         Toast.makeText(getBaseContext(), "delete  item " + i.getItemID() + "order item id" + d, Toast.LENGTH_SHORT).show();
                                                if ((d.equalsIgnoreCase(i.getItemID())) && (or.equalsIgnoreCase(ordind))) {
                                                    //           Toast.makeText(getBaseContext(), "asjdkasdk" + key, Toast.LENGTH_SHORT).show();
                                                    DatabaseReference An1 = FirebaseDatabase.getInstance().getReference("chef");
                                                    An1.child(key).child("dishes").child(postSnapshot5.getKey()).removeValue();
                                                    //new line
                                                    An1.child(key).child("queue").setValue(queue-1);

                                                    final myorder m3 = new myorder(n.getOrderId(), i.getItemID(), i2);
                                                    o.remove(m3);
                                                    int indexofm3=o.indexOf(m3);
                                                    mAdapter = new order_adapter(order_details.this, o);
                                                    mAdapter.notifyDataSetChanged();
                                                    mRecyclerView.setAdapter(mAdapter);

                                                }

                                            }
                                        }
                                        else if((n1.speciality.equalsIgnoreCase("desi"))  && queue <30 )// <10
                                        {
                                            dish_list2 l=new dish_list2(i.getItemID(),ordind);
                                            //Log.d(TAG, "$METHOD_NAME$: $content$");
                                            Toast.makeText(getBaseContext(), "q+1", Toast.LENGTH_SHORT).show();

                                            An.child(key).child("dishes").child(uploadId).setValue(l);
                                            An.child(key).child("queue").setValue(queue+1);
                                            queue++;
                                            DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                                            reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("status").setValue("2");
                                            String dishtime=findtime(i.getItemID());
                                            int caltime=Integer.parseInt(n1.getTime())+Integer.parseInt(dishtime);
                                            reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("required_time").setValue(Integer.toString(caltime));
                                            An.child(key).child("time").setValue(Integer.toString(caltime));
//
                                        }
                                        else {
                                            DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                                            reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("status").setValue("5");
                                            //                                          FirebaseDatabase.getInstance().getReference().child("Orders Queue").child(m.orderid).child("orderItems").child(i.getItemID()).child("status").setValue("5");//not assigned
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ///////////////////////////////////chef read
                            mAdapter = new order_adapter(order_details.this, o);
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        Orders.clear();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void loadbalance(View view) {


        final List<order> Orders;// list of orders
        final List<myorder> o;///////
        final DatabaseReference orderQueue = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
        Orders = new ArrayList<>();

        o = new ArrayList<>();
        orderQueue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Orders.clear();
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final order n = postSnapshot.getValue(order.class);
                    Orders.add(n);
                    //r
                    //  orderQueue.child(n.getOrderId()).child("order_status").setValue("0");
                    int s = Integer.valueOf(n.getOrder_status());
                    if (s != 2)// not complete
                    {
                        int size = n.getOrderItems().size();
                        for (int ii = 0; ii < size; ii++) {
                            final item i = n.getOrderItems().get(ii);
                            final String ordind = n.getOrderId();
                            final int index = ii;
                            myorder m = new myorder(n.getOrderId(), i.getItemID(), ii);
                            //,i.;
                            //   if(m.complete!=2)
                            o.add(m);
                            if (i.getStatus().equalsIgnoreCase("5")) {
                                ///////////////////////////////////chef read "for dish 1 "
                                final DatabaseReference chefQueue = FirebaseDatabase.getInstance().getReference().child("chef");
                                chefQueue.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                        for (DataSnapshot postSnapshot1 : dataSnapshot1.getChildren()) {

                                            chef_list n1 = postSnapshot1.getValue(chef_list.class);


                                            DatabaseReference An = FirebaseDatabase.getInstance().getReference("chef");
                                            String uploadId = An.push().getKey();
                                            String key = postSnapshot1.getKey();
                                            Long queue = n1.getQueue();
                                            if (queue < 30)// <10
                                            {

                                                dish_list2 l = new dish_list2(i.getItemID(), ordind);

                                                An.child(key).child("dishes").child(uploadId).setValue(l);
                                                Long q = queue + 1;
                                                An.child(key).child("queue").setValue(q+1);

                                                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                                                reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("status").setValue("0");
                                                String dishtime = findtime(i.getItemID());
                                                int caltime = Integer.parseInt(n1.getTime()) + Integer.parseInt(dishtime);
                                                reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("required_time").setValue(Integer.toString(caltime));
                                                An.child(key).child("time").setValue(Integer.toString(caltime));
                                                // RadioGroup radioButton = findViewById(R.id.rg1);


                                            }
                                            //FirebaseDatabase.getInstance().getReference().child("Orders Queue").child(n.getOrderId()).child("orderItems").child(i.getItemID()).child("status").setValue("5");//not assigned

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                ///////////////////////////////////chef read

                            }
                        }
                        Orders.clear();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void callchef(View view){
        Intent intent = new Intent(this, chef.class);
        startActivity(intent);
    }
    public void callorder(View view){
        Intent intent = new Intent(this, order_details.class);
        startActivity(intent);
    }

    String time="0";
    public String findtime(final String dish1) {

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().getReference().child("Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot1) {
                for (DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    String u=ds1.getKey();
                    if(u.equalsIgnoreCase(dish1)){

                        time= (ds1.child("time").getValue(String.class));



                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        time="28";
        return time;
    }
}
