package se.aroms;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
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

