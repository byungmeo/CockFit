package com.kbd.cockfit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Uri file;
    private StorageReference storageRef;
    private String uid;
    private ConstraintLayout con;
    private ImageView profile_image;
    private static final int PICK_FROM_ALBUM = 1;
    boolean imageOn;

    private Toolbar toolbar;
    private Long mLastClickTime = 0L;

    private TextInputLayout editText_email, editText_pwd, editText_checkPwd, editText_nickname;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {
            mAuth = FirebaseAuth.getInstance();
            con = findViewById(R.id.register_layout_const);
            imageOn = false;

            final InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.hideSoftInputFromWindow(con.getWindowToken(), 0);
                }
            });

            toolbar = findViewById(R.id.register_materialToolbar);
            setSupportActionBar(toolbar);


            editText_email = (TextInputLayout) findViewById(R.id.register_editText_email);
            editText_pwd = (TextInputLayout) findViewById(R.id.register_editText_pw);
            editText_checkPwd = (TextInputLayout) findViewById(R.id.register_editText_pw2);
            editText_nickname = (TextInputLayout) findViewById(R.id.register_editText_nic);
            profile_image = findViewById(R.id.register_imageview_profileimage);
            button_register = findViewById(R.id.register_button_register);

            button_register.setOnClickListener(new UtilitySet.OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    doRegister();
                }
            });
        } catch (Exception e1) {
            Toast.makeText(this, "onCreate : " + e1.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri file){
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("Users/"+uid+"/profileImage.jpg");
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
                Picasso.get().load(file).into(profile_image);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.top_app_bar_register, menu);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            return super.onCreateOptionsMenu(menu);
        } catch (Exception e1) {
            Toast.makeText(this, "옵션메뉴생성 : " + e1.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapsedTime > 600) {
            if(item.getItemId() == R.id.register_icon) {
                doRegister();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void doRegister() {
        String e_mail = this.editText_email.getEditText().getText().toString();
        String pwd = this.editText_pwd.getEditText().getText().toString();
        String checkPwd = this.editText_checkPwd.getEditText().getText().toString();
        String nickname = this.editText_nickname.getEditText().getText().toString();

        String e_mailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$"; // 이메일 형식 패턴
        if(!Pattern.matches(e_mailPattern , e_mail)){
            Toast.makeText(RegisterActivity.this , "이메일 형식을 확인해주세요" , Toast.LENGTH_LONG).show();
            return;
        }

        if(pwd.equals("") || checkPwd.equals("")) {
            Toast.makeText(RegisterActivity.this , "비밀번호를 입력해주세요" , Toast.LENGTH_LONG).show();
            return;
        }

        if(nickname.equals("")) {
            Toast.makeText(RegisterActivity.this , "닉네임을 입력해주세요" , Toast.LENGTH_LONG).show();
            return;
        }

        if(!pwd.equals(checkPwd)) {
            Toast.makeText(RegisterActivity.this , "비밀번호를 다시 확인해주세요" , Toast.LENGTH_LONG).show();
            return;
        }

        if(pwd.length() < 6) {
            Toast.makeText(RegisterActivity.this , "비밀번호는 최소 6자리 이상으로 입력해주세요." , Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();

                    user.sendEmailVerification(); //인증 이메일을 전송합니다.

                    Toast.makeText(RegisterActivity.this, "이메일 등록에 성공했습니다", Toast.LENGTH_SHORT).show();

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String userRegisterDate = dateFormat.format(date);

                    mDatabase.child("user").child(uid).child("info").child("register_data").setValue(userRegisterDate);

                    //유저의 프로필 닉네임을 업데이트 합니다.
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .setPhotoUri(file)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("test", "유저 프로필 업데이트 완료");
                                    }
                                }
                            });
                    if(imageOn) {
                        uploadImageToFirebase(file);
                    }
                    onBackPressed(); //로그인 화면으로 돌아갑니다.
                } else {
                    try {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        switch (errorCode) {
                            case "ERROR_INVALID_EMAIL" :
                                Toast.makeText(RegisterActivity.this, "잘못된 형식의 이메일입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "ERROR_EMAIL_ALREADY_IN_USE" :
                                Toast.makeText(RegisterActivity.this, "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT).show();
                                break;
                            case "ERROR_WEAK_PASSWORD" :
                                Toast.makeText(RegisterActivity.this, "취약한 비밀번호입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                                break;
                            default :
                                Toast.makeText(RegisterActivity.this, "관리자에게 문의해주세요.\n" + errorCode, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception e){
                        Toast.makeText(RegisterActivity.this, "관리자에게 문의해주세요.\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    HideKeyboard();
                }
            }
        });
    }


    public void clickButton(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapsedTime > 600) {
            if(view.getId() == this.profile_image.getId()){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                imageOn=true;
            }
        }
    }

    public void HideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
    }
}