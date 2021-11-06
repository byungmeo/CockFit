package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String nickname;

    private String forumType;

    private EditText editText_title;
    private EditText editText_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        nickname = user.getDisplayName();

        forumType = getIntent().getStringExtra("forum");

        editText_title = findViewById(R.id.write_editText_title);
        editText_content = findViewById(R.id.write_editText_content);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.write_button_backButton) {
            this.onBackPressed();
        } else if(view.getId() == R.id.write_button_done) {
            if(editText_title.getText().toString().equals("")) {
                Toast.makeText(this , "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if(editText_content.getText().toString().equals("")) {
                Toast.makeText(this , "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            Post post = new Post(editText_title.getText().toString(), nickname, sdf.format(date));
            post.setContent(editText_content.getText().toString());

            mDatabase.child("forum").child(forumType).push().setValue(post);

            this.onBackPressed();
        }
    }
}