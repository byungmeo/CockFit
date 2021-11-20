package com.kbd.cockfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
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
    private boolean imageOn;
    private Toolbar appBar;

    private MyRecipe editRecipe;
    private String editRecipeId;
    private boolean isEdit;

    private TextInputLayout editText_name;
    private TextInputLayout editText_proof;
    private TextInputLayout editText_base;
    private TextInputLayout editText_tags;
    private TextInputLayout editText_equipment;
    private TextInputLayout editText_ingredient;
    private TextInputLayout editText_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        editText_name = findViewById(R.id.make_editText_name);
        editText_proof = findViewById(R.id.make_editText_proof);
        editText_base = findViewById(R.id.make_editText_base);
        editText_tags = findViewById(R.id.make_editText_ingredient);
        editText_equipment = findViewById(R.id.make_editText_equipment);
        editText_ingredient = findViewById(R.id.make_editText_tags);
        editText_description = findViewById(R.id.make_editText_description);
        /*String name = ((TextInputLayout) findViewById(R.id.make_editText_name)).getEditText().getText().toString();
        String proof = ((TextInputLayout) findViewById(R.id.make_editText_proof)).getEditText().getText().toString();
        String base = ((TextInputLayout) findViewById(R.id.make_editText_base)).getEditText().getText().toString();
        String[] ingredient = ((TextInputLayout) findViewById(R.id.make_editText_ingredient)).getEditText().getText().toString().split(", ");
        String[] equipment = ((TextInputLayout) findViewById(R.id.make_editText_equipment)).getEditText().getText().toString().split(", ");
        String[] tags = ((TextInputLayout) findViewById(R.id.make_editText_tags)).getEditText().getText().toString().split(", ");
        String description = ((TextInputLayout) findViewById(R.id.make_editText_description)).getEditText().getText().toString();*/
        /*editText_name = (EditText);
        editText_proof.setText(" ");
        editText_base.setText(" ");
        editText_tags.setText(" ");
        editText_equipment.setText(" ");
        editText_ingredient.setText(" ");
        editText_description.setText(" ");*/


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //getIntent
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if(isEdit) {
            Log.d("test", getIntent().getParcelableExtra("recipe").getClass().toString());
            editRecipe = getIntent().getParcelableExtra("recipe");

            Log.d("test", editRecipe.getName());

            editText_name.getEditText().setText(editRecipe.getName());
            List<String> list = editRecipe.getTags();

            String totalText = "";
            for (String tag : list) {
                totalText += tag + ", ";
            }
            editText_tags.getEditText().setText(totalText);
            editText_proof.getEditText().setText(editRecipe.getProof());
            editText_base.getEditText().setText(editRecipe.getBase());

            list = editRecipe.getIngredient();
            totalText = "";
            for (String ingredient : list) {
                totalText += ingredient + ", ";
            }
            editText_ingredient.getEditText().setText(totalText);

            list = editRecipe.getEquipment();
            totalText = "";
            for (String equipment : list) {
                totalText += equipment + ", ";
            }
            editText_equipment.getEditText().setText(totalText);
            editText_description.getEditText().setText(editRecipe.getDescription());
        }
        editRecipeId = getIntent().getStringExtra("recipeId");

        imageView_addImage = findViewById(R.id.make_imageView_addImage);
        storageRef = FirebaseStorage.getInstance().getReference();
        imageOn=false;
        appBar = findViewById(R.id.topAppBar);

        appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(isEdit) {
                    String name = editText_name.getEditText().getText().toString();
                    String proof = editText_proof.getEditText().getText().toString();
                    String base = editText_base.getEditText().getText().toString();
                    String[] ingredient = editText_ingredient.getEditText().getText().toString().split(", ");
                    String[] equipment = editText_equipment.getEditText().getText().toString().split(", ");
                    String[] tags = editText_tags.getEditText().getText().toString().split(", ");
                    String description = editText_description.getEditText().getText().toString();

                    if(name.equals("") || proof.equals("") || base.equals("") || ingredient.equals("") || equipment.equals("") || description.equals("") || tags.equals("")) {

                        Toast.makeText(MakeRecipeActivity.this, "모든 텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    MyRecipe recipe = new MyRecipe(0, name,proof,base,ingredient,equipment,description,tags);
                    recipe.setUid(uid);


                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(editRecipeId, recipe);
                    mDatabase.child("user").child(uid).child("MyRecipe").updateChildren(childUpdates);

                    onBackPressed();

                }
                else {
                    storeRecipe();
                }
                return false;
            }
        });

        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeRecipeActivity.this.onBackPressed();
            }
        });

    }

    public void storeRecipe(){
        /*String name = ((TextInputLayout) findViewById(R.id.make_editText_name)).getEditText().getText().toString();
        String proof = ((TextInputLayout) findViewById(R.id.make_editText_proof)).getEditText().getText().toString();
        String base = ((TextInputLayout) findViewById(R.id.make_editText_base)).getEditText().getText().toString();
        String[] ingredient = ((TextInputLayout) findViewById(R.id.make_editText_ingredient)).getEditText().getText().toString().split(", ");
        String[] equipment = ((TextInputLayout) findViewById(R.id.make_editText_equipment)).getEditText().getText().toString().split(", ");
        String[] tags = ((TextInputLayout) findViewById(R.id.make_editText_tags)).getEditText().getText().toString().split(", ");
        String description = ((TextInputLayout) findViewById(R.id.make_editText_description)).getEditText().getText().toString();*/


        String name = editText_name.getEditText().getText().toString();
        String proof = editText_proof.getEditText().getText().toString();
        String base = editText_base.getEditText().getText().toString();
        String[] ingredient = editText_ingredient.getEditText().getText().toString().split(", ");
        String[] equipment = editText_equipment.getEditText().getText().toString().split(", ");
        String[] tags = editText_tags.getEditText().getText().toString().split(", ");
        String description = editText_description.getEditText().getText().toString();

        if(name.equals("") || proof.equals("") || base.equals("") || ingredient.equals("") || equipment.equals("") || description.equals("") || tags.equals("")) {
            Toast.makeText(this , "모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        MyRecipe recipe = new MyRecipe(0, name,proof,base,ingredient,equipment,description,tags);
        recipe.setUid(uid);

        mDatabase.child("user").child(uid).child("MyRecipe").push().setValue(recipe);
        if(imageOn) {
            uploadImageToFirebase(file);
        }
        this.onBackPressed();
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
    }
}

