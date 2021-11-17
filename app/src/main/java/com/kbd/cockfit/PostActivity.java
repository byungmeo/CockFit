package com.kbd.cockfit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FragmentManager fragmentManager;
    private GeneralPostFragment generalPostFragment;

    private Post post;
    private String postId;
    private String forumType;

    private ProgressBar progressBar;

    private TextView textView_title;
    private ImageButton button_more;
    private ImageView imageView_writerProfile;
    private TextView textView_writer;
    private TextView textView_date;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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
        textView_title = findViewById(R.id.post_textView_title);
        button_more = findViewById(R.id.post_button_more);
        imageView_writerProfile = findViewById(R.id.post_imageView_writerProfile);
        textView_writer = findViewById(R.id.post_textView_writer);
        textView_date = findViewById(R.id.post_textView_date);

        //Intent로부터 전달받은 postId를 Firebase에서 탐색한 후 해당 Post객체를 받아옵니다.
        mDatabase.child("forum").child(forumType).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue(Post.class);
                
                //postId에 해당하는 게시글이 있는 경우
                if(post != null) {
                    textView_title.setText(post.getTitle());
                    textView_writer.setText(post.getNickname());
                    textView_date.setText(post.getDate());

                    //FirebaseStorage에서 작성자의 프로필 사진을 불러옵니다.
                    StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
                    mStorage.child("Users").child(post.getUid()).child("profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Activity activity = PostActivity.this;
                            if(activity.isFinishing())
                                return;

                            Glide.with(activity)
                                    .load(uri)
                                    .into(imageView_writerProfile);

                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });

                    //작성자가 자신의 게시물을 보게 되면, 상단 우측에 more버튼이 보이며, 클릭 시 메뉴가 뜹니다. (게시글 수정, 삭제버튼)
                    if(mAuth.getUid().equals(post.getUid())) {
                        button_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //메뉴를 생성하고, 수정,삭제 메뉴아이템에 기능을 추가합니다.
                                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                                getMenuInflater().inflate(R.menu.post_menu, popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if(item.getItemId() == R.id.postMenu_edit) {
                                            //게시글 수정
                                            Intent intent = new Intent(v.getContext(), WritePostActivity.class);
                                            intent.putExtra("isEdit", true);
                                            intent.putExtra("post", post);
                                            intent.putExtra("postId", postId);
                                            intent.putExtra("forum", forumType);
                                            startActivity(intent);
                                        } else if(item.getItemId() == R.id.postMenu_delete) {
                                            //게시글 삭제
                                            mDatabase.child("forum").child(forumType).child(postId).setValue(null);
                                            onBackPressed();
                                        }

                                        return false;
                                    }
                                });
                                popupMenu.show(); //최종적으로 more버튼 클릭 시 메뉴가 보이도록 합니다.
                            }
                        });
                    } else {
                        button_more.setVisibility(View.INVISIBLE); //작성자가 아닐 경우, 메뉴는 보이지 않습니다.
                    }
                } else {
                    //postId가 Firebase에 존재하지 않는 경우
                    Log.d("Exception", "서버에서 해당하는 게시글을 찾을 수 없습니다.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public Boolean isLiked() {
        return false;
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_backButton) { this.onBackPressed(); }
    }
}