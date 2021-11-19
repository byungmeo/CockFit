package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ForumActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private RecyclerView postRecycler;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;
    private HashMap<Post, String> postIdMap;
    private Toolbar toolbar;

    private String forumType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Context context = this;

        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        toolbar = findViewById(R.id.forum_materialToolbar);
        forumType = getIntent().getStringExtra("forum");
        switch (forumType) {
            case "share" : {
                toolbar.setTitle("레시피 공유 게시판");
                break;
            }
            case "qa" : {
                toolbar.setTitle("질문 게시판");
                //screenName.setText("질문 게시판");
                break;
            }
            case "general" : {
                toolbar.setTitle("자유 게시판");
                //screenName.setText("자유 게시판");
                break;
            }
            case "myPost" : {
                toolbar.setTitle("내 게시글 목록");
                //screenName.setText("내 게시글 목록");
                break;
            }
            default: {
                break;
            }
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.forum_menuItem_wirtePost) {
                    Intent intent = new Intent(context, WritePostActivity.class);
                    intent.putExtra("forum", forumType);
                    startActivity(intent);
                }
                return false;
            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        postRecycler = findViewById(R.id.forum_recycler);
        postRecycler.setHasFixedSize(true);
        postRecycler.setLayoutManager(layoutManager);

        postIdMap = new HashMap<>();
        postArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(postArrayList);
        postRecycler.setAdapter(postAdapter);

        initPostList();
    }

    public void initPostList() {
        mDatabase.child("forum").child(forumType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    postArrayList.add(post);
                    postIdMap.put(post, postSnapshot.getKey());
                }

                Collections.reverse(postArrayList);
                postAdapter = new PostAdapter(postArrayList);
                postRecycler.setAdapter(postAdapter);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Post> postArrayList;

        public PostAdapter(ArrayList<Post> postArrayList) {
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
            postViewHolder.title.setText(postArrayList.get(position).getTitle());
            postViewHolder.writer.setText(postArrayList.get(position).getNickname());
            try {
                postViewHolder.date.setText(UtilitySet.formatTimeString(postArrayList.get(position).getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            HashMap<String, String> likeUidMap = postArrayList.get(position).getLikeUidMap();
            if(likeUidMap != null) {
                postViewHolder.like.setText(String.valueOf(postArrayList.get(position).getLikeUidMap().size()));
            }

            postViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = postArrayList.get(holder.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), PostActivity.class);
                    intent.putExtra("postId", postIdMap.get(post));
                    intent.putExtra("forum", forumType);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return postArrayList.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout constraintLayout;
            private TextView title;
            private TextView writer;
            private TextView date;
            private TextView like;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.myPostItem_constraintLayout);
                title = itemView.findViewById(R.id.myPostItem_textView_postTitle);
                writer = itemView.findViewById(R.id.myPostItem_textView_writer);
                date = itemView.findViewById(R.id.myPostItem_textView_postDate);
                like = itemView.findViewById(R.id.myPostItem_textView_like);
            }
        }
    }
}