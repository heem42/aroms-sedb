package se.aroms.Devdroids;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import se.aroms.R;

public class adapter_for_cart extends RecyclerView.Adapter<adapter_for_cart.view_holder> {
    private List<Cart_item> my_array_of_cart;
    private OnViewListener onViewListener;
    private RecyclerView.ViewHolder viewHolder;
    public adapter_for_cart(List<Cart_item> cart_list, OnViewListener onViewListener){
        my_array_of_cart= cart_list;
        this.onViewListener=onViewListener;

    }
    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        view_holder vh = new view_holder(v,onViewListener);
        this.viewHolder=vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final view_holder holder, int position) {
        final Cart_item current_item = my_array_of_cart.get(position);
        final view_holder viewHolder = (view_holder) holder;
        if (current_item.getItems().getPicture() != null) {
            Picasso.get().load(current_item.getItems().getPicture()).fit().centerCrop().into(viewHolder.dish_image, new Callback() {
                @Override
                public void onSuccess() {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                    viewHolder.dish_image.setImageResource(R.drawable.notavailable);
                }
            });
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.dish_image.setImageResource(R.drawable.notavailable);
        }
        holder.dish_name.setText(current_item.getItems().getName()+" "+current_item.getItems().getSize() );
        holder.dish_desp.setText(current_item.getItems().getDesp());
        holder.quantity.setText(""+current_item.getQuantity());
        holder.quantityWarning.setText(current_item.getItems().getQunatity());
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0)
                current_item.setLocalQuantity(Integer.parseInt(s.toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_array_of_cart.size();
    }

    public class view_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView dish_image;
        public TextView dish_name;
        public ProgressBar progressBar;
        public OnViewListener onViewListener;
        public TextView dish_desp;
        public Button delete;
        public TextView quantityWarning;
        public EditText quantity;
        public view_holder(@NonNull View itemView,OnViewListener onViewListener) {
            super(itemView);
            this.quantityWarning=itemView.findViewById(R.id.maxQuantityCart);
            this.dish_desp=itemView.findViewById(R.id.cart_dish_desp);
            this.dish_image=itemView.findViewById(R.id.cart_dish_img);
            this.dish_name=itemView.findViewById(R.id.cart_dish_title);
            this.progressBar=itemView.findViewById(R.id.row_progress_cart);
            this.onViewListener=onViewListener;
            this.delete=itemView.findViewById(R.id.cart_delete);
            this.delete.setOnClickListener(this);
            this.quantity=itemView.findViewById(R.id.cart_quantity);
        }

        @Override
        public void onClick(View v) {
            onViewListener.onViewClick(getAdapterPosition());
        }
    }
    public interface OnViewListener{

        void onViewClick(int position);
    }
}
