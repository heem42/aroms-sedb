package se.aroms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.aroms.Devdroids.MenuActivityDev;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Button loginBtn;
    private TextView email, password;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginBtn);
        email = findViewById(R.id.name);
        password = findViewById(R.id.password);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        View bg = findViewById(R.id.login_layout);
//        Drawable backgroundImg = bg.getBackground();
//        backgroundImg.setAlpha(200);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Hall_Manager_Main.class);
                startActivity(intent);
                //loginAuthenticate();
            }
        });
    }


    private void loginAuthenticate() {
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();
        boolean isFill = false;
        if (emailTxt.isEmpty()) {
            email.setError("Fill this field");
            isFill = true;
        }
        if (passwordTxt.isEmpty()) {
            password.setError("Fill this field");
            isFill = true;
        }
        if (!isFill) {
            mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Message: ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                uid = user.getUid();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication Failed",
                                        Toast.LENGTH_LONG).show();
                                uid = null;
                            }

                            if (uid != null) {
                                databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        String role = user.getRole();
                                        String roleSelected = ((Spinner) findViewById(R.id.dropdown)).getSelectedItem().toString();
                                        if (role.equals(roleSelected)) {
                                            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                            Toast.makeText(Login.this, "Login Successful",
                                                    Toast.LENGTH_LONG).show();
                                            if (roleSelected.equals("Admin")) {
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                startActivity(intent);
                                            } else if (roleSelected.equals("Inventory Manager")) {
                                                Intent intent = new Intent(getBaseContext(), inventory_manager_main_page.class);
                                                startActivity(intent);
                                            } else if (roleSelected.equals("Hall Manager")) {
                                                Intent intent = new Intent(getBaseContext(),Hall_Manager_Main.class);
                                                startActivity(intent);

                                            } else if (roleSelected.equals("Kitchen Manager")) {
                                                Intent intent = new Intent(getBaseContext(), specializeMain.class);
                                                startActivity(intent);
                                            } else if (roleSelected.equals("Customer")) {
                                                Intent intent = new Intent(getBaseContext(), MenuActivityDev.class);
                                                startActivity(intent);
                                            } else if (roleSelected.equals("Chef")){
                                                Intent intent = new Intent(getBaseContext(), cookMain.class);
                                                startActivity(intent);
                                            }
                                        } else {
                                            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                            Toast.makeText(Login.this, "Login Failed",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                // ...
                            } else {
                                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                Toast.makeText(Login.this, "No Login Found",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else {
            ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
        }
    }
}
