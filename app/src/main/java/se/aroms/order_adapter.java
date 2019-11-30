package se.aroms;
import com.example.aroms.dish_list2;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class order_adapter extends RecyclerView.Adapter<order_adapter.orderViewHolder>  {
    private Context mContext;
    //  public RadioGroup radioButton;
    //   private List<order> mUploads;
    private List<myorder> mUploads;
    private RadioButton lastCheckedRB,r1,r2,r3;
    public order_adapter(Context context, List<myorder> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public order_adapter.orderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_row, parent, false);
        return new order_adapter.orderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final order_adapter.orderViewHolder holder, int position) {

        myorder uploadCurrent = mUploads.get(position);

        holder.order.setText(uploadCurrent.getOrderid());
        holder.item.setText(uploadCurrent.getItemid());
        final int itemind=uploadCurrent.itemindex;
        ///read database .check(database value)
        //  lastCheckedRB = (RadioButton) findViewById(R.id.rb1);
        holder.radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                // find the radiobutton by returned id
               // Toast.makeText(mContext,"okay",Toast.LENGTH_SHORT).show();
                if(checkedId==R.id.rb2)
                {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                    ref.child(holder.order.getText().toString()).child("orderItems").child(Integer.toString(itemind)).child("status").setValue("1");
                    ref.child(holder.order.getText().toString()).child("order_status").setValue("1");
           //         Toast.makeText(mContext,holder.order.getText().toString(),Toast.LENGTH_SHORT).show();
                    ///////////////////

                    //////////////////
r2.setEnabled(false);
                }
                else if(checkedId==R.id.rb3)
                {
                    r3.setEnabled(false);
final              DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                    ref.child(holder.order.getText().toString()).child("orderItems").child(Integer.toString(itemind)).child("status").setValue("2");
                    DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("chef");

                    FirebaseDatabase.getInstance().getReference().child("chef").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot12) {
                            for (DataSnapshot ds12 : dataSnapshot12.getChildren()) {
                                chef_list n = ds12.getValue(chef_list.class);
                              final  String chefkey=ds12.getKey();


                                for (final String key11 : n.getDishes().keySet()) {

                                    dish_list2 d = n.getDishes().get(key11);

                                    if (d.getOrder_id().equalsIgnoreCase(holder.order.getText().toString()) &&d.getDish().equalsIgnoreCase(Integer.toString(itemind)))
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("chef").child(chefkey).child("dishes").child(key11).removeValue();
                                        int que=Integer.parseInt(n.queue.toString())-1;
                                        FirebaseDatabase.getInstance().getReference().child("chef").child(chefkey).child("queue").setValue(Integer.toString(que));
                                        final String dishtime=findtime(Integer.toString(itemind));



                                        final DatabaseReference chefQueue=FirebaseDatabase.getInstance().getReference().child("chef").child(chefkey);
                                        chefQueue.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                for (DataSnapshot postSnapshot1 : dataSnapshot1.getChildren()) {

                                                    String timechef = postSnapshot1.child("time").getValue(String.class);
                                                    int caltime=Integer.parseInt(timechef)-Integer.parseInt(dishtime);
                                                   // reff.child(ordind).child("orderItems").child(Integer.toString(index)).child("required_time").setValue(Integer.toString(caltime));
                                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Orders Queue");
                                                    ref.child(holder.order.getText().toString()).child("orderItems").child(key11).child("required_time").setValue("0");
                                                    FirebaseDatabase.getInstance().getReference().child("chef").child(chefkey).child("time").setValue(Integer.toString(caltime));

                                                }
                                            }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });




//                                        Intent intent = new Intent(mContext.getApplicationContext(), chef.class);
                                        //                                     mContext.startActivity(intent);
                                    }// final  DatabaseReference An=FirebaseDatabase.getInstance().getReference().child("chef");
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    ref.child(holder.order.getText().toString()).child("orderItems").addListenerForSingleValueEvent(new ValueEventListener() {
                        boolean ans=true;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                // order n=postSnapshot.getValue(order.class);
                                item i = postSnapshot.getValue(item.class);
                                if ((i.getStatus().equalsIgnoreCase("2")) != true)
                                    ans = false;


                            }
                            if (ans == true)
                            {
                                ref.child(holder.order.getText().toString()).child("order_status").setValue("2");

                        }
                }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                    }
            }
        });




    }
    String time;
    public String findtime(final String dish1) {

       // FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().getReference().child("Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot1) {
                for (DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    menus n1=new menus();
                    n1=ds1.getValue(menus.class);
                    if(n1.getuid().equalsIgnoreCase(dish1)){

                        time= (n1.getTime());



                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
           //


            }
        });


        return time;
    }
    @Override
    public int getItemCount() {

        return mUploads.size();
    }


    public class orderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView order;
        public TextView item;
        public TextView pr;
        public RadioGroup radioButton;



        public orderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            order = itemView.findViewById(R.id.orderid);
            item=itemView.findViewById(R.id.itemid);
            radioButton = itemView.findViewById(R.id.rg1);
            lastCheckedRB= itemView.findViewById(R.id.rb1);
            r1= itemView.findViewById(R.id.rb1);
            r2= itemView.findViewById(R.id.rb2);
            r3= itemView.findViewById(R.id.rb3);
            lastCheckedRB.setEnabled(false);


        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Context context = v.getContext();
            if (pos == 1) {
                //   Intent intent = new Intent(context, Book1.class);
                // context.startActivity(intent);
            }

        }
    }
}