package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText editText_title;
    private EditText editText_content;

    private Boolean isEdit;
    private Post editPost;
    private String editPostId;
    private String forumType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        editText_title = findViewById(R.id.write_editText_title);
        editText_content = findViewById(R.id.write_editText_content);

        Intent intent = getIntent();
        if(isEdit = intent.getBooleanExtra("isEdit", false)) {
            editPost = intent.getParcelableExtra("post");
            editPostId = intent.getStringExtra("postId");
            initEditPost();
        }
        forumType = intent.getStringExtra("forum");
    }

    public void initEditPost() {
        editText_title.setText(editPost.getTitle());
        editText_content.setText(editPost.getContent());
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

            if(isEdit) {
                editPost.setTitle(editText_title.getText().toString());
                editPost.setContent(editText_content.getText().toString());
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(editPostId, editPost);
                mDatabase.child("forum").child(forumType).updateChildren(childUpdates);
            } else {
                FirebaseUser user = mAuth.getCurrentUser();
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));
                Post post = new Post(editText_title.getText().toString(), user.getDisplayName(), mAuth.getUid(), date);
                post.setContent(editText_content.getText().toString());

                mDatabase.child("forum").child(forumType).push().setValue(post);
            }

            this.onBackPressed();
        }
    }
}