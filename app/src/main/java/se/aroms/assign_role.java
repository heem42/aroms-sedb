package se.aroms;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class assign_role extends AppCompatActivity {

    DatabaseReference usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_role);

        Intent i =  getIntent();
        if(i.hasExtra("email")){
            String email = i.getStringExtra("email");
            ((EditText) findViewById(R.id.assign_role_email)).setText(email);
            ((EditText) findViewById(R.id.assign_role_email)).setEnabled(false);
        }

        FirebaseApp.initializeApp(this);
        usersDB = FirebaseDatabase.getInstance().getReference("Users");
        //rolesDB.setValue("hello");
    }

    public void onClickBtn(View v){
        EditText a = findViewById(R.id.assign_role_email);
        String email = a.getText().toString();

        final String role = ((Spinner) findViewById(R.id.assign_role_spinner)).getSelectedItem().toString();

        if(!email.isEmpty()&&!role.equals("Select role")){
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //usersDB.child(email).setValue(role);
                Query Query = usersDB.orderByChild("email").equalTo(email);
                Query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String uid = singleSnapshot.getKey();
                                usersDB.child(uid).child("role").setValue(role);
                                finish();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                Toast.makeText(getApplicationContext(), "Role assigned Successfully!", Toast.LENGTH_LONG).show();
                                startActivity(i);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Email not found!",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
                //Toast.makeText(this,"Role assigned successfully!",Toast.LENGTH_LONG).show();
            }
            else{a.setError("Invalid email type");}

        }
        else{
            if(email.isEmpty())
                a.setError("Fill this field");
            if(role.equals("Select role"))
                Toast.makeText(this,"Select a valid role",Toast.LENGTH_LONG).show();
        }
    }
}
