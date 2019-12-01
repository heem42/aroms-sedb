package se.aroms;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class add_menu extends AppCompatActivity {

    private StorageReference mStorageRef;
    DatabaseReference menuDB;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private ImageView mImageView;
    private Uri mImageUri;
    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);


        mButtonChooseImage = findViewById(R.id.add_menu_add_image_btn);
        mImageView = findViewById(R.id.add_menu_imageView);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        FirebaseApp.initializeApp(this);
        menuDB = FirebaseDatabase.getInstance().getReference("Menu");
        mStorageRef = FirebaseStorage.getInstance().getReference("dish_uploads");
        //menuDB.setValue("hello");
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void uploadFile(final String uid,String name) {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(uid+"/"+name);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    menuDB.child(uid).child("picture").setValue(uri.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_menu.this, "Picture upload unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
//            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            menuDB.child(uid).child("picture").setValue("https://firebasestorage.googleapis.com/v0/b/aroms-9adaf.appspot.com/o/noimage.jpg?alt=media&token=8231970c-160b-4fc7-98f2-c404aab5c647");
        }
    }

    public void onClickBtn(View v) {
        ((ProgressBar) findViewById(R.id.add_menu_progressBar)).setVisibility(View.VISIBLE);
        EditText a = (EditText) findViewById(R.id.add_menu_name);
        String name = a.getText().toString();

        EditText b = (EditText) findViewById(R.id.add_menu_regular_price);
        String reg_price = b.getText().toString();

        EditText c = (EditText) findViewById(R.id.add_menu_large_price);
        String large_price = c.getText().toString();

        EditText d = (EditText) findViewById(R.id.add_menu_time);
        String time = d.getText().toString();

        EditText e = (EditText) findViewById(R.id.add_menu_regular_price_incurred);
        String reg_price_incurred = e.getText().toString();

        EditText f = (EditText) findViewById(R.id.add_menu_large_price_incurred);
        String large_price_incurred = f.getText().toString();

        EditText g = (EditText) findViewById(R.id.add_menu_description);
        String description = g.getText().toString();

        String type = ((Spinner) findViewById(R.id.add_menu_spinner)).getSelectedItem().toString();

        if(!name.isEmpty()&&!reg_price.isEmpty()&&!large_price.isEmpty()&&!time.isEmpty()&&!reg_price_incurred.isEmpty()&&!large_price_incurred.isEmpty()&&!description.isEmpty()&&!type.equals("Select dish type")){
            String key = menuDB.push().getKey();

            menuDB.child(key).child("uid").setValue(key);
            menuDB.child(key).child("name").setValue(name);
            menuDB.child(key).child("reg_price").setValue(reg_price);
            menuDB.child(key).child("large_price").setValue(large_price);
            menuDB.child(key).child("reg_price_incurred").setValue(reg_price_incurred);
            menuDB.child(key).child("large_price_incurred").setValue(large_price_incurred);
            menuDB.child(key).child("time").setValue(time);
            menuDB.child(key).child("type").setValue(type);
            menuDB.child(key).child("description").setValue(description);

            uploadFile(key,name);

            Toast.makeText(this,"Dish added successfully",Toast.LENGTH_LONG).show();
            ((ProgressBar) findViewById(R.id.add_menu_progressBar)).setVisibility(View.INVISIBLE);
            finish();
            Intent i = new Intent(getApplicationContext(), add_dish_ingredient.class);
            i.putExtra("uid",key);
            startActivity(i);
        }
        else{
            ((ProgressBar) findViewById(R.id.add_menu_progressBar)).setVisibility(View.INVISIBLE);
            if(name.isEmpty())
                a.setError("Fill this field");

            if(reg_price.isEmpty())
                b.setError("Fill this field");

            if(large_price.isEmpty())
                c.setError("Fill this field");

            if(time.isEmpty())
                d.setError("Fill this field");

            if(reg_price_incurred.isEmpty())
                e.setError("Fill this field");

            if(large_price_incurred.isEmpty())
                f.setError("Fill this field");

            if(description.isEmpty())
                g.setError("Fill this field");

            if(type.equals("Select dish type"))
                Toast.makeText(this,"Select a valid dish type",Toast.LENGTH_LONG).show();
        }


    }
}
