package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ForumActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private RecyclerView postRecycler;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;
    private HashMap<Post, String> postIdMap;
    private Toolbar toolbar;
    private HashMap<String, String> myActivityPostMap; //<PostId, ForumType>

    private String forumType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Context context = this;

        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        toolbar = findViewById(R.id.forum_materialToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        forumType = getIntent().getStringExtra("forum");
        switch (forumType) {
            case "share" : {
                toolbar.setTitle("레시피 공유 게시판");
                break;
            }
            case "qa" : {
                toolbar.setTitle("질문 게시판");
                break;
            }
            case "general" : {
                toolbar.setTitle("자유 게시판");
                break;
            }
            case "myPost" : {
                toolbar.setTitle("내 게시글 목록");
                toolbar.getMenu().setGroupVisible(0, false);
                myActivityPostMap = new HashMap<>();
                break;
            }
            default: {
                break;
            }
        }

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

        if(forumType.equals("myPost")) {
            initMyActivityPostList();
        } else {
            initPostList();
        }
    }

    public void initMyActivityPostList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase.child("user").child(mAuth.getUid()).child("community").child("posting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                myActivityPostMap.clear();

                for(DataSnapshot postInfoSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> value = (HashMap<String, String>) postInfoSnapshot.getValue();
                    myActivityPostMap.put(postInfoSnapshot.getKey(),value.get("ForumType"));
                }

                Log.d("test", "myActivityPostMap.size : " + myActivityPostMap.size());
                if(myActivityPostMap.size() == 0) {
                    postAdapter.notifyDataSetChanged();
                    return;
                }

                for(Map.Entry<String, String> postInfo : myActivityPostMap.entrySet()) {
                    mDatabase.child("forum").child(postInfo.getValue()).child(postInfo.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("test", "OnDataChange");
                            Post post = snapshot.getValue(Post.class);
                            postArrayList.add(post);
                            postIdMap.put(post, snapshot.getKey());

                            if(postArrayList.size() == myActivityPostMap.size()) {
                                Collections.reverse(postArrayList);
                                postAdapter = new PostAdapter(postArrayList);
                                postRecycler.setAdapter(postAdapter);
                            }
                        }

                        @Override public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //해당 게시판의 모든 게시글을 불러옵니다
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
            Log.d("test", String.valueOf(getItemCount()));
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
                    if(forumType.equals("myPost")) {
                        intent.putExtra("forum", myActivityPostMap.get(postIdMap.get(post)));
                    } else {
                        intent.putExtra("forum", forumType);
                    }

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