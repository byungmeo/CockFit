package com.kbd.cockfit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Uri file;
    private StorageReference storageRef;
    private String uid;
    private ConstraintLayout con;
    private ImageView button_back;
    private ImageView profile_image;
    private static final int PICK_FROM_ALBUM = 1;
    boolean imageOn;

    private TextInputLayout editText_email, editText_pwd, editText_checkPwd, editText_nickname;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        con = findViewById(R.id.register_layout_const);
        button_back = findViewById(R.id.register_button_backButton);
        imageOn = false;

        final InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.hideSoftInputFromWindow(con.getWindowToken(),0);
            }
        });

        editText_email = (TextInputLayout)findViewById(R.id.register_editText_email);
        editText_pwd = (TextInputLayout)findViewById(R.id.register_editText_pw);
        editText_checkPwd = (TextInputLayout)findViewById(R.id.register_editText_pw2);
        editText_nickname = (TextInputLayout)findViewById(R.id.register_editText_nic);
        button_register = findViewById(R.id.register_button_register);
        profile_image = findViewById(R.id.register_imageview_profileimage);
    }

    private void uploadImageToFirebase(Uri file){
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("Users/"+uid+"/ProfileImage.jpg");
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

    public void clickButton(View view) {
        if(view.getId() == this.profile_image.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_FROM_ALBUM);
            imageOn=true;
        }
        if(view.getId() == this.button_register.getId()) {
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

            mAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "이메일 등록에 성공했습니다", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification(); //인증 이메일을 전송합니다.
                        uid = user.getUid();
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
                        Toast.makeText(RegisterActivity.this, "이메일 등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        Log.d("test", task.getException().getMessage());
                        HideKeyboard(view);
                    }
                }
            });
        }
    }

    public void HideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
    }
}