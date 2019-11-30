package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.aroms.R;
import se.aroms.inventory_item;
import android.view.View;
public class EditOrders extends AppCompatActivity implements adapter_for_cart.OnViewListener, adapter_for_noedit.OnViewListener {
    DatabaseReference orderDB;
    FirebaseAuth auth;
    Context context;
    DatabaseReference editDB;
    DatabaseReference menuDB;
    DatabaseReference IngredientsDB;
    DatabaseReference InventoryDB;
    List<Cart_item> EditItems;
    List<Cart_item> NoEditItems;
    List<Dishes> dishes;
    order_queue myorder;
    List<inventory_item> inventory_items;
    List<Ingredients> ingredients;
    private RecyclerView cart_rv;
    private RecyclerView.Adapter adapter_cart;
    private RecyclerView.LayoutManager layoutManager_cart;
    private RecyclerView cart_rv1;
    private RecyclerView.Adapter adapter_cart1;
    private RecyclerView.LayoutManager layoutManager_cart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_orders);
        inventory_items = new ArrayList<>();
        ingredients = new ArrayList<>();
        dishes = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        editDB = FirebaseDatabase.getInstance().getReference().child("Edited Order");
        orderDB = FirebaseDatabase.getInstance().getReference().child("Orders Queue");
        menuDB = FirebaseDatabase.getInstance().getReference().child("Menu");
        IngredientsDB = FirebaseDatabase.getInstance().getReference().child("Ingredients");
        InventoryDB = FirebaseDatabase.getInstance().getReference().child("Inventory");
        context = this;
        EditItems = new ArrayList<>();
        NoEditItems = new ArrayList<>();
        orderDB.orderByChild("uid").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    order_queue order = (postSnapshot.getValue(order_queue.class));
                    if (order.getComplimentaryDish().compareTo("NONE") == 0 && order.getOrder_status().compareTo("-1") != 0) {
                        myorder = order;
                    }
                }
                dishes.clear();
                ingredients.clear();
                for (int i = 0; i < myorder.getOrderItems().size(); i++) {
                    menuDB.child(myorder.getOrderItems().get(i).getItemID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean found = false;
                            Dishes dish = dataSnapshot.getValue(Dishes.class);
                            for (int i = 0; i < dishes.size(); i++) {
                                if (dishes.get(i).getUid().compareTo(dish.getUid()) == 0)
                                    found = true;
                            }
                            if (!found)
                                dishes.add(dish);
                            loadArray();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }

                    });
                    IngredientsDB.child(myorder.getOrderItems().get(i).getItemID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Map<String, String> obj = (Map<String, String>) dataSnapshot.getValue();
                            Iterator<Map.Entry<String, String>> itr = obj.entrySet().iterator();
                            while (itr.hasNext()) {
                                Map.Entry<String, String> entry = itr.next();
                                Ingredients ingredient = new Ingredients(entry.getKey(), entry.getValue(), dataSnapshot.getRef().getKey());
                                boolean found = false;
                                for (int i = 0; i < ingredients.size(); i++) {
                                    if (ingredients.get(i).getDishId().compareTo(ingredient.getDishId()) == 0 && ingredient.getId().compareTo(ingredients.get(i).getId()) == 0)
                                        found = true;
                                }
                                if (!found)
                                    ingredients.add(ingredient);
                            }
                            loadArray();
                            updateMaxQuantity();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                updateMaxQuantity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        cart_rv = findViewById(R.id.noedit_rv);
        cart_rv.setHasFixedSize(true);
        layoutManager_cart = new LinearLayoutManager(this);
        adapter_cart = new adapter_for_noedit(NoEditItems, this);

        cart_rv.setLayoutManager(layoutManager_cart);
        cart_rv.setAdapter(adapter_cart);

        cart_rv1 = findViewById(R.id.edit_rv);
        cart_rv1.setHasFixedSize(true);
        layoutManager_cart1 = new LinearLayoutManager(this);
        adapter_cart1 = new adapter_for_cart(EditItems, this);

        cart_rv1.setLayoutManager(layoutManager_cart1);
        cart_rv1.setAdapter(adapter_cart1);
    }

    public void loadArray() {
        EditItems.clear();
        NoEditItems.clear();
        for (int i = 0; i < myorder.getOrderItems().size(); i++) {
            order_queue_items item = myorder.getOrderItems().get(i);
            if (item.getStatus().compareTo("0") == 0) {
                int chk = checkExists(0, item.getItemID(), item.getSize());
                if (chk != -1) {
                    EditItems.get(chk).setQuantity(EditItems.get(chk).getQuantity() + 1);
                } else {
                    Dishes dish = dishes.get(0);
                    for (int j = 0; j < dishes.size(); j++) {
                        if (dishes.get(j).getUid().compareTo(item.getItemID()) == 0) {
                            dish = dishes.get(j);
                        }
                    }

                    cart_Items item1 = new cart_Items(item.getItemID(), item.getSize(), dish.getName(), dish.getDescription(), dish.getPicture(), item.getRequired_time(), "");
                    Cart_item item2 = new Cart_item(item1, 1, 0);
                    EditItems.add(item2);
                }
            }
            if (item.getStatus().compareTo("0") != 0) {
                int chk = checkExists(1, item.getItemID(), item.getSize());
                if (chk != -1) {
                    NoEditItems.get(chk).setQuantity(NoEditItems.get(chk).getQuantity() + 1);
                } else {
                    Dishes dish = dishes.get(0);
                    for (int j = 0; j < dishes.size(); j++) {
                        if (dishes.get(j).getUid().compareTo(item.getItemID()) == 0) {
                            dish = dishes.get(j);
                        }
                    }

                    cart_Items item1 = new cart_Items(item.getItemID(), item.getSize(), dish.getName(), dish.getDescription(), dish.getPicture(), item.getRequired_time(), "");
                    Cart_item item2 = new Cart_item(item1, 1, 0);
                    NoEditItems.add(item2);
                }
            }
        }
        adapter_cart.notifyDataSetChanged();
        adapter_cart1.notifyDataSetChanged();
    }

    private void updateMaxQuantity() {
        for (int i = 0; i < EditItems.size(); i++) {
            int available_quantity = 1000000;
            int available = 1;
            for (int j = 0; j < ingredients.size(); j++) {
                if (ingredients.get(j).getDishId().compareTo(EditItems.get(i).getItems().getItemID()) == 0) {

                    for (int k = 0; k < inventory_items.size(); k++) {
                        if (ingredients.get(j).getId().compareTo(inventory_items.get(k).getUid()) == 0) {
                            String current_quantity = inventory_items.get(k).getQuantity();
                            String req_quantity = ingredients.get(j).getQuantity();
                            int x = Integer.parseInt(req_quantity);
                            int y = Integer.parseInt(current_quantity);
                            int temp = y / x;
                            if (temp < available_quantity) {
                                available_quantity = temp;
                            }
                            if (x > y) {
                                available = 0;
                                break;
                            }
                        }

                    }

                }
                if (available == 0) {
                    break;
                }

            }
            EditItems.get(i).setMaxQuantity(available_quantity + EditItems.get(i).getQuantity());
        }
    }

    private int checkExists(int k, String uid, String size) {
        if (k == 0) {
            for (int i = 0; i < EditItems.size(); i++) {
                if (EditItems.get(i).getItems().getItemID().compareTo(uid) == 0 && EditItems.get(i).getItems().getSize().compareTo(size) == 0) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < NoEditItems.size(); i++) {
                if (NoEditItems.get(i).getItems().getItemID().compareTo(uid) == 0 && NoEditItems.get(i).getItems().getSize().compareTo(size) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void onViewClick(int position) {
        EditItems.remove(position);
        adapter_cart1.notifyDataSetChanged();
    }

    @Override
    public void onViewClick1(int position) {
        Toast.makeText(context, "These items are can not be deleted", Toast.LENGTH_SHORT).show();
    }

    public void onApplyChanges(View v) {
        List<order_queue_items> orderItems;
        orderItems=new ArrayList<>();
        if (EditItems.size() > 0) {
            boolean exit = false;

            orderItems = new ArrayList<>();
            for (int i = 0; i < EditItems.size(); i++) {
                EditItems.get(i).getItems().setQunatity("");
            }
            int quantity;

            for (int i = 0; i < EditItems.size(); i++) {
                if (EditItems.get(i).getLocalQuantity() != 0) {
                    quantity = EditItems.get(i).getLocalQuantity();
                } else {
                    quantity = EditItems.get(i).getQuantity();
                }
                if (quantity > EditItems.get(i).getMaxQuantity()) {
                    EditItems.get(i).getItems().setQunatity("*Maximum Quantity that can be ordered for this item is " + EditItems.get(i).getMaxQuantity());
                    adapter_cart.notifyDataSetChanged();
                    orderItems.clear();
                    exit = true;
                    break;
                }
            }

            if (!exit) {
                for (int i = 0; i < EditItems.size(); i++) {
                    if (EditItems.get(i).getLocalQuantity() != 0) {
                        quantity = EditItems.get(i).getLocalQuantity();
                    } else {
                        quantity = EditItems.get(i).getQuantity();
                    }
                    for (int j = 0; j < quantity; j++) {
                        orderItems.add(new order_queue_items(EditItems.get(i).getItems().getItemID(), EditItems.get(i).getItems().getSize(), "0", EditItems.get(i).getItems().getTime(), "0", 50.0));
                        removeInventory(EditItems.get(i).getItems().getItemID(), 1);
                    }
                }
                for (int i = 0; i < myorder.getOrderItems().size(); i++) {
                    order_queue_items item = myorder.getOrderItems().get(i);
                    if (item.getStatus().compareTo("0") == 0) {
                        addBackInventory(item.getItemID(), 1);
                    } else {
                        orderItems.add(item);
                    }
                }
                myorder.setOrderItems(orderItems);
                if(orderItems.size()==0)
                {
                    myorder.setOrder_status("-1");
                }
                orderDB.child(myorder.getOrderId()).setValue(myorder);
                String key = editDB.push().getKey();
                editDB.child(key).setValue(new editOrder(myorder.getOrderId(), "2"));
                Intent intent=new Intent(this,MyOrders.class);
                startActivity(intent);
                finish();
            }

        }
        else
        {

            for (int i = 0; i < myorder.getOrderItems().size(); i++) {
                order_queue_items item = myorder.getOrderItems().get(i);
                if (item.getStatus().compareTo("0") == 0) {
                    addBackInventory(item.getItemID(), 1);
                } else {
                    orderItems.add(item);
                }
            }
            myorder.setOrderItems(orderItems);
            if(orderItems.size()==0)
            {
                myorder.setOrder_status("-1");
            }
            orderDB.child(myorder.getOrderId()).setValue(myorder);
            String key = editDB.push().getKey();
            editDB.child(key).setValue(new editOrder(myorder.getOrderId(), "2"));
            Intent intent=new Intent(this,MyOrders.class);
            startActivity(intent);
            finish();
        }

    }
        private void addBackInventory (String dishId,int quantity){
            List<Integer> changedindex;
            changedindex = new ArrayList<>();
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i).getDishId().compareTo(dishId) == 0) {
                    for (int j = 0; j < inventory_items.size(); j++) {
                        if (inventory_items.get(j).uid.compareTo(ingredients.get(i).getId()) == 0) {
                            int current_inv = Integer.parseInt(inventory_items.get(j).getQuantity());
                            int new_quantity = current_inv + (Integer.parseInt(ingredients.get(i).getQuantity()) * quantity);
                            inventory_items.get(j).setQuantity(new_quantity + "");
                            changedindex.add(j);
                        }
                    }
                }
            }
            for (int i = 0; i < changedindex.size(); i++) {
                InventoryDB.child(inventory_items.get(changedindex.get(i)).uid).setValue(inventory_items.get(changedindex.get(i)));
            }
        }

    private void removeInventory(String dishId, int quantity) {
        List<Integer> changedindex;
        changedindex = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getDishId().compareTo(dishId) == 0) {
                for (int j = 0; j < inventory_items.size(); j++) {
                    if (inventory_items.get(j).uid.compareTo(ingredients.get(i).getId()) == 0) {
                        int current_inv = Integer.parseInt(inventory_items.get(j).getQuantity());
                        int new_quantity = current_inv - (Integer.parseInt(ingredients.get(i).getQuantity()) * quantity);
                        inventory_items.get(j).setQuantity(new_quantity + "");

                        changedindex.add(j);

                    }
                }
            }
        }
        for (int i = 0; i < changedindex.size(); i++) {
            InventoryDB.child(inventory_items.get(changedindex.get(i)).uid).setValue(inventory_items.get(changedindex.get(i)));
        }
    }
}