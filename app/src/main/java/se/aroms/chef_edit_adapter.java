package se.aroms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class chef_edit_adapter extends RecyclerView.Adapter<chef_edit_adapter.chef_edit_adapterViewHolder>  {
        private Context mContext;
        private List<chefs> mUploads;
        private dishlist3 dstoreinadapter;

    public chef_edit_adapter(Context context, List<chefs> uploads,dishlist3 s) {
            mContext = context;
            mUploads = uploads;
            dstoreinadapter=s;
        }


        @Override
        public chef_edit_adapter.chef_edit_adapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.chef_edit_row, parent, false);
            return new chef_edit_adapter.chef_edit_adapterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(chef_edit_adapter.chef_edit_adapterViewHolder holder, int position) {
            chefs uploadCurrent = mUploads.get(position);
            holder.item.setText(uploadCurrent.getname());
            holder.sp.setText(uploadCurrent.getSpeciality());
            holder.q.setText(Integer.toString((int) uploadCurrent.getQueue()));



        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class chef_edit_adapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView order;
            public TextView item;

            public TextView sp;
            public TextView q;


            public chef_edit_adapterViewHolder(View itemView) {
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

               ///add row in other chef
                DatabaseReference An = FirebaseDatabase.getInstance().getReference("chef");
                String uploaddishId = An.push().getKey();
                dish_list2 f=new dish_list2();
                f.setDish(dstoreinadapter.getDishid());
                f.setOrder_id(dstoreinadapter.getorder_id());

                An.child(mUploads.get(pos).getKey()).child("dishes").child(uploaddishId).setValue(f);
                final DatabaseReference An1 =FirebaseDatabase.getInstance().getReference("chef");
final String l=mUploads.get(pos).getKey();
                FirebaseDatabase.getInstance().getReference().child("chef").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot12) {
                        for (DataSnapshot ds12 : dataSnapshot12.getChildren()) {
                            chef_list n = ds12.getValue(chef_list.class);
                                if(ds12.getKey().equalsIgnoreCase(l))
                                {
                                    long q=n.getQueue();
                                    q=q+1;
                                    An1.child(l).child("queue").setValue(q);

                                }
                        }
                    }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                ;

                    //long q=n.getQueue();
                    //  q=q+1;

                    //An1.child(mUploads.get(pos).getKey()).child("queue").setValue(q);


                //delete from chef

                deletefromchef(dstoreinadapter,mUploads.get(pos).getKey());
            }
        }

    private void deletefromchef(final dishlist3 dstoreinadapter,final String pos) {
        final DatabaseReference An1 =FirebaseDatabase.getInstance().getReference("chef");

        FirebaseDatabase.getInstance().getReference().child("chef").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot12) {
                for (DataSnapshot ds12 : dataSnapshot12.getChildren()) {
                    chef_list n = ds12.getValue(chef_list.class);
    //if(pos.equalsIgnoreCase(ds12.getKey())) {
    //  long q = n.getQueue();
    //An1.child(pos).child("queue").setValue(q + 1);
    //}

                    if (dstoreinadapter.getChefid().equalsIgnoreCase(ds12.getKey())) {

                        for (String key11 : n.getDishes().keySet()) {

                            dish_list2 d = n.getDishes().get(key11);

                            if (d.getOrder_id().equalsIgnoreCase(dstoreinadapter.order_id) && d.getDish().equalsIgnoreCase(dstoreinadapter.dishid)) {
                                      An1.child(dstoreinadapter.getChefid()).child("dishes").child(key11).removeValue();
                                      long q = n.getQueue();
                                      q = q - 1;
                                      An1.child(dstoreinadapter.getChefid()).child("queue").setValue(q);
                                  Intent intent = new Intent(mContext.getApplicationContext(), chef.class);
                                mContext.startActivity(intent);
                            }
                        }

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
