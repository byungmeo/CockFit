package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class DevelopActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText editText_email, editText_pwd;
    private Button button_login, button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        editText_email = findViewById(R.id.develop_editText_email);
        editText_pwd = findViewById(R.id.develop_editText_password);
        button_login = findViewById(R.id.develop_button_login);
        button_register = findViewById(R.id.develop_button_register);
    }

    public void clickButton(View view) {
        if(view.getId() == button_register.getId()) {
            String e_mail = this.editText_email.getText().toString();
            String pwd = this.editText_pwd.getText().toString();

            String e_mailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$"; // 이메일 형식 패턴
            if(!Pattern.matches(e_mailPattern , e_mail)){
                Toast.makeText(DevelopActivity.this , "이메일 형식을 확인해수제요" , Toast.LENGTH_LONG).show();
                return;
            }

            if(pwd.equals("")) {
                Toast.makeText(DevelopActivity.this , "비밀번호를 입력해주세요" , Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(e_mail, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DevelopActivity.this, "이메일 등록에 성공했습니다", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(DevelopActivity.this, "당신의 아이디는" + user.getEmail() + "입니다", Toast.LENGTH_SHORT).show();
                        user.sendEmailVerification();
                    } else {
                        Toast.makeText(DevelopActivity.this, "이메일 등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            HideKeyboard(view);
        } else if(view.getId() == button_login.getId()) {
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
                            Toast.makeText(DevelopActivity.this, "signInWithEmail:success." + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(DevelopActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(DevelopActivity.this, "인증이 되지 않은 이메일입니다 해당 이메일 주소에서 링크를 클릭해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else{
                        Log.d("login", "signInWithEmail:failure", task.getException());
                        Toast.makeText(DevelopActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void HideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText_email.getWindowToken(), 0);
    }

    public void DataSet() {
        FirebaseUser user = mAuth.getCurrentUser(); //현재 로그인한 사람의 정보를 가져옴
        final String uid = user.getUid(); //uid는 계정 하나당 하나의 값을 가짐

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 변경이 감지가 되면 이 함수가 자동으로 콜백이 됩니다 이때 dataSnapashot 는 값을 내려 받을떄 사용함으로 지금은 쓰지 않습니
                mDatabase.child("user").child(uid).child("nickname").setValue("");
                mDatabase.child("user").child(uid).child("firstLunch").setValue("");
                //RealTimeDB는 기본적으로 parent , child , value 값으로 이루어져 있습니다 지금은 최초로 로그인한 사람의
                //색인을 만들고자 지금과 같은 작업을 하는 중입니다 즉 처음 들어오는 사람에게 DB자리를 내준다고 생각하시면됩니다
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // RealTimeDB와 통신 에러 등등 데이터를 정상적으로 받지 못할때 콜백함수로서 이곳으로 들어옵니다
            }
        });
    }
}