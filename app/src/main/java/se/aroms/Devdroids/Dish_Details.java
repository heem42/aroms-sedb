package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.aroms.R;
import se.aroms.inventory_item;

public class Dish_Details extends AppCompatActivity {
    TextView desp;
    TextView name;
    TextView regPrice;
    TextView largePrice;
    TextView cookTime;
    ImageView dish_image;
    ProgressBar progressBar;
    Context context;
    TextView orderNo;
    Dishes dishes;
    RadioGroup radioGroup;
    Button addToCat;
    EditText quantity;
    DatabaseReference cartDB;
    DatabaseReference IngredientsDB;
    DatabaseReference InventoryDB;
    FirebaseAuth auth;
    List<Cart_item> CartItems;
    List<inventory_item> inventory_items;
    List<Ingredients> ingredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish__details);
        Intent intent=getIntent();
        cartDB = FirebaseDatabase.getInstance().getReference().child("Cart");
        IngredientsDB=FirebaseDatabase.getInstance().getReference().child("Ingredients");
        InventoryDB=FirebaseDatabase.getInstance().getReference().child("Inventory");
        auth=FirebaseAuth.getInstance();
        CartItems=new ArrayList<>();
        context=this;
        dishes=intent.getParcelableExtra("item");
        desp=findViewById(R.id.dish_detail_desc);
        regPrice=findViewById(R.id.dish_detail_regprice);
        largePrice=findViewById(R.id.dish_detail_largeprice);
        progressBar=findViewById(R.id.dish_detail_progress);
        quantity=findViewById(R.id.dish_detail_quantity);
        cookTime=findViewById(R.id.dish_detail_cooktime);
        dish_image=findViewById(R.id.dish_detail_img);
        addToCat=findViewById(R.id.addToCart);
        orderNo=findViewById(R.id.cartNo1);
        radioGroup=findViewById(R.id.sizeRadio);
        desp.setText(dishes.getDescription());
        regPrice.setText("Regular: "+dishes.getReg_price()+"RS");
        largePrice.setText("Large: "+dishes.getLarge_price()+"RS");
        cookTime.setText(dishes.getTime()+" Minutes");
        name=findViewById(R.id.dish_detail_name);
        name.setText(dishes.getName());
        ingredients=new ArrayList<>();
        inventory_items=new ArrayList<>();
        if(dishes.getPicture()!=null) {

            Picasso.get().load(dishes.getPicture()).fit().into(dish_image, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.GONE);
                    dish_image.setImageResource(R.drawable.notavailable);
                }
            });

        }
        else
        {
            progressBar.setVisibility(View.GONE);
            dish_image.setImageResource(R.drawable.notavailable);
        }
        if(dishes.getAvailability()==0)
        {
            addToCat.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        cartDB.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    CartItems.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        CartItems.add(postSnapshot.getValue(Cart_item.class));

                    }
                    orderNo.setText(" "+CartItems.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        IngredientsDB.child(dishes.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> obj=(Map<String,String>)dataSnapshot.getValue();
                Iterator<Map.Entry<String, String>> itr=obj.entrySet().iterator();
                while(itr.hasNext())
                {
                    Map.Entry<String, String> entry = itr.next();
                    ingredients.add(new Ingredients(entry.getKey(),entry.getValue(),dataSnapshot.getRef().getKey()));
                }
                    InventoryDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            inventory_items.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                inventory_item iv=(postSnapshot.getValue(inventory_item.class));
                                for(int i=0;i<ingredients.size();i++){
                                    if(iv.uid.compareTo(ingredients.get(i).getId())==0){
                                        inventory_items.add(iv);
                                    }
                                }

                            }
                            int available_quantity=1000000;
                            int available=1;
                            for(int j=0;j<ingredients.size();j++)
                            {
                                if(ingredients.get(j).getDishId().compareTo(dishes.getUid())==0)
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
                            dishes.setMaxQuantity(available_quantity);
                            dishes.setAvailability(available);
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

    }
    public void onClickCart(View v) {
        if(dishes.getAvailability()==0)
        {
            Toast.makeText(this, "Sorry item currently out of stock", Toast.LENGTH_SHORT).show();
            return;
        }
        String size = "Regular";
        boolean found = false;
        int selected = radioGroup.getCheckedRadioButtonId();
        if (selected == R.id.Largesize) {
            size = "Large";
        }
        for (int i = 0; i < CartItems.size(); i++) {
            if (CartItems.get(i).getItems().getItemID().compareTo(dishes.getUid()) == 0 && CartItems.get(i).getItems().getSize().compareTo(size) == 0) {
                found = true;
            }
        }
        if (!found) {
            int quan = Integer.parseInt(quantity.getText().toString());
            if (quan > dishes.getMaxQuantity()) {
                Toast.makeText(this, "Maximum quantity that can be ordered is " + dishes.getMaxQuantity(), Toast.LENGTH_SHORT).show();
                quantity.setText("");
                quantity.setText(quan + "");
            } else if (quan <= 0) {
                Toast.makeText(this, "Quantity should be greater than 0", Toast.LENGTH_SHORT).show();
                quantity.setText("");
            } else {
                Cart_item item = new Cart_item(new cart_Items(dishes.getUid(), size, dishes.getName(), dishes.getDescription(), dishes.getPicture(), dishes.getTime(), ""), quan, dishes.getMaxQuantity());
                cartDB.child(auth.getUid()).child(dishes.getUid() + size).setValue(item);
                updateInventory(quan);

            }
        }
        else
        {
            Toast.makeText(this, "Item Already in Cart", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateInventory(int quan){
        List<Integer> changedindex;
        changedindex=new ArrayList<>();
        for(int i=0;i<ingredients.size();i++)
        {
            for(int j=0;j<inventory_items.size();j++)
            {
                if(ingredients.get(i).getId().compareTo(inventory_items.get(j).getUid())==0)
                {
                    int newQuantity=Integer.parseInt(inventory_items.get(j).getQuantity())-(Integer.parseInt(ingredients.get(i).getQuantity())*quan);

                    inventory_items.get(j).setQuantity(String.valueOf(newQuantity));
                    changedindex.add(j);
                }
            }
        }
        for(int i=0;i<changedindex.size();i++){
            InventoryDB.child(inventory_items.get(changedindex.get(i)).uid).setValue(inventory_items.get(changedindex.get(i))).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

        }
        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
    }
    public void updateCartQuanitiy()
    {

    }
    public void onMenu_DetailCartClick(View v){
        Intent intent=new Intent(Dish_Details.this, Cart.class);

        startActivity(intent);
    }
    public void onMenuDeatilCartBack(View v){
        onBackPressed();
    }
}
