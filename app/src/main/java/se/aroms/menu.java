package se.aroms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class menu extends AppCompatActivity {

    Context c;
    ViewGroup vg;

    RecyclerView recyclerView;
    DatabaseReference menuDB;
    FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<menu_item, menuViewHolder> mFirebaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(c);
        c = getApplicationContext();

        FirebaseApp.initializeApp(this);
        menuDB = FirebaseDatabase.getInstance().getReference("Menu");

        Query query = menuDB.orderByChild("type");
        options = new FirebaseRecyclerOptions.Builder<menu_item>()
                .setQuery(query, menu_item.class).build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<menu_item, menuViewHolder>(options) {

            @Override
            protected void onBindViewHolder(menu.menuViewHolder holder, int position, menu_item model) {
                holder.setValues(model,c,vg);
            }

            @NonNull
            @Override
            public menu.menuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                vg = viewGroup;
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_layout,viewGroup,false);
                return new menu.menuViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(manager);
        mFirebaseAdapter.startListening();
        recyclerView.setAdapter(mFirebaseAdapter);

        ((Button) findViewById(R.id.menu_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), add_menu.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onStop() {
        if(mFirebaseAdapter!=null)
            mFirebaseAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseAdapter!=null)
            mFirebaseAdapter.startListening();

    }

    public static class menuViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView type;
        TextView time;
        LinearLayout mainLayout;

        public menuViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.menu_layout_name);
            this.price = (TextView) itemView.findViewById(R.id.menu_layout_price);
            this.type = (TextView) itemView.findViewById(R.id.menu_layout_type);
            this.time = (TextView) itemView.findViewById(R.id.menu_layout_time);
            this.mainLayout = (LinearLayout) itemView.findViewById(R.id.menu_layout_LL);
        }

        public void setValues(final menu_item a, final Context c, final ViewGroup vg){

            name.setText(a.getName());
            type.setText(a.getType());
            price.setText("Prices: Rs."+a.getReg_price()+" / Rs."+a.getLarge_price());
            time.setText("Ready in "+a.getTime()+" minutes");


            if (a.getType().equals("Beverage")){
                type.setTextColor(Color.DKGRAY);
            }
            else if (a.getType().equals("Chinese")){
                type.setTextColor(Color.CYAN);
            }
            else if (a.getType().equals("Desi")){
                type.setTextColor(Color.BLACK);
            }
            else if (a.getType().equals("Dessert")){
                type.setTextColor(Color.BLUE);
            }
            else if (a.getType().equals("Extras")){
                type.setTextColor(Color.MAGENTA);
            }
            else if (a.getType().equals("Italian")){
                type.setTextColor(Color.RED);
            }
            else if (a.getType().equals("Starter")){
                type.setTextColor(Color.YELLOW);
            }

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(vg.getContext(), edit_dish.class);

                    i.putExtra("uid",a.getUid());
                    i.putExtra("name",a.getName());
                    i.putExtra("reg_price",a.getReg_price());
                    i.putExtra("large_price",a.getLarge_price());
                    i.putExtra("reg_price_incurred",a.getReg_price_incurred());
                    i.putExtra("large_price_incurred",a.getLarge_price_incurred());
                    i.putExtra("time",a.getTime());
                    i.putExtra("description",a.getDescription());
                    i.putExtra("type",a.getType());
                    i.putExtra("picture",a.getPicture());

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                    c.startActivity(i);
                }
            });

        }
    }
}
