package com.kbd.cockfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipePostFragment extends Fragment {
    private String forumType;
    private String postId;
    private String recipeId;

    private TextView textView_contents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_post, container, false);

        //getBundle
        Bundle bundle = getArguments();
        forumType = bundle.getString("forumType");
        postId = bundle.getString("postId");
        recipeId = bundle.getString("recipeId");

        //view initialize
        textView_contents = v.findViewById(R.id.recipePost_textView_test);

        textView_contents.setText(recipeId);

        return v;
    }
}