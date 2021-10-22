package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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
}