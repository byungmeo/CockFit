package com.kbd.cockfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout editText_email; //id 입력창
    private TextInputLayout editText_pwd; //pw 입력창
    private Button button_login; //로그인 버튼
    private Button button_register; //회원가입 버튼
    private FirebaseUser user;

    private GoogleSignInClient googleSignInClient;
    private SignInButton btn_google; //구글 로그인 버튼
    private FirebaseAuth auth; //Firebase 인증객체
    private GoogleApiClient googleApiClient; //구글 API 클라이언트
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //구글 로그인 인증을 요청했을 때 결과값을 되돌려받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount(); //구글로그인 정보를 담고있는 객체 (닉네임, 프로필사진, 이메일주소 등)
                resultLogIn(account); //로그인 결과값을 출력하라는 메소드
            }
        }
    }

    private void resultLogIn(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) { //로그인 성공시
                            Toast.makeText(LoginActivity.this, "LogIn Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            //사용자의 구글아이디 정보를 가져오는 코드
                            intent.putExtra("nickName", String.valueOf(account.getDisplayName()));
                            intent.putExtra("photoUrl", String.valueOf(account.getPhotoUrl()));

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();

                            mDatabase.child("user").child(uid).child("info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        if(task.getResult().getValue() == null) {
                                            long now = System.currentTimeMillis();
                                            Date date = new Date(now);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            String userRegisterDate = dateFormat.format(date);
                                            mDatabase.child("user").child(uid).child("info").child("register_data").setValue(userRegisterDate); //실시간 파이어베이스에 가입일자 저장
                                        }
                                    }
                                }
                            });
                            startActivity(intent);
                        }
                        else { //로그인 실패시
                            Toast.makeText(LoginActivity.this, "LogIn Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("997586484780-qabp69u123fmvu564ti21js98c2d4i72.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        auth=FirebaseAuth.getInstance();//firebase 초기화

        btn_google=findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                startActivityForResult(intent,REQ_SIGN_GOOGLE);
            }
        });
        
        

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


        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
            try {
                GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personGivenName = acct.getGivenName();
                    String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();

                    Log.d("TAG", "handleSignInResult:personName "+personName);
                    Log.d("TAG", "handleSignInResult:personGivenName "+personGivenName);
                    Log.d("TAG", "handleSignInResult:personEmail "+personEmail);
                    Log.d("TAG", "handleSignInResult:personId "+personId);
                    Log.d("TAG", "handleSignInResult:personFamilyName "+personFamilyName);
                    Log.d("TAG", "handleSignInResult:personPhoto "+personPhoto);
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());

            }
        }

    }