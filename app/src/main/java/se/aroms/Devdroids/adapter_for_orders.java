package se.aroms.Devdroids;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.aroms.R;

public class adapter_for_orders  extends RecyclerView.Adapter<adapter_for_orders.view_holder> {
    private List<orders_row> Orders;

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row, parent, false);
        view_holder vh = new view_holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {
       orders_row current_item = Orders.get(position);
       holder.dish_name.setText(current_item.getDishName());
       holder.quantity.setText(current_item.getQuantity());
       holder.size.setText(current_item.getSize());
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }

    public static class view_holder extends RecyclerView.ViewHolder{
        public TextView dish_name;
        public TextView quantity;
        public TextView size;
        public view_holder(View itemView)  {
            super(itemView);
            dish_name = itemView.findViewById(R.id.order_title);
            quantity=itemView.findViewById(R.id.order_quantity);
            size=itemView.findViewById(R.id.order_size);
        }
    }

    public adapter_for_orders(List<orders_row> order_list){
        this.Orders=order_list;
    }
}
