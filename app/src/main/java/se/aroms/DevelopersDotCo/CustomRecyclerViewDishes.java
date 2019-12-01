package se.aroms.DevelopersDotCo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import se.aroms.Devdroids.Dishes;
import se.aroms.Devdroids.order_queue;


public class CustomRecyclerViewDishes extends RecyclerView.Adapter<MyViewHolderDishes> {
    private List<Dishes> dishesList;
    public ArrayList<Integer> ChangedStatusIndexes;
    private int itemLayout;
    private Context mContext;
    private order_queue orderItems;
    private DatabaseReference DB;
    public CustomRecyclerViewDishes(List<Dishes> items, order_queue orderItems, int itemLayout, Context context) {
        this.dishesList = items;
        this.itemLayout = itemLayout;
        this.mContext = context;
        this.orderItems = orderItems;
        this.ChangedStatusIndexes = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolderDishes onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View v = inflater.inflate(itemLayout, parent, false);

        // Return a new holder instance
        return new MyViewHolderDishes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderDishes holder, final int position) {
        if(dishesList != null && holder != null){
            final Dishes clickedOrder = getItem(position);
            //bind views here like text views and image views.
            holder.Name.setText((position+1) +" "+ clickedOrder.getName());
            if(orderItems.getOrderItems().get(position).getStatus().equals("0")){
                holder.Price.setText("In Queue");
            }
            else if(orderItems.getOrderItems().get(position).getStatus().equals("1")){
                holder.Price.setText("Cooking");
            }
            else if(orderItems.getOrderItems().get(position).getStatus().equals("2")){
                holder.Price.setText("Served");
            }
            else if(orderItems.getOrderItems().get(position).getStatus().equals("3")){
                holder.Price.setText("Re-Cooking");
                holder.Recook.setVisibility(View.INVISIBLE);
            }
            holder.Time.setText(clickedOrder.getTime()+" mins");
            holder.Recook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DB= FirebaseDatabase.getInstance().getReference().child("Orders Queue").child(orderItems.getOrderId()).child("orderItems")
                            .child(String.valueOf(position)).child("status");
                    DB.setValue("3");
                    holder.Price.setText("Re-Cooking");
                    holder.Recook.setVisibility(View.INVISIBLE);
                    ChangedStatusIndexes.add(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(dishesList != null)
            return dishesList.size();
        else
            return 0;
    }
    public void setItems(List<Dishes> c){
        dishesList = c;
    }
    public Dishes getItem(int index){
        return dishesList.get(index);
    }

}
