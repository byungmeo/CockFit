package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button favorite = v.findViewById(R.id.profile_button_recipe);
        Button post = v.findViewById(R.id.profile_button_post);
        Button comment = v.findViewById(R.id.profile_button_comment);

        favorite.setOnClickListener(this);
        post.setOnClickListener(this);
        comment.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId()) {
            case R.id.profile_button_recipe: {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("keyword", "favorite");
                context.startActivity(intent);
                break;
            }
            case R.id.profile_button_post: {
                Intent intent = new Intent(context, ForumActivity.class);
                intent.putExtra("forum", "myPost");
                context.startActivity(intent);
                break;
            }
            case R.id.profile_button_comment: {
                //작성한 댓글
            }
        }
    }
}


