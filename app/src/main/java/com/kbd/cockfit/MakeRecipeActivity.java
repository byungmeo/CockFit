package com.kbd.cockfit;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRecipeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String[] imagePath;
    private String uid;
    private ImageView imageView_addImage;
    private static final int PICK_FROM_ALBUM =1;
    private StorageReference storageRef;
    private Uri file;
    private boolean imageOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageView_addImage = findViewById(R.id.make_imageView_addImage);
        storageRef = FirebaseStorage.getInstance().getReference();

    }

    public void onStart(){
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImageToFirebase(Uri file){
        imagePath = file.toString().split("/");
        StorageReference fileRef = storageRef.child("Users/"+uid+"/CocktailImage/"+imagePath[imagePath.length-1]);
        fileRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(uri).into(imageView_addImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(PICK_FROM_ALBUM == 1){
            if(resultCode == Activity.RESULT_OK){
                file = data.getData();
                Picasso.get().load(file).into(imageView_addImage);
            }
        }
    }



    public void clickButton(View view) {

        if(view.getId() == R.id.make_imageView_addImage){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_ALBUM);
            imageOn=true;
        }

        if(view.getId() == R.id.make_button_backButton) {
            this.onBackPressed();
        }
        else if(view.getId() == R.id.make_button_store) {
            String name = ((EditText) findViewById(R.id.make_editText_name)).getText().toString();
            String proof = ((EditText) findViewById(R.id.make_editText_proof)).getText().toString();
            String base = ((EditText) findViewById(R.id.make_editText_base)).getText().toString();
            String[] ingredient = ((EditText) findViewById(R.id.make_editText_ingredient)).getText().toString().split(", ");
            String[] equipment = ((EditText) findViewById(R.id.make_editText_equipment)).getText().toString().split(", ");
            String description = ((EditText) findViewById(R.id.make_editText_description)).getText().toString();
            String[] tags = ((EditText) findViewById(R.id.make_editText_tags)).getText().toString().split(", ");

            if(name.equals("") || proof.equals("") || base.equals("") || ingredient.equals("") || equipment.equals("") || description.equals("") || tags.equals("")) {
                Toast.makeText(this , "모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            Recipe recipe = new Recipe(0, name,proof,base,ingredient,equipment,description,tags);

            mDatabase.child("user").child(uid).child("MyRecipe").push().setValue(recipe);
            if(imageOn) {
                uploadImageToFirebase(file);
            }
            this.onBackPressed();
        }
    }
}

