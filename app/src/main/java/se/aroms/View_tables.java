package se.aroms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import se.aroms.Modelclass.Table;

public class View_tables extends AppCompatActivity implements View.OnClickListener {



    private FloatingActionButton addTable;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String checkedValue;

    private RecyclerView recyclerView;
    ArrayList<Table> arrayList = new ArrayList<>();
    private MKLoader progressBar;
    FirebaseRecyclerAdapter<Table, tableViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tables);


        // firebase Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Table");






        // initialization
        addTable = findViewById(R.id.addTable);

        radioGroup= findViewById(R.id.updateRadioGroup);
        arrayList   = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setUpItemTouchHelper();
//        setUpAnimationDecoratorHelper();


        getAllCustomers();

        //set on click listener

        addTable.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {

        if (v==addTable)
        {
            submitTable();
        }


    }

    private void submitTable() {



        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addtable, null);

        Button button;
        final EditText table,capacity;
        button = view.findViewById(R.id.submitBtn);
        table  = view.findViewById(R.id.tableNumber);
        capacity = view.findViewById(R.id.capacityNumber);
        builder.setView(view);

        final android.app.AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String tableS= table.getText().toString();
                String capacityS = capacity.getText().toString();


                if (tableS.isEmpty() && capacityS.isEmpty()  )
                {
                    Toast.makeText(getApplicationContext(), "Please Enter All Details", Toast.LENGTH_SHORT).show();

                }

                else {


                    //progressDialog
                    final ProgressDialog progressDialog = ProgressDialog.show(View_tables.this, "Add Table....",
                            "Please Wait...", false, false);


                    Map addTable = new HashMap();
                    addTable.put("Table_Number", tableS);
                    addTable.put("Capacity", capacityS);
                    addTable.put("status", "Available");


                    databaseReference.child(databaseReference.push().getKey()).setValue(addTable).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(View_tables.this, View_tables.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(View_tables.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

        alertDialog.show();
    }







    private void getAllCustomers() {



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(databaseReference, Table.class)
                .build();

        progressBar.setVisibility(View.GONE);
        adapter = new FirebaseRecyclerAdapter<Table, tableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final tableViewHolder tableViewHolder, int i, @NonNull final Table table) {


                final String key = getRef(i).getKey();
                databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {



                            final Table table = dataSnapshot.getValue(Table.class);
                            arrayList.add(table);


//                                userName=Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
//                                userEmail=Objects.requireNonNull(dataSnapshot.child("email").getValue().toString());
//                                userAddress=Objects.requireNonNull(dataSnapshot.child("address").getValue().toString());
//                                checkedValue=Objects.requireNonNull(dataSnapshot.child("gender").getValue().toString());
//                                userRCCM=Objects.requireNonNull(dataSnapshot.child("rccm").getValue().toString());
//                                userVAT=Objects.requireNonNull(dataSnapshot.child("vat").getValue().toString());
//                                userMobile=Objects.requireNonNull(dataSnapshot.child("mobile").getValue().toString());
//                                userNote=Objects.requireNonNull(dataSnapshot.child("note").getValue().toString());
//


                            tableViewHolder.capacity.setText(table.getCapacity());
                            tableViewHolder.tableNumber.setText(table.getTable_Number());
                            if (table.getStatus().equals("Available"))
                            {
                                tableViewHolder.availableRadio.setChecked(true);

                            }
                            if (table.getStatus().equals("Not Available"))
                            {
                                tableViewHolder.notAvailableRadio.setChecked(true);
                            }








                            final String notavailableCheck= tableViewHolder.notAvailableRadio.getText().toString();
                            tableViewHolder.notAvailableRadio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Map addTable = new HashMap();
                                    addTable.put("status", notavailableCheck);
                                    databaseReference.child(key).updateChildren(addTable);


                                }
                            });





                            final String availableCheck= tableViewHolder.availableRadio.getText().toString();
                            tableViewHolder.availableRadio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Map addTable = new HashMap();
                                    addTable.put("status", availableCheck);
                                    databaseReference.child(key).updateChildren(addTable);

                                }
                            });



//                            tableViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Toast.makeText(View_tables.this, "Click", Toast.LENGTH_SHORT).show();
//
//                                    Intent intent= new Intent(View_tables.this,CustomerSupplierDetailActivity.class);
//                                    intent.putExtra("capacity",table.getCapacity());
//                                    intent.putExtra("Number",table.getTable_Number());
//
//
//
//                                }
//                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public tableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desigin_addtable, parent, false);
                tableViewHolder ViewHolder = new tableViewHolder(view);





                return ViewHolder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }




    public static class tableViewHolder extends RecyclerView.ViewHolder {
        public BreakIterator name;
        private TextView capacity,tableNumber,status;
        private  RadioButton  availableRadio,notAvailableRadio;

        public tableViewHolder(@NonNull View itemView) {
            super(itemView);


            capacity = itemView.findViewById(R.id.capacityNumber);
            tableNumber = itemView.findViewById(R.id.tableNumber);
            availableRadio = itemView.findViewById(R.id.radio2);
            notAvailableRadio= itemView.findViewById(R.id.radio3);








        }
        private tableViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            void onItemClick(View view, int position);


        }
        public void setOnClickListener( tableViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }





    }




    //RadioGroup
    public void radioGroup(View v){



        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        radioButton.setTextColor(Color.BLACK);
        checkedValue = radioButton.getText().toString();
        Toast.makeText(this, ""+checkedValue, Toast.LENGTH_SHORT).show();


    }


}
