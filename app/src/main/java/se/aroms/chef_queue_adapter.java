package se.aroms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chef_queue_adapter  extends RecyclerView.Adapter<chef_queue_adapter.chef_queue_adapterViewHolder>  {
    private Context mContext;
    private List<dishlist3> mUploads;

    public chef_queue_adapter(Context context, List<dishlist3> uploads) {
        mContext = context;
        mUploads = uploads;
    }


    @Override
    public chef_queue_adapter.chef_queue_adapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.queue_row, parent, false);
        return new chef_queue_adapter.chef_queue_adapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(chef_queue_adapter.chef_queue_adapterViewHolder holder, int position) {
        dishlist3 uploadCurrent = mUploads.get(position);
        holder.item.setText(uploadCurrent.getDish());


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class chef_queue_adapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView order;
        public TextView item;


        public chef_queue_adapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item=itemView.findViewById(R.id.d);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Context context = v.getContext();
           // if (pos == 1) {
                   Intent intent = new Intent(context, chef_edit.class);
                   intent.putExtra("chefid",mUploads.get(pos).getChefid());
                   intent.putExtra("orderid",mUploads.get(pos).getorder_id());
                   intent.putExtra("itemname",mUploads.get(pos).getDish());
                   intent.putExtra("dishid",mUploads.get(pos).getDishid());
                 context.startActivity(intent);
            //}

        }
    }
}


