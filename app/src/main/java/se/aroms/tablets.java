package se.aroms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.aroms.Modelclass.Tablets;
import se.aroms.Modelclass.Waiter;

public class tablets extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton addTablets;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    ArrayList<Tablets> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;

    private MKLoader progressBar;
    FirebaseRecyclerAdapter<Tablets, orderViewHolder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablets);
        //Spinner dropdown=findViewById(R.id);
        List<String> list =new ArrayList<>();
        list.add("Table No");
        list.add("1");
        list.add("2");
        list.add("3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // dropdown.setAdapter(dataAdapter);





        // firebase Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tablets");


        // initialization
        addTablets = findViewById(R.id.addtablets);




        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setUpItemTouchHelper();
//        setUpAnimationDecoratorHelper();
        //set on click listener
        getAllCustomers();




        addTablets.setOnClickListener(this);



    }




    @Override
    public void onClick(View v) {

        if (v==addTablets)
        {
            submitTable();
        }


    }

    private void submitTable() {



        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addtablets, null);

        Button button;
        final EditText  assignTable,name;
        button = view.findViewById(R.id.submitBtn);
        assignTable  = view.findViewById(R.id.assignTable);
        name = view.findViewById(R.id.tabletName);
        builder.setView(view);

        final android.app.AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameS = name.getText().toString();
                String assignS = assignTable.getText().toString();


                if ( nameS.isEmpty() && assignS.isEmpty()  )
                {
                    Toast.makeText(getApplicationContext(), "Please Enter all details", Toast.LENGTH_SHORT).show();

                }

                else {

                    final DatabaseReference key = databaseReference.push();

                    //progressDialog
                    final ProgressDialog progressDialog = ProgressDialog.show(tablets.this, "Add Tablets....",
                            "Please Wait...", false, false);


                    Map addTablets = new HashMap();
                    addTablets.put("tablet_Name", nameS);
                    addTablets.put("Assign_tablet",assignS);

                    databaseReference.child(key.getKey()).setValue(addTablets).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(tablets.this, tablets.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(tablets.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

        alertDialog.show();
    }


    private void getAllCustomers() {



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Tablets>()
                .setQuery(databaseReference, Tablets.class)
                .build();

        progressBar.setVisibility(View.GONE);
        adapter = new FirebaseRecyclerAdapter<Tablets,orderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final  orderViewHolder orderViewHolder, int i, @NonNull final Tablets order) {


                String key = getRef(i).getKey();
                databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {


                            final Tablets order = dataSnapshot.getValue(Tablets.class);
                            arrayList.add(order);


//                                userName=Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
//                                userEmail=Objects.requireNonNull(dataSnapshot.child("email").getValue().toString());
//                                userAddress=Objects.requireNonNull(dataSnapshot.child("address").getValue().toString());
//                                checkedValue=Objects.requireNonNull(dataSnapshot.child("gender").getValue().toString());
//                                userRCCM=Objects.requireNonNull(dataSnapshot.child("rccm").getValue().toString());
//                                userVAT=Objects.requireNonNull(dataSnapshot.child("vat").getValue().toString());
//                                userMobile=Objects.requireNonNull(dataSnapshot.child("mobile").getValue().toString());
//                                userNote=Objects.requireNonNull(dataSnapshot.child("note").getValue().toString());
//

                            orderViewHolder.name.setText(order.getTablet_Name());
                            orderViewHolder.assignTable.setText(order.getAssign_tablet());


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desigin_tablet, parent, false);
                orderViewHolder ViewHolder = new orderViewHolder(view);





                return ViewHolder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class orderViewHolder extends RecyclerView.ViewHolder {
        private TextView  name, assignTable;


        public orderViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.tabletName);
            assignTable =itemView.findViewById(R.id.Assign_tabletName);


//            profile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mClickListener.onItemClick(view, getAdapterPosition());
//                }
//            });


        }
        private orderViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            void onItemClick(View view, int position);
        }
        public void setOnClickListener( orderViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }



    }

}
