package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout editText_email; //id 입력창
    private TextInputLayout editText_pwd; //pw 입력창
    private Button button_login; //로그인 버튼
    private Button button_register; //회원가입 버튼
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CockFit); //Splash 화면 출력 후 본래 테마로 전환
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editText_email = (TextInputLayout)findViewById(R.id.login_editText_id);
        editText_pwd = (TextInputLayout)findViewById(R.id.login_editText_pw);
        button_login = findViewById(R.id.login_button_login);
        button_register = findViewById(R.id.login_button_register);
        user = FirebaseAuth.getInstance().getCurrentUser();


        if(user != null){
            if(user.isEmailVerified()) {
                Toast.makeText(LoginActivity.this, user.getDisplayName() + "님 안녕하세요", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

        public void clickButton (View view){
            if (view.getId() == this.button_login.getId()) {
                String email = this.editText_email.getEditText().getText().toString();
                String password = this.editText_pwd.getEditText().getText().toString();

                //비어 있으면 다시 입력하라고 되돌림
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 계정이 등록이 되어 있으면
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) { // 그리고 그때 그 계정이 실제로 존재하는 계정인지
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "인증이 되지 않은 이메일입니다. 해당 이메일 주소에 전송된 인증 링크를 확인하세요.", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                return;
                            }
                        } else {
                            try {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {
                                    case "ERROR_INVALID_EMAIL" :
                                        Toast.makeText(LoginActivity.this, "이메일 입력이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ERROR_USER_NOT_FOUND" :
                                        Toast.makeText(LoginActivity.this, "사용자가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ERROR_WRONG_PASSWORD" :
                                        Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    default :
                                        Toast.makeText(LoginActivity.this, "관리자에게 문의해주세요.\n" + errorCode, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, "관리자에게 문의해주세요.\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else if(view.getId() == this.button_register.getId()) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }
    }