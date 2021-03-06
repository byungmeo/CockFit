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
import android.view.inputmethod.EditorInfo;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView postRecycler;
    private LinearLayoutManager layoutManager;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;
    private HashMap<Post, String> postIdMap;
    private ArrayList<String> bookmarkedPostIdList;
    private Toolbar toolbar;
    private HashMap<String, String> myActivityPostMap; //<PostId, ForumType>

    private String forumType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Context context = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        toolbar = findViewById(R.id.forum_materialToolbar);
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
                    intent.putExtra("forumType", forumType);
                    startActivity(intent);
                } else if(item.getItemId() == R.id.forum_menuItem_search) {
                    SearchView searchView = (SearchView) item.getActionView();
                    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            postAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
                return false;
            }
        });

        forumType = getIntent().getStringExtra("forumType");
        switch (forumType) {
            case "share" : {
                toolbar.setTitle("????????? ?????? ?????????");
                toolbar.getMenu().getItem(1).setVisible(false);
                break;
            }
            case "qa" : {
                toolbar.setTitle("?????? ?????????");
                break;
            }
            case "general" : {
                toolbar.setTitle("?????? ?????????");
                break;
            }
            case "myPost" : {
                toolbar.setTitle("??? ????????? ??????");
                toolbar.getMenu().setGroupVisible(0, false);
                myActivityPostMap = new HashMap<>();
                break;
            }
            case "bookmarkSharePost" : {
                toolbar.setTitle("??????????????? ?????? ?????????");
                toolbar.getMenu().setGroupVisible(0, false);
                break;
            }
        }

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
        } else if(forumType.equals("bookmarkSharePost")) {
            initBookmarkSharePostList();
        } else {
            initPostList();
        }
    }

    public void initBookmarkSharePostList() {
        bookmarkedPostIdList = new ArrayList<>();
        mDatabase.child("user").child(mAuth.getUid()).child("bookmarkedPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                bookmarkedPostIdList.clear();

                for(DataSnapshot idSnapshot : snapshot.getChildren()) {
                    bookmarkedPostIdList.add(idSnapshot.getKey());
                }

                Log.d("test", "bookmarkedPostIdList.size : " + bookmarkedPostIdList.size());
                if(bookmarkedPostIdList.size() == 0) {
                    postAdapter.notifyDataSetChanged();
                    return;
                }

                for(String postId : bookmarkedPostIdList) {
                    mDatabase.child("forum").child("share").child(postId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Post post = snapshot.getValue(RecipePost.class);
                            postArrayList.add(post);

                            if(postArrayList.size() == bookmarkedPostIdList.size()) {
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

    public void initMyActivityPostList() {
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
                            Post post = snapshot.getValue(RecipePost.class);
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

    //?????? ???????????? ?????? ???????????? ???????????????
    public void initPostList() {
        mDatabase.child("forum").child(forumType).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post;
                    if(forumType.equals("share")) {
                        post = postSnapshot.getValue(RecipePost.class);
                    } else {
                        post = postSnapshot.getValue(Post.class);
                    }

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

    public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        private ArrayList<Post> postArrayList;
        private ArrayList<Post> postArrayListFull;

        public PostAdapter(ArrayList<Post> postArrayList) {
            this.postArrayList = postArrayList;
            this.postArrayListFull = new ArrayList<>(postArrayList);
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

                    if(forumType.equals("myPost")) {
                        intent.putExtra("postId", postIdMap.get(post));
                        intent.putExtra("forumType", myActivityPostMap.get(postIdMap.get(post)));
                        intent.putExtra("post", post);
                    } else if(forumType.equals("share")) {
                        intent.putExtra("postId", postIdMap.get(post));
                        intent.putExtra("forumT", "share");
                        intent.putExtra("post", post);
                    } else if(forumType.equals("bookmarkSharePost")) {
                        intent.putExtra("postId", bookmarkedPostIdList.get(holder.getAdapterPosition()));
                        intent.putExtra("forumType", "share");
                        intent.putExtra("post", post);
                    } else {
                        intent.putExtra("postId", postIdMap.get(post));
                        intent.putExtra("forumType", forumType);
                        intent.putExtra("post", post);
                    }

                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return postArrayList.size();
        }

        @Override
        public Filter getFilter() {
            return postFilter;
        }

        private Filter postFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Post> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(postArrayListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Post post : postArrayListFull) {
                        if (post.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(post);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                postArrayList.clear();
                postArrayList.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };

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