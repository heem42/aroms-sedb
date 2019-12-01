package se.aroms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;

import se.aroms.Modelclass.Waiter;

public class waiters extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton addWaiter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;



    private RecyclerView recyclerView;
    ArrayList<Waiter> arrayList = new ArrayList<>();
    private MKLoader progressBar;


    FirebaseRecyclerAdapter<Waiter, customerViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiters);


        // firebase Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Waiters");





        // initialization
        addWaiter = findViewById(R.id.addWaiter);
        progressBar = findViewById(R.id.progressBar);


        //set on click listener


        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setUpItemTouchHelper();
//        setUpAnimationDecoratorHelper();

        getAllCustomers();

        addWaiter.setOnClickListener(this);







    }


    @Override
    public void onClick(View v) {

        if (v == addWaiter) {
            submitTable();
        }


    }

    private void submitTable() {


        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addwaiter, null);

        Button button;
        final EditText assign, name;


        button  = view.findViewById(R.id.submitBtn);
        assign      = view.findViewById(R.id.waiterID);
        name    = view.findViewById(R.id.waiterName);

        builder.setView(view);

        final android.app.AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameS = name.getText().toString();
                String assignS = assign.getText().toString();

//                waiterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        waiterSpinner.setSelection(position);
//
//                        spinnerValue[0] =  waiterSpinner.getSelectedItem().toString();
//
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//
//





                if (nameS.isEmpty() && assignS.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Please Enter the All detail", Toast.LENGTH_SHORT).show();

                } else {

                    final DatabaseReference key = databaseReference.push();

                    //progressDialog
                    final ProgressDialog progressDialog = ProgressDialog.show(waiters.this, "Add Waiter....",
                            "Please Wait...", false, false);


                    Map addWaiter = new HashMap();
                    addWaiter.put("Waiter_Name", nameS);
                    addWaiter.put("Assign_table", assignS);


                    databaseReference.child(key.getKey()).setValue(addWaiter).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(waiters.this, waiters.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(waiters.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

        alertDialog.show();
    }

    private void getAllCustomers() {



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Waiter>()
                .setQuery(databaseReference, Waiter.class)
                .build();

        progressBar.setVisibility(View.GONE);
        adapter = new FirebaseRecyclerAdapter<Waiter, customerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final customerViewHolder customerViewHolder, int i, @NonNull final Waiter customerModel) {


                String key = getRef(i).getKey();
                databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {



                            final Waiter customerModel1 = dataSnapshot.getValue(Waiter.class);
                            arrayList.add(customerModel1);


//                                userName=Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
//                                userEmail=Objects.requireNonNull(dataSnapshot.child("email").getValue().toString());
//                                userAddress=Objects.requireNonNull(dataSnapshot.child("address").getValue().toString());
//                                checkedValue=Objects.requireNonNull(dataSnapshot.child("gender").getValue().toString());
//                                userRCCM=Objects.requireNonNull(dataSnapshot.child("rccm").getValue().toString());
//                                userVAT=Objects.requireNonNull(dataSnapshot.child("vat").getValue().toString());
//                                userMobile=Objects.requireNonNull(dataSnapshot.child("mobile").getValue().toString());
//                                userNote=Objects.requireNonNull(dataSnapshot.child("note").getValue().toString());
//



                            customerViewHolder.name.setText(customerModel1.getWaiter_Name());
                            customerViewHolder.assign.setText(customerModel.getAssign_table());


//                            customerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    Intent intent= new Intent(CustomerActivity.this,CustomerSupplierDetailActivity.class);
//                                    intent.putExtra("name",customerModel.getName());
//                                    intent.putExtra("email",customerModel.getEmail());
//                                    intent.putExtra("number",customerModel.getMobile());
//                                    intent.putExtra("gender",customerModel.getGender());
//                                    intent.putExtra("address",customerModel.getAddress());
//                                    intent.putExtra("note",customerModel.getNote());
//                                    intent.putExtra("rccm",customerModel.getRccm());
//                                    intent.putExtra("vat",customerModel.getVat());
//                                    intent.putExtra("image",customerModel.getImage());
//                                    startActivity(intent);
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
            public customerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desigin_waiter, parent, false);
                customerViewHolder ViewHolder = new customerViewHolder(view);





                return ViewHolder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class customerViewHolder extends RecyclerView.ViewHolder {
        private TextView name,assign;


        public customerViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.waiterName);
            assign = itemView.findViewById(R.id.assignWaiter);

//            profile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mClickListener.onItemClick(view, getAdapterPosition());
//                }
//            });


        }
        private customerViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            void onItemClick(View view, int position);
        }
        public void setOnClickListener( customerViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }



    }



















}