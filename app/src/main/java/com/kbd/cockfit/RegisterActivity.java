package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ConstraintLayout con;
    private ImageView back;

    private EditText editText_email, editText_pwd, editText_checkPwd, editText_nickname;
    private Button button_checkId, button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        con = findViewById(R.id.register_layout_const);
        back = findViewById(R.id.register_button_backButton);

        final InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        back.setOnClickListener(new View.OnClickListener() {
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

        editText_email = findViewById(R.id.register_editText_email);
        editText_pwd = findViewById(R.id.register_editText_pw);
        editText_checkPwd = findViewById(R.id.register_editText_pw2);
        editText_nickname = findViewById(R.id.register_editText_nic);

        button_checkId = findViewById(R.id.register_button_checkId);
        button_register = findViewById(R.id.register_button_register);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickButton(View view) {
        if(view.getId() == this.button_register.getId()) {
            String e_mail = this.editText_email.getText().toString();
            String pwd = this.editText_pwd.getText().toString();
            String checkPwd = this.editText_checkPwd.getText().toString();
            String nickname = this.editText_nickname.getText().toString();

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

                        //유저의 프로필 닉네임을 업데이트 합니다.
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nickname)
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

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "이메일 등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
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