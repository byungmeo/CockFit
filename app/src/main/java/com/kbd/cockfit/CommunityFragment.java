package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    View v;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_community, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView shareMore = v.findViewById(R.id.community_textView_shareMore);
        TextView qaMore = v.findViewById(R.id.community_textView_qaMore);
        TextView generalMore = v.findViewById(R.id.community_textView_generalMore);

        shareMore.setOnClickListener(this);
        qaMore.setOnClickListener(this);
        generalMore.setOnClickListener(this);

        initRecentPost();

        return v;
    }

    public void initRecentPost() {
        mDatabase.child("forum").child("share").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long i = 4;
                long postNum = snapshot.getChildrenCount();

                if(postNum == 0) {
                    return;
                }

                //최근 표시 게시물을 표시할 때, 실시간으로 최근 게시물이 삭제될 경우
                //똑같은 게시판이 2개 표시되는 현상을 방지합니다.
                if(postNum < 4) {
                    for(long index = 4; index > postNum; index--) {
                        String _titleViewTag = "textView_share" + String.valueOf(index);
                        String _dateViewTag = _titleViewTag + "_date";

                        TextView title = v.findViewWithTag(_titleViewTag);
                        TextView date = v.findViewWithTag(_dateViewTag);

                        title.setText("");
                        date.setText("");
                    }
                    i = postNum;
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String titleViewTag = "textView_share" + String.valueOf(i);
                    String dateViewTag = titleViewTag + "_date";
                    String constraintTag = "constraintLayout_share" + String.valueOf(i);

                    TextView title = v.findViewWithTag(titleViewTag);
                    TextView date = v.findViewWithTag(dateViewTag);
                    ConstraintLayout constraintLayout = v.findViewWithTag(constraintTag);

                    Post post = postSnapshot.getValue(Post.class);
                    title.setText(post.getTitle());
                    date.setText(post.getDate());

                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), PostActivity.class);
                            intent.putExtra("post", post);
                            intent.putExtra("postId", postSnapshot.getKey());
                            intent.putExtra("forum", "share");
                            startActivity(intent);
                        }
                    });
                    i--;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mDatabase.child("forum").child("qa").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long i = 4;
                long postNum = snapshot.getChildrenCount();

                if(postNum == 0) {
                    return;
                }

                //최근 표시 게시물을 표시할 때, 실시간으로 최근 게시물이 삭제될 경우
                //똑같은 게시판이 2개 표시되는 현상을 방지합니다.
                if(postNum < 4) {
                    for(long index = 4; index > postNum; index--) {
                        String _titleViewTag = "textView_qa" + String.valueOf(index);
                        String _dateViewTag = _titleViewTag + "_date";

                        TextView title = v.findViewWithTag(_titleViewTag);
                        TextView date = v.findViewWithTag(_dateViewTag);

                        title.setText("");
                        date.setText("");
                    }
                    i = postNum;
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String titleViewTag = "textView_qa" + String.valueOf(i);
                    String dateViewTag = titleViewTag + "_date";
                    String constraintTag = "constraintLayout_qa" + String.valueOf(i);

                    TextView title = v.findViewWithTag(titleViewTag);
                    TextView date = v.findViewWithTag(dateViewTag);
                    ConstraintLayout constraintLayout = v.findViewWithTag(constraintTag);

                    Post post = postSnapshot.getValue(Post.class);
                    title.setText(post.getTitle());
                    date.setText(post.getDate());

                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), PostActivity.class);
                            intent.putExtra("post", post);
                            intent.putExtra("postId", postSnapshot.getKey());
                            intent.putExtra("forum", "qa");
                            startActivity(intent);
                        }
                    });
                    i--;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        mDatabase.child("forum").child("general").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long i = 4;
                long postNum = snapshot.getChildrenCount();

                if(postNum == 0) {
                    return;
                }

                //최근 표시 게시물을 표시할 때, 실시간으로 최근 게시물이 삭제될 경우
                //똑같은 게시판이 2개 표시되는 현상을 방지합니다.
                if (postNum < 4) {
                    for (long index = 4; index > postNum; index--) {
                        String _titleViewTag = "textView_general" + String.valueOf(index);
                        String _dateViewTag = _titleViewTag + "_date";

                        TextView title = v.findViewWithTag(_titleViewTag);
                        TextView date = v.findViewWithTag(_dateViewTag);

                        title.setText("");
                        date.setText("");
                    }
                    i = postNum;
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String titleViewTag = "textView_general" + String.valueOf(i);
                    String dateViewTag = titleViewTag + "_date";
                    String constraintTag = "constraintLayout_general" + String.valueOf(i);

                    TextView title = v.findViewWithTag(titleViewTag);
                    TextView date = v.findViewWithTag(dateViewTag);
                    ConstraintLayout constraintLayout = v.findViewWithTag(constraintTag);

                    Post post = postSnapshot.getValue(Post.class);
                    title.setText(post.getTitle());
                    date.setText(post.getDate());

                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), PostActivity.class);
                            intent.putExtra("post", post);
                            intent.putExtra("postId", postSnapshot.getKey());
                            intent.putExtra("forum", "general");
                            startActivity(intent);
                        }
                    });
                    i--;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, ForumActivity.class);
        switch (v.getId()) {
            case R.id.community_textView_qaMore: {
                //더보기 버튼
                intent.putExtra("forum", "qa");
                break;
            }
            case R.id.community_textView_shareMore: {
                intent.putExtra("forum", "share");
                break;
            }
            case R.id.community_textView_generalMore: {
                intent.putExtra("forum", "general");
                break;
            }
            default: {
                break;
            }
        }
        context.startActivity(intent);
    }
}