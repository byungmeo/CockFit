package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;

    private String forumType;

    private TextView screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        screenName = findViewById(R.id.forom_textView_screenName);
        setScreenName();

        initMyPostRecycler();
    }

    public void setScreenName() {
        forumType = getIntent().getStringExtra("forum");
        switch (forumType) {
            case "share" : {
                screenName.setText("레시피 공유 게시판");
                break;
            }
            case "qa" : {
                screenName.setText("질문 게시판");
                break;
            }
            case "myPost" : {
                screenName.setText("내 게시글 목록");
                break;
            }
            default: {
                break;
            }
        }
    }

    public void initMyPostRecycler() {
        postRecycler = findViewById(R.id.forum_recycler);
        postArrayList = new ArrayList<>();

        initPostList();

        postAdapter = new PostAdapter(this, postArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        postRecycler.setLayoutManager(linearLayoutManager);
        postRecycler.setAdapter(postAdapter);
    }

    public void initPostList() {
        mDatabase.child("forum").child(forumType).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    postArrayList.add(post);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.forum_button_backButton) {
            this.onBackPressed();
        } else if(view.getId() == R.id.forum_button_write) {
            Intent intent = new Intent(this, WritePostActivity.class);
            intent.putExtra("forum", forumType);
            startActivity(intent);
        }
    }

    public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private ArrayList<Post> postArrayList;

        public PostAdapter(Context context, ArrayList<Post> postArrayList) {
            this.context = context;
            this.postArrayList = postArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypostitem_layout, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.postTitle.setText(postArrayList.get(position).getPostTitle());
            postViewHolder.writer.setText(postArrayList.get(position).getWriter());
            postViewHolder.postDate.setText(postArrayList.get(position).getPostDate());

            postViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return postArrayList.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout constraintLayout;
            private TextView postTitle;
            private TextView writer;
            private TextView postDate;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.myPostItem_constraintLayout);
                postTitle = itemView.findViewById(R.id.myPostItem_textView_postTitle);
                writer = itemView.findViewById(R.id.myPostItem_textView_writer);
                postDate = itemView.findViewById(R.id.myPostItem_textView_postDate);
            }
        }
    }
}