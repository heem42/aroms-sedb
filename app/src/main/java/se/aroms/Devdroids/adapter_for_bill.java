package se.aroms.Devdroids;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import se.aroms.R;

public class adapter_for_bill extends RecyclerView.Adapter<adapter_for_bill.view_holder> {
    private List<bill_rows> Orders;

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_payment_row, parent, false);
        view_holder vh = new view_holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {
        bill_rows current_item = Orders.get(position);
        holder.dish_name.setText(current_item.getName());
        holder.quantity.setText(current_item.getQunatity());
        holder.price.setText(current_item.getPrice()+"");
        holder.size.setText(current_item.getSize());
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }

    public static class view_holder extends RecyclerView.ViewHolder{
        public TextView dish_name;
        public TextView quantity;
        public TextView price;
        public TextView size;
        public view_holder(View itemView)  {
            super(itemView);
            dish_name = itemView.findViewById(R.id.row_bill_payment_title);
            quantity=itemView.findViewById(R.id.row_bill_payment_quantity);
            price=itemView.findViewById(R.id.row_bill_payment_price);
            size=itemView.findViewById(R.id.row_bill_payment_size);

        }
    }

    public adapter_for_bill(List<bill_rows> order_list){
        this.Orders=order_list;
    }
}
