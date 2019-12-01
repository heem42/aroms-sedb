package se.aroms.DevelopersDotCo;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import se.aroms.R;

public class MyViewHolderOrder extends RecyclerView.ViewHolder {

    public Button Specialize;
    public TextView Priority;
    public TextView OrderNumber;
    public TextView status;
    public TextView complimentaryDish;


    public MyViewHolderOrder(View view){
        super(view);
        Specialize = (Button) view.findViewById(R.id.SpecializeOrder);
        Priority = (TextView) view.findViewById(R.id.Priority);
        OrderNumber = (TextView) view.findViewById(R.id.OrderNumber);
        status = (TextView) view.findViewById(R.id.status);
        complimentaryDish = (TextView) view.findViewById(R.id.complimentaryDish);
    }

}
