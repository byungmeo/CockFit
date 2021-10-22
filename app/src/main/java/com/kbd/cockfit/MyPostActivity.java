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

public class MyPostActivity extends AppCompatActivity {
    private RecyclerView myPostRecycler;
    private MyPostAdapter myPostAdapter;
    private ArrayList<MyPost> myPostArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        initMyPostRecycler();
    }

    public void initMyPostRecycler() {
        myPostRecycler = findViewById(R.id.myPost_recycler);
        myPostArrayList = new ArrayList<>();

        initMyPostList();

        myPostAdapter = new MyPostAdapter(this, myPostArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myPostRecycler.setLayoutManager(linearLayoutManager);
        myPostRecycler.setAdapter(myPostAdapter);
    }

    public void initMyPostList() {
        myPostArrayList.add(new MyPost("테스트 게시물1", "게시글 요약문 테스트1", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물2", "게시글 요약문 테스트2", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물3", "게시글 요약문 테스트3", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물4", "게시글 요약문 테스트4", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물5", "게시글 요약문 테스트5", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물6", "게시글 요약문 테스트6", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물7", "게시글 요약문 테스트7", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물8", "게시글 요약문 테스트8", "00/00"));
        myPostArrayList.add(new MyPost("테스트 게시물9", "게시글 요약문 테스트9", "00/00"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void clickBackButton(View view) {
        onBackPressed();
    }

    public class MyPost {
        private String postName;
        private String postAbstract;
        private String postDate;

        public MyPost(String postName, String postAbstract, String postDate) {
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

    public class MyPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private ArrayList<MyPost> myPostArrayList;

        public MyPostAdapter(Context context, ArrayList<MyPost> myPostArrayList) {
            this.context = context;
            this.myPostArrayList = myPostArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypostitem_layout, parent, false);
            return new MyPostAdapter.MyPostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MyPostAdapter.MyPostViewHolder myPostViewHolder = (MyPostAdapter.MyPostViewHolder) holder;
            myPostViewHolder.postName.setText(myPostArrayList.get(position).getPostName());
            myPostViewHolder.postAbstract.setText(myPostArrayList.get(position).getPostAbstract());
            myPostViewHolder.postDate.setText(myPostArrayList.get(position).getPostDate());
        }

        @Override
        public int getItemCount() {
            return myPostArrayList.size();
        }

        public class MyPostViewHolder extends RecyclerView.ViewHolder {
            private TextView postName;
            private TextView postAbstract;
            private TextView postDate;

            public MyPostViewHolder(@NonNull View itemView) {
                super(itemView);
                postName = itemView.findViewById(R.id.myPostItem_textView_postName);
                postAbstract = itemView.findViewById(R.id.myPostItem_textView_postAbstract);
                postDate = itemView.findViewById(R.id.myPostItem_textView_postDate);
            }
        }
    }
}