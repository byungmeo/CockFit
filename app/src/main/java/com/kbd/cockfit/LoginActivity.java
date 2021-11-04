package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText editText_email; //id 입력창
    private EditText editText_pwd; //pw 입력창
    private Button button_login; //로그인 버튼
    private Button button_register; //회원가입 버튼
    private Button button_develop; //개발자용 임시 버튼
    private Button button_firebase; //파이어베이스 테스트용 임시 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CockFit); //Splash 화면 출력 후 본래 테마로 전환
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.login_editText_id);
        editText_pwd = findViewById(R.id.login_editText_pw);
        button_login = findViewById(R.id.login_button_login);
        button_register =findViewById(R.id.login_button_register);
        button_develop = findViewById(R.id.login_button_developButton);
        button_firebase = findViewById(R.id.login_button_firebaseTest);

        //회원가입 버튼에 리스너를 부착합니다.
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //개발자용 임시 버튼에 리스너를 부착합니다.
        //로그인 로직을 건너뜁니다.
        button_develop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DevelopActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickButton(View view) {
        if(view.getId() == this.button_login.getId()) {
            String email = this.editText_email.getText().toString();
            String password = this.editText_pwd.getText().toString();

            //비어 있으면 다시 입력하라고 되돌림
            if(email.equals("") || password.equals("")){
                Toast.makeText(this , "아이디 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                return;
            }

            //로그인 로직 시작
            Log.d("login" , "start_e_mail_login");

            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){ // 계정이 등록이 되어 있으면
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user.isEmailVerified()){ // 그리고 그때 그 계정이 실제로 존재하는 계정인지
                            Log.d("login", "signInWithEmail:success" + user.getEmail());
                            Toast.makeText(LoginActivity.this, "signInWithEmail:success." + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "인증이 되지 않은 이메일입니다 해당 이메일 주소에서 링크를 클릭해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else{
                        Log.d("login", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}