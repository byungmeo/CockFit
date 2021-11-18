package com.kbd.cockfit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class GeneralPostFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String forumType;
    private String postId;
    private HashMap<String, String> likeUidMap;

    private TextView textView_contents;
    private Button button_like;

    private Drawable drawable_alreadyLike;
    private Drawable drawable_waitLike;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_general_post, container, false);

        //firebase initialize
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //getBundle
        Bundle bundle = getArguments();
        forumType = bundle.getString("forum");
        postId = bundle.getString("postId");

        //view initialize
        textView_contents = v.findViewById(R.id.general_textView_contents);
        button_like = v.findViewById(R.id.general_button_like);

        drawable_alreadyLike = getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24);
        drawable_waitLike = getResources().getDrawable(R.drawable.ic_outline_thumb_up_24);


        mDatabase.child("forum").child(forumType).child(postId).child("content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { textView_contents.setText(snapshot.getValue(String.class)); }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        mDatabase.child("forum").child(forumType).child(postId).child("likeUidMap").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likeUidMap = (HashMap<String, String>) snapshot.getValue();
                int likeCount = (int) snapshot.getChildrenCount();

                if(likeUidMap != null) {
                    button_like.setText(String.valueOf(likeCount));
                    if(likeUidMap.containsKey(mAuth.getUid())) {
                        button_like.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_alreadyLike, null, null, null);
                        button_like.setOnClickListener(new UnLikeListener());
                    } else {
                        button_like.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_waitLike, null, null, null);
                        button_like.setOnClickListener(new LikeListener());
                    }
                } else {
                    button_like.setText("0");
                    button_like.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_waitLike, null, null, null);
                    button_like.setOnClickListener(new LikeListener());
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        return v;
    }


    private class LikeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(mAuth.getUid(), mAuth.getCurrentUser().getDisplayName());
            mDatabase.child("forum").child(forumType).child(postId).child("likeUidMap").updateChildren(childUpdates);
        }
    }

    private class UnLikeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mDatabase.child("forum").child(forumType).child(postId).child("likeUidMap").child(mAuth.getUid()).removeValue();
        }
    }
}