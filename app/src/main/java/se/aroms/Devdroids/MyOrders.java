package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.aroms.MainActivity;
import se.aroms.Order;
import se.aroms.R;

public class MyOrders extends AppCompatActivity {
    DatabaseReference orderDB;
    List<order_queue> Orders;
    private Handler handler;

    private final int TIMER = 1000*60;
    List<orders_row> rows;
    private int remiaingTime;
    private TextView remaining;
    DatabaseReference orderQueue;
    FirebaseAuth auth;
    boolean orderReady;
    Context context;
    Button editOrder;
    Runnable r1;
    boolean disable=true;
    Button cancelOrder;
    DatabaseReference cartDB;
    DatabaseReference menuDB;
    private RecyclerView cart_rv;
    private RecyclerView.Adapter adapter_cart;
    private  RecyclerView.LayoutManager layoutManager_order;
    private List<Dishes> dishes;
    TextView thanks;
    TextView empty;
    @Override
    protected void onStart() {
        super.onStart();
        scheduleuUpdateTime();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Orders=new ArrayList<>();
        rows=new ArrayList<>();
        dishes=new ArrayList<>();
        orderReady=false;
        cancelOrder=findViewById(R.id.myorder_cancel);
        remaining=findViewById(R.id.order_remaining);
        remiaingTime=45;
                empty=findViewById(R.id.empty_order);
        editOrder=findViewById(R.id.myorder_edit);
        handler=new Handler();
        orderQueue= FirebaseDatabase.getInstance().getReference().child("Orders Queue");
        auth=FirebaseAuth.getInstance();
        context=this;
       thanks=findViewById(R.id.order_thank);

        orderDB = FirebaseDatabase.getInstance().getReference().child("Orders");
        cartDB = FirebaseDatabase.getInstance().getReference().child("Cart");
        menuDB=FirebaseDatabase.getInstance().getReference().child("Menu");

        orderQueue.orderByChild("uid").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Orders.clear();
                rows.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    order_queue order=(postSnapshot.getValue(order_queue.class));
                    if(order.getComplimentaryDish().compareTo("NONE")==0 && order.getOrder_status().compareTo("-1")!=0){
                        Orders.add(order);
                        if(Orders.get(0).getOrder_status().compareTo("2")==0){
                            orderReady=true;
                        }

                    }

                }
                disable=true;
                if(Orders.size()>0){
                    empty.setVisibility(View.INVISIBLE);
                    remaining.setVisibility(View.VISIBLE);
                    thanks.setVisibility(View.VISIBLE);
                    remaining.setText("Remaining Time:"+remiaingTime);
                    updateRemainingTime();

                }
                for (int i=0;i<Orders.size();i++)
                {
                    for(int j=0;j<Orders.get(i).getOrderItems().size();j++) {
                        if(Orders.get(i).getOrderItems().get(j).getStatus().compareTo("0")==0){
                            disable=false;
                        }
                        int chk=checkDishAlreadyDownloaded(Orders.get(i).getOrderItems().get(j).getItemID(),Orders.get(i).getOrderItems().get(j).getSize());
                        if (chk==-1) {
                            rows.add(new orders_row("","1",Orders.get(i).getOrderItems().get(j).getItemID(),Orders.get(i).getOrderItems().get(j).getSize()));
                        }
                        else
                        {
                            int quantity=Integer.parseInt(rows.get(chk).getQuantity());
                            quantity++;
                            rows.get(chk).setQuantity(String.valueOf(quantity));
                        }
                    }
                }
                if(disable){
                    //editOrder.setBackgroundColor(getColor(R.color.grey));
                }
                int k=0;
                dishes.clear();
                for(int i=0;i<rows.size();i++){
                    menuDB.child(rows.get(i).getDishId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dishes.add(dataSnapshot.getValue(Dishes.class));
                            if(dishes.size()==rows.size())
                            {
                                updateNames();
                                if(orderReady){
                                    pushInOrders();
                                }
                            }
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
        cart_rv = findViewById(R.id.orders_rv);
        cart_rv.setHasFixedSize(true);
        layoutManager_order = new LinearLayoutManager(this);
        adapter_cart = new adapter_for_orders(rows);
        cart_rv.setLayoutManager(layoutManager_order);
        cart_rv.setAdapter(adapter_cart);
    }
    public int checkDishAlreadyDownloaded(String uid,String size){
        for(int i=0;i<rows.size();i++){
            if(rows.get(i).getDishId().compareTo(uid)==0 && rows.get(i).getSize().compareTo(size)==0)
            {
                return i;
            }
        }
        return -1;
    }
    public void updateNames()
    {
        for (int i=0;i<rows.size();i++)
        {
            for(int j=0;j<dishes.size();j++)
            {
                if(rows.get(i).getDishId().compareTo(dishes.get(j).getUid())==0)
                {
                    rows.get(i).setDishName(dishes.get(j).getName());
                    break;
                }
            }
        }
        adapter_cart.notifyDataSetChanged();
    }
    public  void onOrderCancelCLick (View v) {
        if (Orders.size() > 0) {
            if (Orders.get(0).getOrder_status().compareTo("0") != 0) {
                Toast.makeText(context, "Order in cooking queue can not be deleted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context, "Order cancelled", Toast.LENGTH_SHORT).show();
            List<order_queue_items> orderItems = Orders.get(0).getOrderItems();
            List<Cart_item> carts;
            carts = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                Dishes dish = dishes.get(0);
                for (int j = 0; j < dishes.size(); j++) {
                    if (dishes.get(j).getUid().compareTo(rows.get(i).getDishId()) == 0) {
                        dish = dishes.get(j);
                        break;
                    }
                }
                cart_Items cart_items = new cart_Items(rows.get(i).getDishId(), rows.get(i).getSize(), rows.get(i).getDishName(), dish.getDescription(), dish.getPicture(), dish.getTime(), "");

                carts.add(new Cart_item(cart_items, Integer.parseInt(rows.get(i).getQuantity()), 0));
            }
            for (int i = 0; i < carts.size(); i++) {
                cartDB.child(auth.getUid()).child(carts.get(i).getItems().getItemID() + carts.get(i).getItems().getSize()).setValue(carts.get(i));
            }
            Orders.get(0).setOrder_status("-1");
            for (int i = 0; i < Orders.get(0).getOrderItems().size(); i++) {
                Orders.get(0).getOrderItems().get(i).setStatus("-1");
            }
            orderQueue.child(Orders.get(0).getOrderId()).setValue(Orders.get(0));
            Intent intent = new Intent(this, MenuActivityDev.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "Kindly place order before", Toast.LENGTH_SHORT).show();
        }
    }
    public  void onOrderEditCLick (View v){
        if(Orders.size()>0) {
            if (disable) {
                Toast.makeText(context, "Order in cooking queue can not be edited", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else
        {
            Toast.makeText(context, "Kindly place order before", Toast.LENGTH_SHORT).show();

        }

    }
    public void pushInOrders() {
        orderReady=false;
        handler.removeCallbacks(r1);
        List<order_queue_items> orderItems = Orders.get(0).getOrderItems();
        List<orders_items> items;
        items=new ArrayList<>();
        for (int i = 0; i < orderItems.size(); i++) {
            Dishes dish=dishes.get(0);
            double price=0.0;
            double incured=0.0;
            for(int j=0;j<dishes.size();j++)
            {
                if(dishes.get(j).getUid().compareTo(orderItems.get(i).getItemID())==0)
                {
                    dish=dishes.get(j);
                    break;
                }

            }
            if(orderItems.get(i).getSize().compareTo("Regular")==0)
            {
                price=Double.parseDouble(dish.getReg_price());
                incured=Double.parseDouble(dish.getReg_price_incurred());
            }
            else
            {
                price=Double.parseDouble(dish.getLarge_price());
                incured=Double.parseDouble(dish.getLarge_price_incurred());

            }
            orders_items orders_item=new orders_items(orderItems.get(i).getItemID(),orderItems.get(i).getSize(),price,incured);
            items.add(orders_item);
        }
        Orders orders=new Orders(new Date(),items,"hi",auth.getUid(),0);
        orderQueue.child(Orders.get(0).getOrderId()).removeValue();
        String key = orderDB.push().getKey();
        orders.setOrderId(key);
        orderDB.child(key).setValue(orders);
        Intent intent=new Intent(this,BillPayment.class);
        intent.putExtra("order",orders);
        startActivity(intent);
    }

    public void scheduleuUpdateTime() {
        r1=new Runnable() {
            public void run() {
                updateTime();          // this method will contain your almost-finished HTTP calls
                handler.postDelayed(this, TIMER);
            }
        };
        handler.postDelayed(r1, TIMER);
    }
    public void updateTime(){
        remiaingTime-=TIMER/(1000*6000);
        remaining.setText("Remaining Time: "+remiaingTime);
    }
    public void updateRemainingTime(){
        int time=-1;
        for(int i=0;i<Orders.get(0).getOrderItems().size();i++)
        {
            order_queue_items orderQueue=Orders.get(0).getOrderItems().get(i);
            int temp=0;
            try{
                temp=Integer.parseInt(orderQueue.getExpected_time())+Integer.parseInt(orderQueue.getRequired_time());

            }
            catch (Exception ex)
            {
              temp=-1;
            }
            if(temp!=-1){
                if(time<temp){
                    time=temp;
                }
            }
        }
        remiaingTime=time;
        remaining.setText("Remaining Time: "+remiaingTime);
    }
}
