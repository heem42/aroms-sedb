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

public class YearlyReportFragment extends Fragment {

    private ArrayList<Orders> orders;
    DatabaseReference usersDB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.yearly_report, container, false);
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
        double []sales = new double[12];
        double []incurredCosts = new double[12];
        double []profit = new double[12];
        for(int i=0; i < orders.size(); i++){
            long diffInMillies = Math.abs(currDate.getTime() - orders.get(i).getOrderTIme().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(diff >=0 && diff <=30 && orders.get(i).getStatus() == 0){
                int index = 0;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=31 && diff <=60 && orders.get(i).getStatus() == 0){
                int index = 1;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=61 && diff <=90 && orders.get(i).getStatus() == 0){
                int index = 2;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=91 && diff <=120 && orders.get(i).getStatus() == 0){
                int index = 3;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=121 && diff <=150 && orders.get(i).getStatus() == 0){
                int index = 4;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=151 && diff <=180 && orders.get(i).getStatus() == 0){
                int index = 5;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=181 && diff <=210 && orders.get(i).getStatus() == 0){
                int index = 6;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=211 && diff <=240 && orders.get(i).getStatus() == 0){
                int index = 7;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=241 && diff <=270 && orders.get(i).getStatus() == 0){
                int index = 8;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=271 && diff <=300 && orders.get(i).getStatus() == 0){
                int index = 9;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=301 && diff <=330 && orders.get(i).getStatus() == 0){
                int index = 10;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
            if(diff >=331 && diff <=360 && orders.get(i).getStatus() == 0){
                int index = 11;
                for(int j=0; j < orders.get(i).getOrderItems().size(); j++){
                    sales[index] = sales[index] + orders.get(i).getOrderItems().get(j).getPrice();
                    incurredCosts[index] = incurredCosts[index] + orders.get(i).getOrderItems().get(j).getIncured_price();
                }
            }
        }

        for(int i=0; i < 12; i++){
            profit[i] = ((sales[i] - incurredCosts[i])/sales[i]) * 100;
            if(Double.isNaN(profit[i]))
                profit[i] = 0.00;
        }

        GraphView salesGraph = (GraphView) getView().findViewById(R.id.salesGraph);
        LineGraphSeries<DataPoint> salesLine = new LineGraphSeries<>();
        GraphView profitGraph = (GraphView) getView().findViewById(R.id.profitGraph);
        LineGraphSeries<DataPoint> profitLine = new LineGraphSeries<>();

        for(int i=0; i <= 11; i++){
            salesLine.appendData(new DataPoint(i+1, sales[11-i]), true, 500);
            profitLine.appendData(new DataPoint(i+1, profit[11-i]), true, 500);
        }
        salesGraph.getViewport().setMinX(1);
        salesGraph.getViewport().setMaxX(12);
        salesGraph.getViewport().setScalable(true);
        salesGraph.getViewport().setScalableY(true);

        profitGraph.getViewport().setMinX(1);
        profitGraph.getViewport().setMaxX(12);
        profitGraph.getViewport().setScalable(true);
        profitGraph.getViewport().setScalableY(true);

        salesGraph.addSeries(salesLine);
        profitGraph.addSeries(profitLine);
    }
}
