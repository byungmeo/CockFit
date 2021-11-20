package com.kbd.cockfit;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
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
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;

    private Drawable drawable_alreadyLike;
    private Drawable drawable_waitLike;

    private RecyclerView recycler_comments;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_general_post, container, false);

        constraintLayout = v.findViewById(R.id.general_constraintLayout_comments);
        progressBar = v.findViewById(R.id.general_progressBar);
        constraintLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


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

        drawable_alreadyLike = getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24, getActivity().getTheme());
        drawable_waitLike = getResources().getDrawable(R.drawable.ic_outline_thumb_up_24, getActivity().getTheme());

        //
        recycler_comments = v.findViewById(R.id.general_recycler_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycler_comments.setLayoutManager(linearLayoutManager);
        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentArrayList);
        recycler_comments.setAdapter(commentAdapter);
        //

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

        mDatabase.child("forum").child(forumType).child(postId).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.INVISIBLE);
                commentArrayList.clear();

                for(DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    comment.setCommentId(commentSnapshot.getKey());
                    commentArrayList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();

                if(commentArrayList.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
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

    private class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<Comment> commentArrayList;

        CommentAdapter(ArrayList<Comment> commentArrayList) {
            this.commentArrayList = commentArrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
            CommentViewHolder commentViewHolder = new CommentViewHolder(view);

            commentViewHolder.imageButton_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            commentViewHolder.imageButton_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            commentViewHolder.imageButton_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View dialogView = View.inflate(getContext(), R.layout.comment_dialog_layout, null);
                    Button button_delete = dialogView.findViewById(R.id.commentDialog_button_delete);
                    Button button_edit = dialogView.findViewById(R.id.commentDialog_button_edit);

                    dialogBuilder.setView(dialogView);
                    AlertDialog alertDialog = dialogBuilder.create();

                    button_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Comment comment = commentArrayList.get(commentViewHolder.getAdapterPosition());
                            mDatabase.child("forum").child(forumType).child(postId).child("comments").child(comment.getCommentId()).removeValue();
                            alertDialog.dismiss();
                        }
                    });

                    button_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            });

            return commentViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            Comment comment = commentArrayList.get(holder.getAdapterPosition());

            if(!comment.getUid().equals(mAuth.getUid())) {
                commentViewHolder.imageButton_more.setVisibility(View.INVISIBLE);
            }
            StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
            mStorage.child("Users").child(comment.getUid()).child("profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //프로필 사진이 있는 댓글사용자

                    Glide.with(getContext())
                            .load(uri)
                            .into(commentViewHolder.imageView_profile);

                    if(holder.getAdapterPosition() == getItemCount() - 1) {
                        progressBar.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //프로필 사진이 없는 댓글사용자
                    progressBar.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            });

            commentViewHolder.textView_nickname.setText(comment.getNickname());
            commentViewHolder.textView_text.setText(comment.getText());
            try {
                commentViewHolder.textView_date.setText(UtilitySet.formatTimeString(comment.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(comment.getLikeUidMap() != null) {
                commentViewHolder.textView_likeCount.setText(comment.getLikeUidMap().size());
            } else {
                commentViewHolder.textView_likeCount.setText("0");
            }
        }

        @Override
        public int getItemCount() {
            return commentArrayList.size();
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

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView_profile = itemView.findViewById(R.id.comment_imageView_profile);
                textView_nickname = itemView.findViewById(R.id.comment_textView_nickname);
                textView_text = itemView.findViewById(R.id.comment_textView_text);
                textView_date = itemView.findViewById(R.id.comment_textView_date);
                textView_likeCount = itemView.findViewById(R.id.comment_textView_likeCount);
                imageButton_reply = itemView.findViewById(R.id.comment_imageButton_reply);
                imageButton_like = itemView.findViewById(R.id.comment_imageButton_like);
                imageButton_more = itemView.findViewById(R.id.comment_imageButton_more);
            }
        }
    }
}