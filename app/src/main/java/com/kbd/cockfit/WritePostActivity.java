package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {
    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextInputLayout editText_title;
    private TextInputLayout editText_content;
    private EditText editText_title_text;
    private EditText editText_content_text;
    private Boolean isEdit;
    private Post editPost;
    private String editPostId;
    private String forumType;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        context = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        editText_title = findViewById(R.id.write_editText_title);
        editText_content = findViewById(R.id.write_editText_content);
        editText_title_text = findViewById(R.id.write_editText_title_text);
        editText_content_text = findViewById(R.id.write_editText_content_text);

        Intent intent = getIntent();
        if(isEdit = intent.getBooleanExtra("isEdit", false)) {
            editPost = intent.getParcelableExtra("post");
            editPostId = intent.getStringExtra("postId");
            initEditPost();
        }
        forumType = intent.getStringExtra("forumType");

        toolbar = findViewById(R.id.write_materialToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { onBackPressed(); }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.write_menuItem_write) {
                    if(editText_title.getEditText().getText().toString().equals("")) {
                        Toast.makeText(context, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if(editText_content.getEditText().getText().toString().equals("")) {
                        Toast.makeText(context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if(isEdit) {
                        editPost.setTitle(editText_title.getEditText().getText().toString());
                        editPost.setContent(editText_content.getEditText().getText().toString());
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(editPostId, editPost);
                        mDatabase.child("forum").child(forumType).updateChildren(childUpdates);
                    } else {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                        Post post = new Post(editText_title.getEditText().getText().toString(), user.getDisplayName(), uid, date);
                        post.setContent(editText_content.getEditText().getText().toString());

                        String key = mDatabase.child("forum").child(forumType).push().getKey();
                        HashMap<String, String> value = new HashMap<>();
                        value.put("ForumType", forumType);
                        mDatabase.child("forum").child(forumType).child(key).setValue(post);
                        mDatabase.child("user").child(uid).child("community").child("posting").child(key).setValue(value);
                    }
                    onBackPressed();

                    return true;
                }
                return false;
            }
        });
    }

    public void initEditPost() {
        editText_title_text.setText(editPost.getTitle());
        editText_content_text.setText(editPost.getContent());
    }
}