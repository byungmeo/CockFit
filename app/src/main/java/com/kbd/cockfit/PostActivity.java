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
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Post post;
    //private RecipePost recipePost;

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

        //getIntent
        Intent getIntent = getIntent();
        if((forumType = getIntent.getStringExtra("forumT")) == null) {
            forumType = getIntent.getStringExtra("forumType");
        }

        post = getIntent.getParcelableExtra("post");
        postId = getIntent.getStringExtra("postId");

        //fragment setting
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GeneralPostFragment generalPostFragment;
        RecipePostFragment recipePostFragment;
        if(forumType.equals("share")) {
            recipePostFragment = new RecipePostFragment();
            transaction.replace(R.id.post_frameLayout, recipePostFragment).commitAllowingStateLoss();
            Bundle bundle = new Bundle();
            bundle.putString("postId", postId);
            bundle.putString("forumType", forumType);
            RecipePost recipePost = (RecipePost) post;
            bundle.putString("recipeId", recipePost.getRecipeId());
            bundle.putString("writerUid", recipePost.getUid());
            recipePostFragment.setArguments(bundle);
        } else {
            generalPostFragment = new GeneralPostFragment();
            transaction.replace(R.id.post_frameLayout, generalPostFragment).commitAllowingStateLoss();
            Bundle bundle = new Bundle();
            bundle.putString("postId", postId);
            bundle.putString("forumType", forumType);
            generalPostFragment.setArguments(bundle);
        }

        //Intent????????? ???????????? postId??? Firebase?????? ????????? ??? ?????? Post????????? ???????????????.
        mDatabase.child("forum").child(forumType).child(postId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(forumType.equals("share")) {
                    post = dataSnapshot.getValue(RecipePost.class);
                } else {
                    post = dataSnapshot.getValue(Post.class);
                }

                //postId??? ???????????? ???????????? ?????? ??????
                if(post != null) {
                    toolbar.setTitle(post.getTitle());
                    if(!mAuth.getUid().equals(post.getUid())) {
                        toolbar.getMenu().setGroupVisible(0, false);
                    }
                    textView_writer.setText(post.getNickname());
                    try {
                        textView_date.setText(UtilitySet.formatTimeString(post.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //FirebaseStorage?????? ???????????? ????????? ????????? ???????????????.
                    StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
                    mStorage.child("Users").child(post.getUid()).child("profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //????????? ????????? ?????? ?????????
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
                            //????????? ????????? ?????? ?????????
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    //postId??? Firebase??? ???????????? ?????? ??????
                    Log.d("Exception", "???????????? ???????????? ???????????? ?????? ??? ????????????.");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_post, menu);
        menu.getItem(2).setVisible(false); //???????????? ??????
        menu.getItem(3).setVisible(false); //???????????? ??????
        
        if(forumType.equals("share")) {
            menu.getItem(0).setVisible(false);
        }

        if(!FirebaseAuth.getInstance().getUid().equals(post.getUid())) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        mDatabase.child("admin").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                    String adminUid = adminSnapshot.getKey();
                    if(adminUid.equals(mAuth.getUid())) {
                        menu.getItem(2).setVisible(true);
                        if(forumType.equals("share")) {
                            menu.getItem(3).setVisible(true);
                        }
                        break;
                    }
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.post_menuItem_delete || item.getItemId() == R.id.post_menuItem_admin_delete) {
            //????????? ??????
            mDatabase.child("forum").child(forumType).child(postId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    mDatabase.child("user").child(post.getUid()).child("community").child("posting").child(postId).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mDatabase.child("panel").child("hotRecipe").child(postId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) { onBackPressed(); }
                            });
                        }
                    });
                }
            });

            if(forumType.equals("share")) {
                RecipePost recipePost = (RecipePost) post;
                HashMap<String, Object> update = new HashMap<>();
                update.put("isShare", new Boolean(false));
                update.put("sharePostId", null);
                mDatabase.child("user").child(post.getUid()).child("MyRecipe").child(recipePost.getRecipeId()).updateChildren(update);
            }
        } else if(item.getItemId() == R.id.post_menuItem_edit) {
            //????????? ??????
            Intent intent = new Intent(context, WritePostActivity.class);
            intent.putExtra("isEdit", true);
            intent.putExtra("post", post);
            intent.putExtra("postId", postId);
            intent.putExtra("forumType", forumType);
            startActivity(intent);
        } else if(item.getItemId() == R.id.post_menuItem_admin_hot) {
            HashMap<String, String> value = new HashMap<>();
            value.put("postId", postId);
            value.put("recipeId", ((RecipePost)post).getRecipeId());
            value.put("uid", post.getUid());
            mDatabase.child("panel").child("hotRecipe").child(postId).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) { onBackPressed(); }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_comment) {
            String commentText = editText_comment.getText().toString();
            if(commentText.length() == 0) {
                Toast.makeText(this, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                return;
            }

            String nickname = mAuth.getCurrentUser().getDisplayName();
            String uid = mAuth.getUid();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));

            String type = "????????? ????????? ???????????????.";

            Comment comment = new Comment(commentText, nickname, uid, date);

            String title = post.getTitle();
            Notify notify = new Notify(title, commentText, type, date, uid);


            //????????? ?????? ?????? ???????????? DB??? ?????? ???????????? ??????
            mDatabase.child("forum").child(forumType).child(postId).child("uid").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    mDatabase.child("user").child(value).child("notify").push().setValue(notify);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // ????????? ??????
                }
            });

            mDatabase.child("forum").child(forumType).child(postId).child("comments").push().setValue(comment);


            editText_comment.setText("");
        }
    }
}