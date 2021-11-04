package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
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
                        Toast.makeText(RegisterActivity.this, "당신의 아이디는" + user.getEmail() + "입니다", Toast.LENGTH_SHORT).show();
                        user.sendEmailVerification();
                        DataSet(true, editText_nickname.getText().toString());
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

    public void DataSet(Boolean firstLaunch, String nickname) {
        FirebaseUser user = mAuth.getCurrentUser(); //현재 로그인한 사람의 정보를 가져옴
        final String uid = user.getUid(); //uid는 계정 하나당 하나의 값을 가짐

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 변경이 감지가 되면 이 함수가 자동으로 콜백이 됩니다 이때 dataSnapashot 는 값을 내려 받을떄 사용함으로 지금은 쓰지 않습니
                mDatabase.child("user").child(uid).child("name").setValue(nickname);
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