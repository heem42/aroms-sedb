package se.aroms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class kitchen_manager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_manager);
    }

    public void orderdetail(View view)
    { Intent intent = new Intent(getBaseContext(),order_details.class);
        startActivity(intent);
    }
    public void chefqueue(View view)
    { Intent intent = new Intent(getBaseContext(),chef.class);
        startActivity(intent);
    }
    public void specialorder(View view)
    { Intent intent = new Intent(getBaseContext(),specializeMain.class);
        startActivity(intent);
    }


}
