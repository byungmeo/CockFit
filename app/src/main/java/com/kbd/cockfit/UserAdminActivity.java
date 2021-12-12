package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.regex.Pattern;

public class UserAdminActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private View userAdminView;
    private EditText editText_changeEmail;
    private EditText editText_changePw;
    private EditText editText_checkPw;
    private Button button_cancel;
    private Button button_ok;
    private FirebaseUser user;
    private Toolbar userAdminToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userAdminToolbar = findViewById(R.id.userAdmin_materialToolbar);
        setSupportActionBar(userAdminToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        userAdminToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAdminActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void clickButton(View view) {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());


        if(view.getId() == R.id.user_button_changeEmail) {
            userAdminView = inflater.inflate(R.layout.changeemail, null);

            editText_changeEmail = userAdminView.findViewById(R.id.changeEmail_editText_changeEmail);
            button_cancel = userAdminView.findViewById(R.id.changeEmail_button_cancel);
            button_ok = userAdminView.findViewById(R.id.changeEmail_button_ok);

            builder.setView(userAdminView);
            dialog = builder.create();
            dialog.show();

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String changeEmail = editText_changeEmail.getText().toString();
                    String e_mailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$"; // 이메일 형식 패턴

                    if(!Pattern.matches(e_mailPattern , changeEmail)){
                        Toast.makeText(view.getContext() , "이메일 형식을 확인해주세요" , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        user.updateEmail(changeEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "이메일이 변경되었습니다", Toast.LENGTH_LONG).show();
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.sendEmailVerification();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                }
            });
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else if(view.getId() == R.id.user_button_changePw) {
            userAdminView = inflater.inflate(R.layout.changepw, null);

            editText_changePw = userAdminView.findViewById(R.id.changePw_editText_changePw);
            editText_checkPw = userAdminView.findViewById(R.id.changePw_editText_checkPw);
            button_cancel = userAdminView.findViewById(R.id.changePw_button_cancel);
            button_ok = userAdminView.findViewById(R.id.changePw_button_ok);

            builder.setView(userAdminView);
            dialog = builder.create();
            dialog.show();

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String changePw = editText_changePw.getText().toString();
                    String checkPw = editText_checkPw.getText().toString();

                    if(!changePw.equals(checkPw)) {
                        Toast.makeText(view.getContext(), "비밀번호를 다시 한번 확인해주세요", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        user.updatePassword(changePw)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user = FirebaseAuth.getInstance().getCurrentUser();
                                            FirebaseAuth mAuth = FirebaseAuth.getInstance();

                                            mAuth.sendPasswordResetEmail(user.getEmail())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                            Toast.makeText(view.getContext(), "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                }
            });
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else if(view.getId() == R.id.user_button_leave) {


            mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference(); //실시간 Firebase 경로
            user = FirebaseAuth.getInstance().getCurrentUser();
            String uId = user.getUid();
            storage = FirebaseStorage.getInstance();



            //Storage에서 프로필 사진 삭제
            storage.getReference().child("Users").child(uId).child("profileImage.jpg").delete().addOnSuccessListener(new OnSuccessListener<Object>() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(view.getContext(), "프로필사진 삭제 완료", Toast.LENGTH_LONG).show();
                }
            });


            //커뮤니티 게시글, 즐겨찾기한 공유레시피, 즐겨찾기한 기본레시피, 사용자정보(가입일자) 순서대로 삭제 후 DB삭제 완료 메시지 띄움
            mDatabase.child("user").child(uId).child("community").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mDatabase.child("user").child(uId).child("bookmarkedPost").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mDatabase.child("user").child(uId).child("favorite").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mDatabase.child("user").child(uId).child("info").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "DB삭제 완료", Toast.LENGTH_LONG).show();
                                            //onBackPressed();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });


            //Firebase Authentication에서 계정 제거
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                //mDatabase.child("user").child(uId).removeValue(); //실시간 Firebase 데이터 삭제
                                Toast.makeText(view.getContext(), "계정이 탈퇴 되었습니다", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                view.getContext().startActivity(intent);
                            }
                        }
                    });
        }
    }
}