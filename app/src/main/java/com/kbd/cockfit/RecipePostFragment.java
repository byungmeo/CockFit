package com.kbd.cockfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import java.util.List;

public class RecipePostFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String forumType;
    private String postId;
    private String recipeId;
    private String writerUid;

    private TextView textView_test;
    private TextView textView_recipeName;
    private TextView textView_tags;
    private TextView textView_proof;
    private TextView textView_base;
    private TextView textView_ingredient;
    private TextView textView_equipment;
    private TextView textView_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_post, container, false);

        //firebase initialize
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //getBundle
        Bundle bundle = getArguments();
        forumType = bundle.getString("forumType");
        postId = bundle.getString("postId");
        recipeId = bundle.getString("recipeId");
        writerUid = bundle.getString("writerUid");

        //view initialize
        textView_test = v.findViewById(R.id.recipePost_textView_test);
        textView_recipeName = v.findViewById(R.id.recipePost_textView_recipeName);
        textView_tags = v.findViewById(R.id.recipePost_textView_tag);
        textView_proof = v.findViewById(R.id.recipePost_textView_proof);
        textView_base = v.findViewById(R.id.recipePost_textView_base);
        textView_ingredient = v.findViewById(R.id.recipePost_textView_ingredient);
        textView_equipment = v.findViewById(R.id.recipePost_textView_equipment);
        textView_description = v.findViewById(R.id.recipePost_textView_description);

        textView_test.setText(recipeId);
        mDatabase.child("user").child(writerUid).child("MyRecipe").child(recipeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyRecipe recipe = snapshot.getValue(MyRecipe.class);

                if(recipe == null) {
                    return;
                }

                textView_recipeName.setText(recipe.getName());

                List<String> list = recipe.getTags();
                String totalText = "";
                for (String tag : list) {
                    totalText += tag + " ";
                }
                textView_tags.setText(totalText);
                textView_proof.setText(recipe.getProof());
                textView_base.setText(recipe.getBase());

                list = recipe.getIngredient();
                totalText = "";
                for (String ingredient : list) {
                    totalText += ingredient + " ";
                }
                textView_ingredient.setText(totalText);

                list = recipe.getEquipment();
                totalText = "";
                for (String equipment : list) {
                    totalText += equipment + " ";
                }
                textView_equipment.setText(totalText);
                textView_description.setText(recipe.getDescription());
            }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        return v;
    }
}