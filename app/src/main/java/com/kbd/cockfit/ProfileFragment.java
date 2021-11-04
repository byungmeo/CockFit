package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView editText_nickname;

    private Button button_myFavorite;
    private Button button_myPost;
    private Button button_myComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        editText_nickname = v.findViewById(R.id.profile_textView_name);
        mDatabase.child("user").child(mAuth.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    editText_nickname.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        button_myFavorite = v.findViewById(R.id.profile_button_recipe);
        button_myPost = v.findViewById(R.id.profile_button_post);
        button_myComment = v.findViewById(R.id.profile_button_comment);

        button_myFavorite.setOnClickListener(this);
        button_myPost.setOnClickListener(this);
        button_myComment.setOnClickListener(this);

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