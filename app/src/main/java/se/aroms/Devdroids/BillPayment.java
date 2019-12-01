package se.aroms.Devdroids;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import se.aroms.R;
import android.view.View;
import android.widget.TextView;

public class BillPayment extends AppCompatActivity {
    private Double tax=0.15;
    Orders orders;
    List<bill_rows> bills;
    private RecyclerView bills_rv;
    private RecyclerView.Adapter adapter_bill;
    private  RecyclerView.LayoutManager layoutManager_bills;
    DatabaseReference menuDB;
    private TextView bill_message;
    private TextView bill_title;
    private TextView bill_size;
    private TextView bill_quantity;
    private TextView bill_price;
    private TextView bill_tax;
    private TextView bill_tax_percent;
    private TextView bill_tax_amount;
    private TextView bill_total;
    private TextView bill_Tquanitity;
    private TextView bill_total_price;
    List<Dishes> dishes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);
        bill_message=findViewById(R.id.bill_message);
        bill_title=findViewById(R.id.bill_title);
        bill_size=findViewById(R.id.bill_size);
        bill_quantity=findViewById(R.id.bill_quantity);
        bill_price=findViewById(R.id.bill_price);
        bill_tax=findViewById(R.id.bill_tax);
        bill_tax_percent=findViewById(R.id.bill_percent);
        bill_tax_amount=findViewById(R.id.bill_tax_amount);
        bill_total=findViewById(R.id.bill_total);
        bill_Tquanitity=findViewById(R.id.bill_Tquantity);
        bill_total_price=findViewById(R.id.bill_total_price);
        menuDB= FirebaseDatabase.getInstance().getReference().child("Menu");
        Intent intent=getIntent();
        bills=new ArrayList<>();
        dishes=new ArrayList<>();
        orders=intent.getParcelableExtra("order");
        for(int i=0;i<orders.getOrderItems().size();i++){
            int chk=checkDishAlreadyDownloaded(orders.getOrderItems().get(i).getItemID(),orders.getOrderItems().get(i).getSize());
            if (chk==-1) {
                bills.add(new bill_rows("","1",orders.getOrderItems().get(i).getPrice(),orders.getOrderItems().get(i).getItemID(),orders.getOrderItems().get(i).getSize()));
            }
            else
            {
                int quantity=Integer.parseInt(bills.get(chk).getQunatity());
                Double price=bills.get(chk).getPrice();
                price+=orders.getOrderItems().get(i).getPrice();
                quantity++;
                bills.get(chk).setQunatity(String.valueOf(quantity));
                bills.get(chk).setPrice(price);
            }
        }
        for(int i=0;i<bills.size();i++){
            menuDB.child(bills.get(i).getDishId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dishes.add(dataSnapshot.getValue(Dishes.class));
                    if(dishes.size()==bills.size())
                    {
                        updateNames();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }
        bills_rv = findViewById(R.id.bill_payment_rv);
    }
    public int checkDishAlreadyDownloaded(String uid,String size){
        for(int i=0;i<bills.size();i++){
            if(bills.get(i).getDishId().compareTo(uid)==0 && bills.get(i).getSize().compareTo(size)==0)
            {
                return i;
            }
        }
        return -1;
    }
    public void updateNames()
    {
        for (int i=0;i<bills.size();i++)
        {
            for(int j=0;j<dishes.size();j++)
            {
                if(bills.get(i).getDishId().compareTo(dishes.get(j).getUid())==0)
                {
                    bills.get(i).setName(dishes.get(j).getName());
                    break;
                }
            }
        }
        //adapter_bill.notifyDataSetChanged();
    }
    private double totalPrice(){
        Double price=0.0;
        for(int i=0;i<bills.size();i++){
            price+=bills.get(i).getPrice()*Integer.parseInt(bills.get(i).getQunatity());
        }
        return price;
    }
    private int totalQuantity(){
        int price=0;
        for(int i=0;i<bills.size();i++){
            price+=Integer.parseInt(bills.get(i).getQunatity());
        }
        return price;
    }
    public void onClick(View v){
        Double total_price=totalPrice();
        int totalQuantity=totalQuantity();
        bills_rv.setVisibility(View.VISIBLE);
        bill_message.setVisibility(View.VISIBLE);
        bill_title.setVisibility(View.VISIBLE);
        bill_size.setVisibility(View.VISIBLE);
        bill_quantity.setVisibility(View.VISIBLE);
        bill_price.setVisibility(View.VISIBLE);
        bill_tax.setVisibility(View.VISIBLE);
        bill_tax_percent.setVisibility(View.VISIBLE);
        bill_tax_percent.setText(tax*100+"%");
        bill_tax_amount.setVisibility(View.VISIBLE);
        bill_tax_amount.setText(total_price*tax+"RS");
        bill_total.setVisibility(View.VISIBLE);
        Double taxed=total_price*tax;
        bill_Tquanitity=findViewById(R.id.bill_Tquantity);
        bill_Tquanitity.setText(totalQuantity+"");
        bill_total_price=findViewById(R.id.bill_total_price);
        bill_total_price.setText(total_price+taxed+"");
bill_Tquanitity.setVisibility(View.VISIBLE);
bill_total_price.setVisibility(View.VISIBLE);
        bills_rv.setHasFixedSize(true);
        layoutManager_bills = new LinearLayoutManager(this);
        adapter_bill = new adapter_for_bill(bills);
        bills_rv.setLayoutManager(layoutManager_bills);
        bills_rv.setAdapter(adapter_bill);
    }
}
