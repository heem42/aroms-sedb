package se.aroms.DevelopersDotCo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import se.aroms.R;

public class MyViewHolderDishes extends RecyclerView.ViewHolder {
    public TextView Name;
    public TextView Price;
    public TextView Time;
    public Button Recook;


    public MyViewHolderDishes(View view){
        super(view);
        Name = (TextView) view.findViewById(R.id.DishName);
        Price = (TextView) view.findViewById(R.id.Price);
        Time = (TextView) view.findViewById(R.id.Time);
        Recook = (Button) view.findViewById(R.id.Recook);
    }
}
