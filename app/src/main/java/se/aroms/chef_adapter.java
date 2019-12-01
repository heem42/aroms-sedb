package se.aroms;;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class chef_adapter extends RecyclerView.Adapter<chef_adapter.chefViewHolder>  {
    private Context mContext;
    private List<chefs> mUploads;

    public chef_adapter(Context context, List<chefs> uploads) {
        mContext = context;
        mUploads = uploads;
    }


    @Override
    public chefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_chef_row, parent, false);
        return new chefViewHolder(v);
    }

    @Override
    public void onBindViewHolder(chefViewHolder holder, int position) {
        chefs uploadCurrent = mUploads.get(position);
        holder.item.setText(uploadCurrent.getname());
        holder.sp.setText(uploadCurrent.getSpeciality());
        holder.q.setText(Integer.toString((int) uploadCurrent.getQueue()));



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class chefViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView order;
        public TextView item;

        public TextView sp;
        public TextView q;


        public chefViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item=itemView.findViewById(R.id.d);
            sp=itemView.findViewById(R.id.s);
            q=itemView.findViewById(R.id.q);

        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Context context = v.getContext();

                   Intent intent = new Intent(context, chef_queue.class);
                   chefs uploadCurrent = mUploads.get(pos);
                Toast.makeText(context.getApplicationContext(), uploadCurrent.getname(), Toast.LENGTH_SHORT).show();

            intent.putExtra("name",uploadCurrent.getname());
            intent.putExtra("key",uploadCurrent.getKey());//chef id
                   context.startActivity(intent);


        }
    }
}


