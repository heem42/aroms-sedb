package se.aroms.DevelopersDotCo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.aroms.Devdroids.order_queue;

public class CustomRecyclerViewOrder extends RecyclerView.Adapter<MyViewHolderOrder> {

    private List<order_queue> ordersList;
    private int itemLayout;
    private Context mContext;
    public CustomRecyclerViewOrder(List<order_queue> items, int itemLayout, Context context) {
        this.ordersList = items;
        this.itemLayout = itemLayout;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyViewHolderOrder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View v = inflater.inflate(itemLayout, parent, false);

        // Return a new holder instance
        return new MyViewHolderOrder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderOrder holder, final int position) {
        if(ordersList != null && holder != null){
            final order_queue clickedOrder = getItem(position);
            //bind views here like text views and image views.
            holder.OrderNumber.setText("Order # " + (position+1));
            if(clickedOrder.getPriority() == null){
                holder.Priority.setText("Normal");
            }
            else if(clickedOrder.getPriority().equals("") ){
                holder.Priority.setText("Normal");
            }
            else{
                if (clickedOrder.getPriority().equals("1"))
                    holder.Priority.setText("High");
                else
                    holder.Priority.setText("Normal");
            }
            if(clickedOrder.getOrder_status().equals("0")){
                holder.status.setText("Status: In Queue");
            }
            else if(clickedOrder.getOrder_status().equals("1")){
                holder.status.setText("Status: Cooking");
            }
            else if(clickedOrder.getOrder_status().equals("2")){
                holder.status.setText("Status: Served");
            }
            else if(clickedOrder.getOrder_status().equals("3")){
                holder.status.setText("Status: Re-Cooking");
            }
            if(clickedOrder.getComplimentaryDish() != null){
                if (!(clickedOrder.getComplimentaryDish().equals("") || clickedOrder.getComplimentaryDish().equals("None"))){
                    holder.complimentaryDish.setText("Complimentary Dish: "+clickedOrder.getComplimentaryDish());
                }
            }
            else{
                holder.complimentaryDish.setText("Complimentary Dish: None");
            }
            holder.Specialize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpecializeOrder.class);
                    intent.putExtra("order",clickedOrder);
                    intent.putExtra("index",position);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(ordersList != null)
            return ordersList.size();
        else
            return 0;
    }
    public void setItems(List<order_queue> c){
        ordersList = c;
    }
    public order_queue getItem(int index){
        return ordersList.get(index);
    }

}