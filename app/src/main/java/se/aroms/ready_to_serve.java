package se.aroms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import se.aroms.Modelclass.OrderModel;
import se.aroms.Modelclass.Waiter;

public class ready_to_serve extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private MKLoader progressBar;
    private RecyclerView recyclerView;
    ArrayList<OrderModel> arrayList = new ArrayList<>();


    FirebaseRecyclerAdapter<OrderModel, waiterViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_to_serve);

        // firebase Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");




        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setUpItemTouchHelper();
//        setUpAnimationDecoratorHelper();

        getAllCustomers();




    }





    private void getAllCustomers() {



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<OrderModel>()
                .setQuery(databaseReference, OrderModel.class)
                .build();

        progressBar.setVisibility(View.GONE);
        adapter = new FirebaseRecyclerAdapter<OrderModel, waiterViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final waiterViewHolder waiterViewHolder, int i, @NonNull final OrderModel orderModel) {


                String key = getRef(i).getKey();
                databaseReference.child(key);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {


                            final OrderModel orderModel1 = dataSnapshot.getValue(OrderModel.class);
                            arrayList.add(orderModel1);


//                                userName=Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
//                                userEmail=Objects.requireNonNull(dataSnapshot.child("email").getValue().toString());
//                                userAddress=Objects.requireNonNull(dataSnapshot.child("address").getValue().toString());
//                                checkedValue=Objects.requireNonNull(dataSnapshot.child("gender").getValue().toString());
//                                userRCCM=Objects.requireNonNull(dataSnapshot.child("rccm").getValue().toString());
//                                userVAT=Objects.requireNonNull(dataSnapshot.child("vat").getValue().toString());
//                                userMobile=Objects.requireNonNull(dataSnapshot.child("mobile").getValue().toString());
//                                userNote=Objects.requireNonNull(dataSnapshot.child("note").getValue().toString());
//

//                            for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
//
//                                waiterViewHolder.id.setText(dataSnapshot1.child("orderId").getValue().toString());
//
//                            }


                            if ((orderModel.getStatus()==0))
                            {
                                waiterViewHolder.status.setText("not yet in cooking");
                            }
                            if (orderModel.getStatus()==1)
                            {
                                waiterViewHolder.status.setText(" started cooking");
                            }
                            if (orderModel.getStatus()==2)
                            {
                                waiterViewHolder.status.setText("sevrved");
                            }
                            if (orderModel.getStatus()==3)
                            {
                                waiterViewHolder.status.setText("re-cooking");
                            }


//                            waiterViewHolder.status.setText(String.valueOf(orderModel.getStatus()));

                            waiterViewHolder.id.setText(orderModel.getOrderId());


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
            public waiterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desigin_serve, parent, false);
                waiterViewHolder ViewHolder = new waiterViewHolder(view);





                return ViewHolder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class waiterViewHolder extends RecyclerView.ViewHolder {
        private TextView id,status;


        public waiterViewHolder(@NonNull View itemView) {
            super(itemView);


            id = itemView.findViewById(R.id.orderID);
            status = itemView.findViewById(R.id.orderStatus);

//            profile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mClickListener.onItemClick(view, getAdapterPosition());
//                }
//            });


        }
        private waiterViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            void onItemClick(View view, int position);
        }
        public void setOnClickListener( waiterViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }



    }



}
