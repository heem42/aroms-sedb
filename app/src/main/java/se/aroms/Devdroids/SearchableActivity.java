package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.aroms.R;
import se.aroms.inventory_item;

public class SearchableActivity extends AppCompatActivity implements adapter_for_dishes.OnViewListener {
    List<inventory_item> inventory_items;
    List<Dishes> Items;
    List<Dishes> Itemsfiltered;
    List<Ingredients> ingredients;
    FirebaseAuth auth;
    Context context;
    DatabaseReference cartDB;
    TextView orderNo;
    List<Cart_item> CartItems;
    DatabaseReference menuDB;
    String query;
    private RecyclerView dishes_rv;
    private RecyclerView.Adapter adapter_dishes;
    private  RecyclerView.LayoutManager layoutManager_dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Items=new ArrayList<>();
        CartItems=new ArrayList<>();
        orderNo=findViewById(R.id.search_cart);
        context=this;
        menuDB= FirebaseDatabase.getInstance().getReference();
        cartDB = FirebaseDatabase.getInstance().getReference().child("Cart");
        auth=FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Itemsfiltered=new ArrayList<>();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Menu");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Items.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Items.add(postSnapshot.getValue(Dishes.class));

                    }
                    filterItems();
                    adapter_dishes.notifyDataSetChanged();
                    ingredients=new ArrayList<>();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Ingredients");
                    ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ingredients.clear();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Map<String,String> obj=(Map<String,String>)postSnapshot.getValue();
                                Iterator<Map.Entry<String, String>> itr=obj.entrySet().iterator();
                                while(itr.hasNext())
                                {
                                    Map.Entry<String, String> entry = itr.next();
                                    ingredients.add(new Ingredients(entry.getKey(),entry.getValue(),postSnapshot.getRef().getKey()));
                                }
                                adapter_dishes.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    inventory_items=new ArrayList<>();
                    DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("Inventory");
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            inventory_items.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                inventory_items.add(postSnapshot.getValue(inventory_item.class));

                            }
                            for(int i=0;i<Items.size();i++)
                            {
                                int available_quantity=1000000;
                                int available=1;
                                for(int j=0;j<ingredients.size();j++)
                                {
                                    if(ingredients.get(j).getDishId().compareTo(Items.get(i).getUid())==0)
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
                                Items.get(i).setMaxQuantity(available_quantity);
                                Items.get(i).setAvailability(available);
                            }
                            adapter_dishes.notifyDataSetChanged();
                            //Toast.makeText(context,"I was changed",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                        orderNo.setText(" " + CartItems.size());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dishes_rv = findViewById(R.id.search_activity_recyclerView);
            dishes_rv.setHasFixedSize(true);
            layoutManager_dishes = new LinearLayoutManager(this);
            adapter_dishes = new adapter_for_dishes(Itemsfiltered,this);

            dishes_rv.setLayoutManager(layoutManager_dishes);
            dishes_rv.setAdapter(adapter_dishes);
        }
        else
        {
            finish();
        }
    }
    public void filterItems(){
        Itemsfiltered.clear();
        for(int i=0;i<Items.size();i++){
            if(Items.get(i).getDescription().toLowerCase().contains(query.toLowerCase()) || Items.get(i).getName().toLowerCase().contains(query.toLowerCase()) || Items.get(i).getType().toLowerCase().contains(query.toLowerCase()))
            {
                Itemsfiltered.add(Items.get(i));
            }
        }
        adapter_dishes.notifyDataSetChanged();
    }

    @Override
    public void onViewClick(int position) {
        Intent intent=new Intent(SearchableActivity.this, Dish_Details.class);
        intent.putExtra("item",Items.get(position));
        startActivity(intent);
    }
}
