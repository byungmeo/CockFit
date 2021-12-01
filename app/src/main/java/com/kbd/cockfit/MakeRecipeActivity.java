package com.kbd.cockfit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRecipeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private ImageView imageView_addImage;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private StorageReference mStorage;
    private Uri imageUrl;
    private boolean imageOn;
    private Toolbar appBar;
    private String imageKey;
    private DatabaseReference forSnapshot;
    private Context context;
    private String currentPhotoPath;

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

    private ProgressBar progressBar;
    private ScrollView scrollView;

    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);
        context = this;

        editText_name = findViewById(R.id.make_editText_name);
        editText_proof = findViewById(R.id.make_editText_proof);
        editText_base = findViewById(R.id.make_editText_base);
        editText_tags = findViewById(R.id.make_editText_tags);
        editText_equipment = findViewById(R.id.make_editText_equipment);
        editText_ingredient = findViewById(R.id.make_editText_ingredient);
        editText_description = findViewById(R.id.make_editText_description);
        progressBar = findViewById(R.id.make_progressBar);
        scrollView = findViewById(R.id.scrollView3);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");

        //getIntent
        editRecipeId = getIntent().getStringExtra("recipeId");
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if(isEdit) {
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);

            editRecipe = getIntent().getParcelableExtra("recipe");

            editText_name.getEditText().setText(editRecipe.getName());
            List<String> list = editRecipe.getTags();

            String totalText = "";
            for (String tag : list) {
                totalText += tag + ", ";
            }
            totalText = totalText.substring(0, totalText.length() - 2);
            editText_tags.getEditText().setText(totalText);
            editText_proof.getEditText().setText(editRecipe.getProof());
            editText_base.getEditText().setText(editRecipe.getBase());

            list = editRecipe.getIngredient();
            totalText = "";
            for (String ingredient : list) {
                totalText += ingredient + ", ";
            }
            totalText = totalText.substring(0, totalText.length() - 2);
            editText_ingredient.getEditText().setText(totalText);

            list = editRecipe.getEquipment();
            totalText = "";
            for (String equipment : list) {
                totalText += equipment + ", ";
            }
            totalText = totalText.substring(0, totalText.length() - 2);
            editText_equipment.getEditText().setText(totalText);
            editText_description.getEditText().setText(editRecipe.getDescription());

            mStorage.child("Users/"+uid+"/CocktailImage/"+editRecipeId+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MakeRecipeActivity.this)
                            .load(uri)
                            .into(imageView_addImage);
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            });
        }

        imageView_addImage = findViewById(R.id.make_imageView_addImage);
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

                    if(imageOn) {
                        uploadImageToFirebase(imageUrl);
                    } else {
                        onBackPressed();
                    }
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

        forSnapshot = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        forSnapshot.child("user").child(uid).child("MyRecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot recipeSnapshot: snapshot.getChildren()) {
                    imageKey = recipeSnapshot.getKey();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyRecipeLoad", "loadPost:onCancelled", error.toException());
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void storeRecipe(){
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
        if(imageOn==false){
            Toast.makeText(this , "사진을 넣어주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        MyRecipe recipe = new MyRecipe(0, name, proof, base, ingredient, equipment, description, tags);
        recipe.setUid(uid);


        mDatabase.child("user").child(uid).child("MyRecipe").push().setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                uploadImageToFirebase(imageUrl);
            }
        });
    }


    private void uploadImageToFirebase(Uri file){
        StorageReference fileRef = mStorage.child("Users/"+uid+"/CocktailImage/"+imageKey+".jpg");
        fileRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_FROM_ALBUM :
            {
                if(resultCode == Activity.RESULT_OK){
                    imageUrl = data.getData();
                    Glide.with(MakeRecipeActivity.this)
                            .load(imageUrl)
                            .into(imageView_addImage);
                }
                break;
            }
            case PICK_FROM_CAMERA:
            {
                if(resultCode == Activity.RESULT_OK){
                    //bitmap으로 받은 result를 uri로 바꿔줌
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), imageBitmap, "Title", null);
                    imageUrl = Uri.parse(path);

                    Glide.with(MakeRecipeActivity.this)
                            .load(imageUrl)
                            .into(imageView_addImage);
                }
                break;
            }
            default:
                break;
        }

    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 사용이 승인되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                    imageOn = true;
                } else {
                    Toast.makeText(this, "카메라 사용이 승인되지 않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.make_imageView_addImage) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

            alertdialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            alertdialog.setNegativeButton("앨범선택", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                    imageOn = true;
                }
            });

            alertdialog.setPositiveButton("사진촬영", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int permissionCheck = ContextCompat.checkSelfPermission(MakeRecipeActivity.this,Manifest.permission.CAMERA);

                    if(permissionCheck == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(MakeRecipeActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(MakeRecipeActivity.this,
                                    "com.kbd.cockfit",
                                    photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                            imageOn = true;
                        }
                        //startActivityForResult(intent, PICK_FROM_CAMERA);
                        //imageOn = true;
                    }
                }
            });
            AlertDialog alert = alertdialog.create();
            alert.setMessage("사진 업로드 방식을 선택해주세요.");
            alert.show();
        }
    }
}

