package se.aroms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import se.aroms.Devdroids.Orders;

public class MonthlyReportFragment extends Fragment {


    private ArrayList<Orders> orders;
    DatabaseReference usersDB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.monthly_report, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        orders = new ArrayList<>();
        usersDB = FirebaseDatabase.getInstance().getReference("Orders");
        getOrdersDetails();
    }

    void getOrdersDetails(){
        usersDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren()){
                    orders.add(i.getValue(Orders.class));
                }
                computeSalesAndProfit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void computeSalesAndProfit(){

        Date currDate = new Date();
        double []sales = new double[30];
        double []incurredCosts = new double[30];
        double []profit = new double[30];
        for(int i=0; i < orders.size(); i++){
            long diffInMillies = Math.abs(currDate.getTime() - orders.get(i).getOrderTIme().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(diff <=29 && orders.get(i).getStatus() == 0){
                int index = (int)diff;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
        }

        for(int i=0; i < 30; i++){
            profit[i] = ((sales[i] - incurredCosts[i])/sales[i]) * 100;
            if(Double.isNaN(profit[i]))
                profit[i] = 0.00;
        }

        GraphView salesGraph = (GraphView) getView().findViewById(R.id.salesGraph);
        LineGraphSeries<DataPoint> salesLine = new LineGraphSeries<>();
        GraphView profitGraph = (GraphView) getView().findViewById(R.id.profitGraph);
        LineGraphSeries<DataPoint> profitLine = new LineGraphSeries<>();

        for(int i=0; i <= 29; i++){
            salesLine.appendData(new DataPoint(i+1, sales[29-i]), true, 500);
            profitLine.appendData(new DataPoint(i+1, profit[29-i]), true, 500);
        }
        salesGraph.getViewport().setMinX(1);
        salesGraph.getViewport().setMaxX(30);
        salesGraph.getViewport().setScalable(true);
        salesGraph.getViewport().setScalableY(true);

        profitGraph.getViewport().setMinX(1);
        profitGraph.getViewport().setMaxX(30);
        profitGraph.getViewport().setScalable(true);
        profitGraph.getViewport().setScalableY(true);

        salesGraph.addSeries(salesLine);
        profitGraph.addSeries(profitLine);
    }
}
