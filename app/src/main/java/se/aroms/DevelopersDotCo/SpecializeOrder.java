package se.aroms.DevelopersDotCo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import se.aroms.Devdroids.Dishes;
import se.aroms.Devdroids.order_queue;
import se.aroms.R;
import se.aroms.inventory_item;


public class SpecializeOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomRecyclerViewDishes adapter;
    private DatabaseReference DB;
    private ArrayList<Dishes> dishes;
    private ArrayList<inventory_item> inventory;
    private int orderNo;
    private order_queue order;
    private Switch priority;
    private RadioGroup complimentaryDishes;
    private Button confirmBtn;
    private TextView complimentaryDish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialize_order);
        inventory = new ArrayList<>();
        DB = FirebaseDatabase.getInstance().getReference().child("Inventory");
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inventory.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    inventory.add(postSnapshot.getValue(inventory_item.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        order = (order_queue) getIntent().getSerializableExtra("order");
        orderNo = getIntent().getIntExtra("index",0);
        priority = (Switch) findViewById(R.id.priority);
        complimentaryDishes = (RadioGroup) findViewById(R.id.radioGroup);
        complimentaryDish = (TextView) findViewById(R.id.complimentaryDishOnOrder);
        if(order.getComplimentaryDish() != null){
            if (!(order.getComplimentaryDish().equals("") || order.getComplimentaryDish().equals("None"))){
                complimentaryDish.setText(order.getComplimentaryDish());
            }
        }
        confirmBtn = (Button) findViewById(R.id.Confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priority.isChecked()){
                    order.setPriority("1");
                }
                else{
                    order.setPriority("2");
                }
                order.setOrder_status("3");
                if (complimentaryDishes.getCheckedRadioButtonId() == -1)
                {
                    order.setComplimentaryDish("None");
                }
                else
                {
                    int selectedBtnId = complimentaryDishes.getCheckedRadioButtonId();
                    RadioButton selectedBtn = (RadioButton) findViewById(selectedBtnId);
                    if(checkInventoryForComplimentaryDish( (String) selectedBtn.getText())){
                        order.setComplimentaryDish((String) selectedBtn.getText());
                    }

                    else{
                        Toast.makeText(SpecializeOrder.this, "Item out of Stock",
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(SpecializeOrder.this, "Only Priority & Status Changed",
                                Toast.LENGTH_LONG).show();
                    }

                }
                for (int i =0; i < adapter.ChangedStatusIndexes.size(); i++){
                    order.getOrderItems().get(adapter.ChangedStatusIndexes.get(i)).setStatus("3");
                }
                DB= FirebaseDatabase.getInstance().getReference().child("Orders Queue").child(order.getOrderId());
                DB.setValue(order);
                finish();

            }
        });
        TextView ordernumber = (TextView) findViewById(R.id.orderNumber);
        ordernumber.setText("Order Number " + (orderNo + 1));
        dishes = new ArrayList<>();
        dishes.clear();
        adapter = new CustomRecyclerViewDishes(dishes,order, R.layout.specialize_order_dish_row,this);
        recyclerView = (RecyclerView) findViewById(R.id.DishesRV);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DB= FirebaseDatabase.getInstance().getReference().child("Menu");
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (int i=0;i < order.getOrderItems().size();i++){
                        if(postSnapshot.getKey().equals(order.getOrderItems().get(i).getItemID())){
                            dishes.add(postSnapshot.getValue(Dishes.class));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean checkInventoryForComplimentaryDish(String dish){
        for (int i = 0; i < inventory.size();i++){
            if(dish.equals("Fries") && inventory.get(i).getName().equals("Potatoes") && Integer.parseInt(inventory.get(i).getQuantity()) > 0){
                DB = FirebaseDatabase.getInstance().getReference().child("Inventory").child(inventory.get(i).getUid()).child("quantity");
                DB.setValue(String.valueOf(Integer.parseInt(inventory.get(i).getQuantity())-1));
                return true;
            }
            else if(dish.equals("Cold Drink") && inventory.get(i).getName().equals("Cold Drinks") && Integer.parseInt(inventory.get(i).getQuantity()) > 0){
                DB = FirebaseDatabase.getInstance().getReference().child("Inventory").child(inventory.get(i).getUid()).child("quantity");
                DB.setValue(String.valueOf(Integer.parseInt(inventory.get(i).getQuantity())-1));
                return true;
            }
            else if(dish.equals("Onion Rings") && inventory.get(i).getName().equals("Onions") && Integer.parseInt(inventory.get(i).getQuantity()) > 0){
                DB = FirebaseDatabase.getInstance().getReference().child("Inventory").child(inventory.get(i).getUid()).child("quantity");
                DB.setValue(String.valueOf(Integer.parseInt(inventory.get(i).getQuantity())-1));
                return true;
            }
            else if(dish.equals("Cheese Sticks") && inventory.get(i).getName().equals("Cheese") && Integer.parseInt(inventory.get(i).getQuantity()) > 0){
                DB = FirebaseDatabase.getInstance().getReference().child("Inventory").child(inventory.get(i).getUid()).child("quantity");
                DB.setValue(String.valueOf(Integer.parseInt(inventory.get(i).getQuantity())-1));
                return true;
            }
            else if(dish.equals("Mint Margarita") && inventory.get(i).getName().equals("Mint") && Integer.parseInt(inventory.get(i).getQuantity()) > 0){
                DB = FirebaseDatabase.getInstance().getReference().child("Inventory").child(inventory.get(i).getUid()).child("quantity");
                DB.setValue(String.valueOf(Integer.parseInt(inventory.get(i).getQuantity())-1));
                return true;
            }
        }
        return false;
    }
}
