package com.kbd.cockfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MakeRecipeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private ImageView imageView_addImage;
    private static final int PICK_FROM_ALBUM =1;
    private StorageReference storageRef;
    private Uri file;
    private boolean imageOn;
    private Toolbar appBar;
    private String imageKey;
    private DatabaseReference forSnapshot;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);
        context = this;

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        imageView_addImage = findViewById(R.id.make_imageView_addImage);
        storageRef = FirebaseStorage.getInstance().getReference();
        imageOn=false;
        appBar = findViewById(R.id.topAppBar);

        appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                storeRecipe();
                return false;
            }
        });

        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeRecipeActivity.this.onBackPressed();
            }
        });

        forSnapshot = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        forSnapshot.child("user").child(uid).child("MyRecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot recipeSnapshot: snapshot.getChildren()) {
                    imageKey = recipeSnapshot.getKey();
                }
                Log.d("test2", imageKey+"테스트입니다");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyRecipeLoad", "loadPost:onCancelled", error.toException());
            }
        });

    }


    public void storeRecipe(){
        String name = ((TextInputLayout) findViewById(R.id.make_editText_name)).getEditText().getText().toString();
        String proof = ((TextInputLayout) findViewById(R.id.make_editText_proof)).getEditText().getText().toString();
        String base = ((TextInputLayout) findViewById(R.id.make_editText_base)).getEditText().getText().toString();
        String[] ingredient = ((TextInputLayout) findViewById(R.id.make_editText_ingredient)).getEditText().getText().toString().split(", ");
        String[] equipment = ((TextInputLayout) findViewById(R.id.make_editText_equipment)).getEditText().getText().toString().split(", ");
        String[] tags = ((TextInputLayout) findViewById(R.id.make_editText_tags)).getEditText().getText().toString().split(", ");
        String description = ((TextInputLayout) findViewById(R.id.make_editText_description)).getEditText().getText().toString();

        if(name.equals("") || proof.equals("") || base.equals("") || ingredient.equals("") || equipment.equals("") || description.equals("") || tags.equals("")) {
            Toast.makeText(this , "모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(imageOn==false){
            Toast.makeText(this , "사진을 넣어주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        MyRecipe recipe = new MyRecipe(0, name, proof, base, ingredient, equipment, description, tags);
        recipe.setUid(uid);


        mDatabase.child("user").child(uid).child("MyRecipe").push().setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                uploadImageToFirebase(file);
            }
        });

        this.onBackPressed();
    }


    private void uploadImageToFirebase(Uri file){
        Log.d("test3", imageKey+"업로드 전");
        StorageReference fileRef = storageRef.child("Users/"+uid+"/CocktailImage/"+imageKey+".jpg");
        fileRef.putFile(file);
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

