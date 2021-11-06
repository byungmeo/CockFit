package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_community, container, false);
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
                if(snapshot.getChildrenCount() < 4) {
                    i = snapshot.getChildrenCount();
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(i > 0) {
                        String titleViewTag = "textView_share" + String.valueOf(i);
                        String dateViewTag = titleViewTag + "_date";
                        TextView title = getView().findViewWithTag(titleViewTag);
                        TextView date = getView().findViewWithTag(dateViewTag);

                        Post post = postSnapshot.getValue(Post.class);
                        title.setText(post.getTitle());
                        date.setText(post.getDate());
                    }
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
                if(snapshot.getChildrenCount() < 4) {
                    i = snapshot.getChildrenCount();
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(i > 0) {
                        String titleViewTag = "textView_qa" + String.valueOf(i);
                        String dateViewTag = titleViewTag + "_date";
                        TextView title = getView().findViewWithTag(titleViewTag);
                        TextView date = getView().findViewWithTag(dateViewTag);

                        Post post = postSnapshot.getValue(Post.class);
                        title.setText(post.getTitle());
                        date.setText(post.getDate());
                    }
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
                if(snapshot.getChildrenCount() < 4) {
                    i = snapshot.getChildrenCount();
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(i > 0) {
                        String titleViewTag = "textView_general" + String.valueOf(i);
                        String dateViewTag = titleViewTag + "_date";
                        TextView title = getView().findViewWithTag(titleViewTag);
                        TextView date = getView().findViewWithTag(dateViewTag);

                        Post post = postSnapshot.getValue(Post.class);
                        title.setText(post.getTitle());
                        date.setText(post.getDate());
                    }
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