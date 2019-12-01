package se.aroms;

import android.content.Context;
import android.content.Intent;
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


import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chef_edit extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private RecyclerView mRecyclerView;
    private chef_edit_adapter mAdapter;
    TextView tv;
    private List<chefs> mUploads;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_edit);
        Intent i=getIntent();
    //    Toast.makeText(getApplicationContext(),"itemname"+i.getStringExtra("itemnane"),Toast.LENGTH_SHORT).show();
      //  Toast.makeText(getApplicationContext(),"itemid"+i.getStringExtra("dishid"),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"chefis"+i.getStringExtra("chefid"),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),"orderid"+i.getStringExtra("orderid"),Toast.LENGTH_SHORT).show();
final dishlist3 storeinadapter=new dishlist3();
        storeinadapter.setDishid(i.getStringExtra("dishid"));
        storeinadapter.setDish(i.getStringExtra("itemnane"));
        storeinadapter.setChefid(i.getStringExtra("chefid"));
        storeinadapter.setOrderid(i.getStringExtra("orderid"));
        mRecyclerView = findViewById(R.id.rcv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mUploads = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().getReference().child("chef").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    chefs n1=new chefs();
                    n1.name=ds.getValue(chefs.class).getname();
                    n1.speciality=ds.getValue(chefs.class).getSpeciality();
                    n1.queue=ds.getValue(chefs.class).getQueue();
                    n1.setKey(ds.getKey());//chef id
                    mUploads.add(n1);

                    mAdapter = new chef_edit_adapter(chef_edit.this, mUploads,storeinadapter);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}

