package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.aroms.R;
import se.aroms.inventory_item;

import android.view.View;
public class Cart extends AppCompatActivity implements adapter_for_cart.OnViewListener {
    FirebaseAuth auth;
    Context context;
    DatabaseReference cartDB;
    DatabaseReference orderDB;
    DatabaseReference editDB;
    DatabaseReference menuDB;
    DatabaseReference IngredientsDB;
    DatabaseReference InventoryDB;
    List<Cart_item> CartItems;
    order_queue myorder;
    TextView empty;
    List<Dishes> dishes;
    List<inventory_item> inventory_items;
    List<Ingredients> ingredients;
    private RecyclerView cart_rv;
    private RecyclerView.Adapter adapter_cart;
    private  RecyclerView.LayoutManager layoutManager_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        CartItems=new ArrayList<>();
        inventory_items=new ArrayList<>();
        ingredients=new ArrayList<>();
        dishes=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        editDB = FirebaseDatabase.getInstance().getReference().child("Edited Order");
    empty=findViewById(R.id.empty_cart);
        cartDB = FirebaseDatabase.getInstance().getReference().child("Cart");
        orderDB = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
        menuDB = FirebaseDatabase.getInstance().getReference().child("Menu");
        IngredientsDB=FirebaseDatabase.getInstance().getReference().child("Ingredients");
        InventoryDB=FirebaseDatabase.getInstance().getReference().child("Inventory");
        context=this;
        orderDB.orderByChild("uid").equalTo(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    order_queue order = (postSnapshot.getValue(order_queue.class));
                    if (order.getComplimentaryDish().compareTo("NONE") == 0 && order.getOrder_status().compareTo("-1") != 0) {
                        myorder = order;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cartDB.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    CartItems.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CartItems.add(postSnapshot.getValue(Cart_item.class));

                    }
                    if(CartItems.size()>0)
                    {
                        empty.setVisibility(View.INVISIBLE);
                    }
                    adapter_cart.notifyDataSetChanged();
                    dishes.clear();
                    ingredients.clear();
                    for(int i=0;i<CartItems.size();i++) {
                        menuDB.child(CartItems.get(i).getItems().getItemID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean found=false;
                                Dishes dish=dataSnapshot.getValue(Dishes.class);
                                for(int i=0;i<dishes.size();i++)
                                {
                                    if(dishes.get(i).getUid().compareTo(dish.getUid())==0)
                                        found=true;
                                }
                                if(!found)
                                    dishes.add(dish);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                        });
                        IngredientsDB.child(CartItems.get(i).getItems().getItemID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Map<String, String> obj = (Map<String, String>) dataSnapshot.getValue();
                                Iterator<Map.Entry<String, String>> itr = obj.entrySet().iterator();
                                while (itr.hasNext()) {
                                    Map.Entry<String, String> entry = itr.next();
                                    Ingredients ingredient=new Ingredients(entry.getKey(), entry.getValue(), dataSnapshot.getRef().getKey());
                                    boolean found=false;
                                    for(int i=0;i<ingredients.size();i++)
                                    {
                                        if(ingredients.get(i).getDishId().compareTo(ingredient.getDishId())==0 && ingredient.getId().compareTo(ingredients.get(i).getId())==0)
                                            found=true;
                                    }
                                    if(!found)
                                        ingredients.add(ingredient);
                                }
                                updateMaxQuantity();
                                loadInventory();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        InventoryDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inventory_items.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    inventory_item iv = (postSnapshot.getValue(inventory_item.class));
                    inventory_items.add(iv);


                }
                loadInventory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        cart_rv= findViewById(R.id.cart_recylcerview);
        cart_rv.setHasFixedSize(true);
        layoutManager_cart = new LinearLayoutManager(this);
        adapter_cart= new adapter_for_cart(CartItems,this);

        cart_rv.setLayoutManager(layoutManager_cart);
        cart_rv.setAdapter(adapter_cart);
    }

    @Override
    public void onViewClick(int position) {
        addBackInventory(CartItems.get(position).getItems().getItemID(),CartItems.get(position).getQuantity());
        cartDB.child(auth.getUid()).child(CartItems.get(position).getItems().getItemID()+CartItems.get(position).getItems().getSize()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }
    public void OnOrderClick (View view)
    {
        if(CartItems.size()>0) {
            boolean exit = false;
            final List<order_queue_items> orderItems;
            orderItems = new ArrayList<>();
            for (int i = 0; i < CartItems.size(); i++) {
                CartItems.get(i).getItems().setQunatity("");
            }
            int quantity;

            for (int i = 0; i < CartItems.size(); i++) {
                if(CartItems.get(i).getLocalQuantity()!=0)
                {
                    quantity=CartItems.get(i).getLocalQuantity();
                }
                else
                {
                    quantity=CartItems.get(i).getQuantity();
                }
                if (quantity > CartItems.get(i).getMaxQuantity()) {
                    CartItems.get(i).getItems().setQunatity("*Maximum Quantity that can be ordered for this item is " + CartItems.get(i).getMaxQuantity());
                    adapter_cart.notifyDataSetChanged();
                    orderItems.clear();
                    exit = true;
                    break;
                }

                for (int j = 0; j < quantity; j++)
                    orderItems.add(new order_queue_items(CartItems.get(i).getItems().getItemID(), CartItems.get(i).getItems().getSize(), "0", CartItems.get(i).getItems().getTime(), "0", 50.0));
            }
            if (exit == false) {
                for (int i = 0; i < CartItems.size(); i++) {
                    if (CartItems.get(i).getLocalQuantity() < CartItems.get(i).getQuantity() && CartItems.get(i).getLocalQuantity()!=0) {
                        //user has decreased quantity add back inventory
                        addBackInventory(CartItems.get(i).getItems().getItemID(), CartItems.get(i).getQuantity() - CartItems.get(i).getLocalQuantity());
                    } else if (CartItems.get(i).getLocalQuantity() > CartItems.get(i).getQuantity() && CartItems.get(i).getLocalQuantity()!=0) {
                        //user has increased quantity
                        removeInventory(CartItems.get(i).getItems().getItemID(), CartItems.get(i).getLocalQuantity() - CartItems.get(i).getQuantity());
                    }
                }
                if (myorder == null) {
                    order_queue orderQueue = new order_queue(new Date(), orderItems, "hell", auth.getUid(), "0", "0", "NONE");
                    String key = orderDB.push().getKey();
                    orderQueue.setOrderId(key);
                    orderDB.child(key).setValue(orderQueue).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Orders Placed", Toast.LENGTH_SHORT).show();
                            cartDB.child(auth.getUid()).removeValue();
                            Intent intent = new Intent(context, MyOrders.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                } else {
                    for (int i = 0; i < orderItems.size(); i++) {
                        myorder.getOrderItems().add(orderItems.get(i));
                    }
                    cartDB.child(auth.getUid()).removeValue();
                    orderDB.child(myorder.getOrderId()).setValue(myorder);
                    String key = editDB.push().getKey();
                    editDB.child(key).setValue(new editOrder(myorder.getOrderId(), "1"));
                    Intent intent = new Intent(context, MyOrders.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else
        {
            Toast.makeText(context, "Please add items in cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void addBackInventory(String dishId,int quantity){
        List<Integer> changedindex;
        changedindex=new ArrayList<>();
        for(int i=0;i<ingredients.size();i++)
        {
            if(ingredients.get(i).getDishId().compareTo(dishId)==0){
                for(int j=0;j<inventory_items.size();j++){
                    if(inventory_items.get(j).uid.compareTo(ingredients.get(i).getId())==0){
                        int current_inv=Integer.parseInt(inventory_items.get(j).getQuantity());
                        int new_quantity=current_inv+ (Integer.parseInt(ingredients.get(i).getQuantity())*quantity);
                        inventory_items.get(j).setQuantity(new_quantity+"");
                        changedindex.add(j);
                    }
                }
            }
        }
        for(int i=0;i<changedindex.size();i++){
            InventoryDB.child(inventory_items.get(changedindex.get(i)).uid).setValue(inventory_items.get(changedindex.get(i)));
        }
    }
    private void removeInventory(String dishId,int quantity){
        List<Integer> changedindex;
        changedindex=new ArrayList<>();
        for(int i=0;i<ingredients.size();i++)
        {
            if(ingredients.get(i).getDishId().compareTo(dishId)==0){
                for(int j=0;j<inventory_items.size();j++){
                    if(inventory_items.get(j).uid.compareTo(ingredients.get(i).getId())==0){
                        int current_inv=Integer.parseInt(inventory_items.get(j).getQuantity());
                        int new_quantity=current_inv- (Integer.parseInt(ingredients.get(i).getQuantity())*quantity);
                        inventory_items.get(j).setQuantity(new_quantity+"");

                        changedindex.add(j);

                    }
                }
            }
        }
        for(int i=0;i<changedindex.size();i++){
            InventoryDB.child(inventory_items.get(changedindex.get(i)).uid).setValue(inventory_items.get(changedindex.get(i)));
        }
    }
    private void loadInventory(){

    }
    private void updateMaxQuantity(){
        for(int i=0;i<CartItems.size();i++)
        {
            int available_quantity=1000000;
            int available=1;
            for(int j=0;j<ingredients.size();j++)
            {
                if(ingredients.get(j).getDishId().compareTo(CartItems.get(i).getItems().getItemID())==0)
                {

                    for(int k=0;k<inventory_items.size();k++)
                    {
                        if(ingredients.get(j).getId().compareTo(inventory_items.get(k).getUid())==0)
                        {
                            String current_quantity=inventory_items.get(k).getQuantity();
                            String req_quantity=ingredients.get(j).getQuantity();
                            int x=Integer.parseInt(req_quantity);
                            int y=Integer.parseInt(current_quantity);
                            int temp=y/x;
                            if(temp<available_quantity){
                                available_quantity=temp;
                            }
                            if(x>y)
                            {
                                available=0;
                                break;
                            }
                        }

                    }

                }
                if(available==0){
                    break;
                }

            }
            CartItems.get(i).setMaxQuantity(available_quantity+CartItems.get(i).getQuantity());
        }
    }
    public void onCartBack(View v){
        onBackPressed();
    }

}
