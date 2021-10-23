package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {
    private RecyclerView postRecycler;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;

    private TextView screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        screenName = findViewById(R.id.forom_textView_screenName);
        setScreenName();

        initMyPostRecycler();
    }

    public void setScreenName() {
        switch (getIntent().getStringExtra("forum")) {
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

        initMyPostList();

        postAdapter = new PostAdapter(this, postArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        postRecycler.setLayoutManager(linearLayoutManager);
        postRecycler.setAdapter(postAdapter);
    }

    public void initMyPostList() {
        postArrayList.add(new Post("테스트 게시물1", "게시글 요약문 테스트1", "00/00"));
        postArrayList.add(new Post("테스트 게시물2", "게시글 요약문 테스트2", "00/00"));
        postArrayList.add(new Post("테스트 게시물3", "게시글 요약문 테스트3", "00/00"));
        postArrayList.add(new Post("테스트 게시물4", "게시글 요약문 테스트4", "00/00"));
        postArrayList.add(new Post("테스트 게시물5", "게시글 요약문 테스트5", "00/00"));
        postArrayList.add(new Post("테스트 게시물6", "게시글 요약문 테스트6", "00/00"));
        postArrayList.add(new Post("테스트 게시물7", "게시글 요약문 테스트7", "00/00"));
        postArrayList.add(new Post("테스트 게시물8", "게시글 요약문 테스트8", "00/00"));
        postArrayList.add(new Post("테스트 게시물9", "게시글 요약문 테스트9", "00/00"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickBackButton(View view) {
        onBackPressed();
    }

    public class Post {
        private String postName;
        private String postAbstract;
        private String postDate;

        public Post(String postName, String postAbstract, String postDate) {
            this.postName = postName;
            this.postAbstract = postAbstract;
            this.postDate = postDate;
        }

        public String getPostName() {
            return postName;
        }

        public String getPostAbstract() {
            return postAbstract;
        }

        public String getPostDate() {
            return postDate;
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
            postViewHolder.postName.setText(postArrayList.get(position).getPostName());
            postViewHolder.postAbstract.setText(postArrayList.get(position).getPostAbstract());
            postViewHolder.postDate.setText(postArrayList.get(position).getPostDate());
        }

        @Override
        public int getItemCount() {
            return postArrayList.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
            private TextView postName;
            private TextView postAbstract;
            private TextView postDate;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                postName = itemView.findViewById(R.id.myPostItem_textView_postName);
                postAbstract = itemView.findViewById(R.id.myPostItem_textView_postAbstract);
                postDate = itemView.findViewById(R.id.myPostItem_textView_postDate);
            }
        }
    }
}