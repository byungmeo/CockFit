package com.kbd.cockfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private String uid;

    private Toolbar toolbar;
    private RecyclerView notification_recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter commentAdapter;
    private ArrayList<Notify> commentArrayList;
    private ImageButton button_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        toolbar = findViewById(R.id.notify_materialToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton button_delete = findViewById(R.id.notify_imageButton_delete);

        notification_recyclerView = findViewById(R.id.notification_recyclerView);
        notification_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        notification_recyclerView.setLayoutManager(layoutManager);

        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentArrayList, this);

        mDatabase.child("user").child(uid).child("notify").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notify notify = snapshot.getValue(Notify.class);
                    notify.setNotifyId(snapshot.getKey());
                    commentArrayList.add(notify);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("test", "에러발생");
            }
        });
        

        adapter = new CommentAdapter(commentArrayList, this);
        notification_recyclerView.setAdapter(adapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.setNavigationOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                NotificationActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        private ArrayList<Notify> commentArrayList;
        private Context context;

        public CommentAdapter(ArrayList<Notify> commentArrayList, Context context) {
            this.commentArrayList = commentArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_layout, parent, false);
            CommentViewHolder holder = new CommentViewHolder(view);



            holder.imageButton_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notify notify = commentArrayList.get(holder.getAdapterPosition());
                    mDatabase.child("user").child(uid).child("notify").child(notify.getNotifyId()).removeValue();
                    Toast.makeText(view.getContext() , "알림이 삭제되었습니다" , Toast.LENGTH_SHORT).show();

                    //삭제 후 새로고침
                    Intent intent = ((Activity)context).getIntent();
                    ((Activity)context).finish();
                    ((Activity)context).overridePendingTransition(0, 0);
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).overridePendingTransition(0, 0);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            CommentAdapter.CommentViewHolder commentViewHolder = (CommentAdapter.CommentViewHolder) holder;
            Notify notify = commentArrayList.get(holder.getAdapterPosition());



            commentViewHolder.textView_title.setText(notify.getTitle());
            commentViewHolder.textView_nickname.setText(notify.getNickname());
            commentViewHolder.textView_text.setText(notify.getText());
            try {
                commentViewHolder.textView_date.setText(UtilitySet.formatTimeString(notify.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


        @Override
        public int getItemCount() {
            return (commentArrayList != null ? commentArrayList.size() : 0 );
        }

        private class CommentViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView_profile;
            private TextView textView_nickname;
            private TextView textView_text;
            private TextView textView_date;
            private TextView textView_likeCount;
            private ImageButton imageButton_reply;
            private ImageButton imageButton_like;
            private ImageButton imageButton_more;
            private ImageButton imageButton_delete;
            private TextView textView_title;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                textView_title = itemView.findViewById(R.id.notify_textView_title);
                textView_nickname = itemView.findViewById(R.id.notify_textView_nickname);
                textView_text = itemView.findViewById(R.id.notify_textView_text);
                textView_date = itemView.findViewById(R.id.notify_textView_date);
                imageButton_delete = itemView.findViewById(R.id.notify_imageButton_delete);

                imageView_profile = itemView.findViewById(R.id.comment_imageView_profile);
                textView_likeCount = itemView.findViewById(R.id.comment_textView_likeCount);
                imageButton_reply = itemView.findViewById(R.id.comment_imageButton_reply);
                imageButton_like = itemView.findViewById(R.id.comment_imageButton_like);
                imageButton_more = itemView.findViewById(R.id.comment_imageButton_more);
            }
        }
    }
}