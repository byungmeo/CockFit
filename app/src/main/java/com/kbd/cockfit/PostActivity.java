package com.kbd.cockfit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Post post;
    private String postId;
    private String forumType;

    private TextView textView_title;
    private TextView textView_writer;
    private TextView textView_date;
    //private TextView textView_content;
    private ImageButton button_more;
    private ImageView imageView_writerProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        postId = getIntent().getStringExtra("postId");
        forumType = getIntent().getStringExtra("forum");

        textView_title = findViewById(R.id.post_textView_title);
        textView_writer = findViewById(R.id.post_textView_writer);
        textView_date = findViewById(R.id.post_textView_date);
        //textView_content = findViewById(R.id.post_textView_content);
        button_more = findViewById(R.id.post_button_more);
        imageView_writerProfile = findViewById(R.id.post_imageView_writerProfile);

        Log.d("forumType", forumType);
        Log.d("postId", postId);
        mDatabase.child("forum").child(forumType).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                if(post != null) {
                    textView_title.setText(post.getTitle());
                    textView_writer.setText(post.getWriter());
                    textView_date.setText(post.getDate());
                    StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
                    mStorage.child("Users").child(post.getUid()).child("profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("test", uri.toString());
                            Picasso.get().load(uri).into(imageView_writerProfile);
                        }
                    });

                    //textView_content.setText(post.getContent());

                    if(mAuth.getUid().equals(post.getUid())) {
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
                    } else {
                        button_more.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_backButton) { this.onBackPressed(); }
    }
}