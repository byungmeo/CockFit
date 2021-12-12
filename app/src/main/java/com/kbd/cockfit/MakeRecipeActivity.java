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
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.volokh.danylo.hashtaghelper.HashTagHelper;

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
    private final int MY_PERMISSIONS_REQUEST_CAMERA=2;
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
    private HashTagHelper mTextHashTagHelper;

    private TextInputLayout editText_name;
    private TextInputLayout editText_proof;
    private TextInputLayout editText_base;
    private TextInputLayout editText_tags;
    private TextInputLayout editText_equipment;
    private TextInputLayout editText_ingredient;
    private TextInputLayout editText_description;

    private ProgressBar progressBar;
    private ScrollView scrollView;

    private Long mLastClickTime = 0L;
    

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

        editText_ingredient.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    if (s.length() > 1) {
                        if (s.charAt(s.length() - 1) == ',') {
                            editText_ingredient.getEditText().removeTextChangedListener(this);
                            editText_ingredient.getEditText().append(" ");
                            editText_ingredient.getEditText().setSelection(s.length());
                            editText_ingredient.getEditText().addTextChangedListener(this);
                        } else if (s.charAt(s.length() - 1) == ' ') {
                            editText_ingredient.getEditText().removeTextChangedListener(this);
                            String strTemp = editText_ingredient.getEditText().getText().toString();
                            editText_ingredient.getEditText().setText(strTemp.substring(0, strTemp.length()-1));
                            editText_ingredient.getEditText().append(", ");
                            editText_ingredient.getEditText().setSelection(editText_ingredient.getEditText().getText().length());
                            editText_ingredient.getEditText().addTextChangedListener(this);
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        });



        editText_equipment.getEditText().addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count == 1) {
                    if (s.length() > 1) {
                        if (s.charAt(s.length() - 1) == ',') {
                            editText_equipment.getEditText().removeTextChangedListener(this);
                            editText_equipment.getEditText().append(" ");
                            editText_equipment.getEditText().setSelection(s.length());
                            editText_equipment.getEditText().addTextChangedListener(this);
                        } else if (s.charAt(s.length()-1) == ' '){
                            editText_equipment.getEditText().removeTextChangedListener(this);
                            String strTemp = editText_equipment.getEditText().getText().toString();
                            editText_equipment.getEditText().setText(strTemp.substring(0,strTemp.length()-1));
                            editText_equipment.getEditText().append(", ");
                            editText_equipment.getEditText().setSelection(editText_equipment.getEditText().getText().length());
                            editText_equipment.getEditText().addTextChangedListener(this);
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        editText_tags.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_tags.getEditText().getSelectionEnd() <= 1) {
                    editText_tags.getEditText().setCursorVisible(false);
                    editText_tags.getEditText().setSelection(1);
                    editText_tags.getEditText().setCursorVisible(true);
                }
            }
        });

        editText_tags.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(editText_tags.getEditText().getSelectionEnd() <= 1) {
                    editText_tags.getEditText().setCursorVisible(false);
                    editText_tags.getEditText().setSelection(1);
                    editText_tags.getEditText().setCursorVisible(true);
                }
            }
        });

        editText_tags.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 0) {
                    editText_tags.getEditText().append("#");
                }
                if(count == 1) {
                    if (s.length() > 0) {
                        if (s.charAt(s.length() - 1) == ' ') {
                            editText_tags.getEditText().append("#");
                            editText_tags.getEditText().setSelection(s.length());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });


        // 태그 생성
        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.CockFitMainColor1), null, null);
        mTextHashTagHelper.handle(editText_tags.getEditText());


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
                totalText += tag;
            }

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
                long currentClickTime = SystemClock.uptimeMillis();
                long elapsedTime = currentClickTime - mLastClickTime;
                mLastClickTime = currentClickTime;

                if(elapsedTime > 600) {
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
                return false;
            }
        });

        appBar.setNavigationOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
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


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void onCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.d("카메라 이미지 없음", "이미지 생성이 안됐네");
        }
        if (photoFile != null) {
            imageUrl= FileProvider.getUriForFile(MakeRecipeActivity.this, "com.kbd.cockfit", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
            startActivityForResult(intent, PICK_FROM_CAMERA);
            imageOn = true;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FROM_ALBUM)
                imageUrl = data.getData();
            Glide.with(MakeRecipeActivity.this)
                    .load(imageUrl)
                    .into(imageView_addImage);
        }
    }


    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 사용이 승인되었습니다.", Toast.LENGTH_LONG).show();
                    onCameraIntent();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MakeRecipeActivity.this, Manifest.permission.CAMERA)){
                        Toast.makeText(this, "권한 거부 시 직접 앱 설정을 해야합니다.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this, "카메라 사용이 승인되지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
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

                    if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                        onCameraIntent();
                    }
                    else{
                        Log.d("권한", "거부됐습니다");
                        ActivityCompat.requestPermissions(MakeRecipeActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            });
            AlertDialog alert = alertdialog.create();
            alert.setMessage("사진 업로드 방식을 선택해주세요.");
            alert.show();
        }
    }
}


