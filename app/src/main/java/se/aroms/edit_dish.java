package se.aroms;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class edit_dish extends AppCompatActivity {

    String uid;
    DatabaseReference menuDB,ingredientDB;
    private StorageReference mStorageRef;
    Dialog mDialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private ImageView mImageView;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        Intent i = getIntent();
        if(i.hasExtra("uid")){
            uid = i.getStringExtra("uid");
            String name = i.getStringExtra("name");
            String reg_price = i.getStringExtra("reg_price");
            String large_price = i.getStringExtra("large_price");
            String reg_price_incurred = i.getStringExtra("reg_price_incurred");
            String large_price_incurred = i.getStringExtra("large_price_incurred");
            String description = i.getStringExtra("description");
            String time = i.getStringExtra("time");
            String type = i.getStringExtra("type");
            picture = i.getStringExtra("picture");

            ((EditText)findViewById(R.id.edit_dish_name)).setText(name);
            ((EditText)findViewById(R.id.edit_dish_regular_price)).setText(reg_price);
            ((EditText)findViewById(R.id.edit_dish_large_price)).setText(large_price);
            ((EditText)findViewById(R.id.edit_dish_regular_price_incurred)).setText(reg_price_incurred);
            ((EditText)findViewById(R.id.edit_dish_large_price_incurred)).setText(large_price_incurred);
            ((EditText)findViewById(R.id.edit_dish_time)).setText(time);
            ((EditText)findViewById(R.id.edit_dish_description)).setText(description);
            ((Spinner)findViewById(R.id.edit_dish_spinner)).setSelection(((ArrayAdapter<String>)((Spinner)findViewById(R.id.edit_dish_spinner)).getAdapter()).getPosition(type));

            if(!picture.equals("https://firebasestorage.googleapis.com/v0/b/aroms-9adaf.appspot.com/o/noimage.jpg?alt=media&token=8231970c-160b-4fc7-98f2-c404aab5c647")){
                ((ProgressBar) findViewById(R.id.edit_dish_progressBar)).setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(picture)
                        .into((ImageView) findViewById(R.id.edit_dish_imageView));
                ((ProgressBar) findViewById(R.id.edit_dish_progressBar)).setVisibility(View.INVISIBLE);
            }

            FirebaseApp.initializeApp(this);
            menuDB = FirebaseDatabase.getInstance().getReference("Menu/"+uid);
            ingredientDB = FirebaseDatabase.getInstance().getReference("Ingredients/"+uid);
            (findViewById(R.id.edit_dish_delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onClickBtnDelete();
                    showPopup();
                }
            });
        }
        //image
        mButtonChooseImage = findViewById(R.id.edit_dish_add_image_btn);
        mImageView = findViewById(R.id.edit_dish_imageView);

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("dish_uploads");

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

    private void uploadFile(final String uid,String name,String pic) {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(uid+"/"+name);

            if (!pic.equals("https://firebasestorage.googleapis.com/v0/b/aroms-9adaf.appspot.com/o/noimage.jpg?alt=media&token=8231970c-160b-4fc7-98f2-c404aab5c647")){
                mStorageRef.child(uid).delete();
            }

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    menuDB.child("picture").setValue(uri.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(edit_dish.this, "Picture upload unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    })
                   ;
        } else {
//            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
              //menuDB.child("picture").setValue("https://firebasestorage.googleapis.com/v0/b/aroms-9adaf.appspot.com/o/noimage.jpg?alt=media&token=8231970c-160b-4fc7-98f2-c404aab5c647");
        }
    }


    public void onClickBtn(View v) {
        ((ProgressBar) findViewById(R.id.edit_dish_progressBar)).setVisibility(View.VISIBLE);
        EditText a = (EditText) findViewById(R.id.edit_dish_name);
        String name = a.getText().toString();

        EditText b = (EditText) findViewById(R.id.edit_dish_regular_price);
        String reg_price = b.getText().toString();

        EditText c = (EditText) findViewById(R.id.edit_dish_large_price);
        String large_price = c.getText().toString();

        EditText d = (EditText) findViewById(R.id.edit_dish_time);
        String time = d.getText().toString();

        EditText e = (EditText) findViewById(R.id.edit_dish_regular_price_incurred);
        String reg_price_incurred = e.getText().toString();

        EditText f = (EditText) findViewById(R.id.edit_dish_large_price_incurred);
        String large_price_incurred = f.getText().toString();

        EditText g = (EditText) findViewById(R.id.edit_dish_description);
        String description = g.getText().toString();

        String type = ((Spinner) findViewById(R.id.edit_dish_spinner)).getSelectedItem().toString();

        if(!name.isEmpty()&&!reg_price.isEmpty()&&!large_price.isEmpty()&&!time.isEmpty()&&!type.equals("Select dish type")&&!reg_price_incurred.isEmpty()&&!large_price_incurred.isEmpty()&&!description.isEmpty()){

            menuDB.child("name").setValue(name);
            menuDB.child("reg_price").setValue(reg_price);
            menuDB.child("large_price").setValue(large_price);
            menuDB.child("time").setValue(time);
            menuDB.child("type").setValue(type);
            menuDB.child("reg_price_incurred").setValue(reg_price_incurred);
            menuDB.child("large_price_incurred").setValue(large_price_incurred);

            uploadFile(uid,name,picture);

            Toast.makeText(this,"Dish updated successfully",Toast.LENGTH_LONG).show();
            ((ProgressBar) findViewById(R.id.edit_dish_progressBar)).setVisibility(View.INVISIBLE);
            finish();
            Intent i = new Intent(getApplicationContext(),add_dish_ingredient.class);
            i.putExtra("uid",uid);
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


    private void showPopup(){
        TextView close;
        Button accept;
        Button reject;


        mDialog = new Dialog(edit_dish.this);
        mDialog.setContentView(R.layout.notification_popup);
        close = mDialog.findViewById(R.id.txtClose);
        accept = mDialog.findViewById(R.id.btnAccept);
        reject = mDialog.findViewById(R.id.btnReject);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnDelete();
                mDialog.dismiss();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.show();
    }


    public void onClickBtnDelete(){
        menuDB.removeValue();
        ingredientDB.removeValue();
        mStorageRef.child(uid).delete();
        Toast.makeText(this,"Dish deleted successfully",Toast.LENGTH_LONG).show();
        finish();
        Intent i = new Intent(getApplicationContext(),menu.class);
        startActivity(i);
    }

}
