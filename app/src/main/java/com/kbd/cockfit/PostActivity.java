package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private Post post;
    private String postId;
    private String forumType;

    private TextView textView_title;
    private TextView textView_writer;
    private TextView textView_content;
    private ImageButton button_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        postId = getIntent().getStringExtra("postId");
        forumType = getIntent().getStringExtra("forum");

        textView_title = findViewById(R.id.post_textView_title);
        textView_writer = findViewById(R.id.post_textView_writer);
        textView_content = findViewById(R.id.post_textView_content);
        button_more = findViewById(R.id.post_button_more);

        mDatabase.child("forum").child(forumType).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                if(post != null) {
                    textView_title.setText(post.getTitle());
                    textView_writer.setText(post.getWriter());
                    textView_content.setText(post.getContent());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                getMenuInflater().inflate(R.menu.post_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.postMenu_edit) {
                            Intent intent = new Intent(v.getContext(), WritePostActivity.class);
                            intent.putExtra("isEdit", true);
                            intent.putExtra("post", post);
                            intent.putExtra("postId", postId);
                            intent.putExtra("forum", forumType);
                            startActivity(intent);
                        } else if(item.getItemId() == R.id.postMenu_delete) {
                            mDatabase.child("forum").child(forumType).child(postId).setValue(null);
                            onBackPressed();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_backButton) {
            this.onBackPressed();
        }
    }
}