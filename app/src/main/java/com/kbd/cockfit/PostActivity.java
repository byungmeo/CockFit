package com.kbd.cockfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FragmentManager fragmentManager;
    private GeneralPostFragment generalPostFragment;

    private Post post;
    private String postId;
    private String forumType;

    private ProgressBar progressBar;

    private ImageView imageView_writerProfile;
    private TextView textView_writer;
    private TextView textView_date;
    private NestedScrollView scrollView;
    private EditText editText_comment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        context = this;

        //loading...
        progressBar = findViewById(R.id.post_progressBar);
        scrollView = findViewById(R.id.post_scrollView_content);
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //firebase initialize
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //getIntent
        post = getIntent().getParcelableExtra("post");
        postId = getIntent().getStringExtra("postId");
        forumType = getIntent().getStringExtra("forum");

        //fragment
        fragmentManager = getSupportFragmentManager();
        generalPostFragment = new GeneralPostFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.post_frameLayout, generalPostFragment).commitAllowingStateLoss();
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        bundle.putString("forum", forumType);
        generalPostFragment.setArguments(bundle);

        //view initialize
        imageView_writerProfile = findViewById(R.id.post_imageView_writerProfile);
        textView_writer = findViewById(R.id.post_textView_writer);
        textView_date = findViewById(R.id.post_textView_date);
        editText_comment = findViewById(R.id.post_editText_comment);
        toolbar = findViewById(R.id.post_materialToolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { onBackPressed(); }
        });

        //Intent로부터 전달받은 postId를 Firebase에서 탐색한 후 해당 Post객체를 받아옵니다.
        mDatabase.child("forum").child(forumType).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                
                //postId에 해당하는 게시글이 있는 경우
                if(post != null) {
                    toolbar.setTitle(post.getTitle());
                    textView_writer.setText(post.getNickname());
                    try {
                        textView_date.setText(UtilitySet.formatTimeString(post.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //FirebaseStorage에서 작성자의 프로필 사진을 불러옵니다.
                    StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
                    mStorage.child("Users").child(post.getUid()).child("profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //프로필 사진이 있는 사용자
                            Activity activity = PostActivity.this;
                            if(activity.isFinishing())
                                return;

                            Glide.with(activity)
                                    .load(uri)
                                    .into(imageView_writerProfile);

                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //프로필 사진이 없는 사용자
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    //postId가 Firebase에 존재하지 않는 경우
                    Log.d("Exception", "서버에서 해당하는 게시글을 찾을 수 없습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mAuth.getUid().equals(post.getUid())) {
            menu.setGroupVisible(0, false);
        } else {
            getMenuInflater().inflate(R.menu.top_app_bar_post, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.post_menuItem_delete) {
            //게시글 삭제
            mDatabase.child("forum").child(forumType).child(postId).setValue(null);
            onBackPressed();
        } else if(item.getItemId() == R.id.post_menuItem_edit) {
            //게시글 수정
            Intent intent = new Intent(context, WritePostActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("post", post);
            intent.putExtra("postId", postId);
            intent.putExtra("forum", forumType);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_comment) {
            String commentText = editText_comment.getText().toString();
            if(commentText.length() == 0) {
                Toast.makeText(this, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String nickname = mAuth.getCurrentUser().getDisplayName();
            String uid = mAuth.getUid();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

            Comment comment = new Comment(commentText, nickname, uid, date);
            mDatabase.child("forum").child(forumType).child(postId).child("comments").push().setValue(comment);
        }
    }
}