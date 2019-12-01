package se.aroms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chef_queue extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private chef_queue_adapter mAdapter;
    TextView tv;
    private List<dishlist3> mUploads;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_queue);
        mRecyclerView = findViewById(R.id.rcv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUploads = new ArrayList<>();


        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().getReference().child("chef").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                    chef_list n=new chef_list();
                    n= ds1.getValue(chef_list.class);
                    Intent t = getIntent();
                    String na = t.getStringExtra("name");
                    String chefid=t.getStringExtra("key");

                    if (na.equalsIgnoreCase(n.name))
                    {

                        for(String key1 : n.getDishes().keySet()) {

                            dish_list2 d = n.getDishes().get(key1);
                            String nameofdsh = findname(d.getDish(), d.getOrder_id(), chefid);
                        }

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    public String namedish;
    public String findname(final String dish1,final String orid,final String chefid) {
         FirebaseApp.initializeApp(this);
         FirebaseDatabase.getInstance().getReference().child("Menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

             public void onDataChange(DataSnapshot dataSnapshot1) {
                for (DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    menuc n1=new menuc();
                    n1=ds1.getValue(menuc.class);
                    if(n1.getuid().equalsIgnoreCase(dish1)){

                        namedish= (n1.getName());
                       dishlist3 d =new dishlist3();
                        d.setDish(namedish);
                        d.setOrderid(orid);
                        d.setChefid(chefid);
                        d.setDishid(dish1);
                        mUploads.add(d);
                        mAdapter = new chef_queue_adapter(chef_queue.this, mUploads);
                          mAdapter.notifyDataSetChanged();
                         mRecyclerView.setAdapter(mAdapter);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        return namedish;
    }

}
