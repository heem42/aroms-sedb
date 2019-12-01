package se.aroms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chef_display_adapter extends RecyclerView.Adapter<chef_display_adapter.chef_display_adapterViewHolder>  {
        private Context mContext;
        private List<dishlist3> mUploads;

        public chef_display_adapter(Context context, List<dishlist3> uploads) {
            mContext = context;
            mUploads = uploads;
        }


        @Override
        public chef_display_adapter.chef_display_adapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chef_display_row, parent, false);
            return new chef_display_adapter.chef_display_adapterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(chef_display_adapter.chef_display_adapterViewHolder holder, int position) {
            dishlist3 uploadCurrent = mUploads.get(position);
            holder.item.setText(uploadCurrent.getDish());


        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class chef_display_adapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView order;
            public TextView item;


            public chef_display_adapterViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                item=itemView.findViewById(R.id.d);
            }

            @Override
            public void onClick(View v) {
                int pos = getLayoutPosition();
                Context context = v.getContext();
            }
        }
    }

