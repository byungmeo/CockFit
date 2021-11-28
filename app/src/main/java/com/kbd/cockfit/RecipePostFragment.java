package com.kbd.cockfit;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.Map;

public class RecipePostFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String forumType;
    private String postId;
    private String recipeId;
    private String writerUid;
    private HashMap<String, String> likeUidMap;

    private ImageView imageView_picture;
    private TextView textView_recipeName;
    private TextView textView_tags;
    private TextView textView_proof;
    private TextView textView_base;
    private TextView textView_ingredient;
    private TextView textView_equipment;
    private TextView textView_description;
    private Button button_like;
    private Button button_bookmark;

    private Drawable drawable_alreadyLike;
    private Drawable drawable_waitLike;
    private Drawable drawable_alreadyBookmark;
    private Drawable drawable_waitBookmark;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private RecyclerView recycler_comments;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList;

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
        imageView_picture = v.findViewById(R.id.share_imageView_picture);
        textView_recipeName = v.findViewById(R.id.share_textView_recipeName);
        textView_tags = v.findViewById(R.id.share_textView_tag);
        textView_proof = v.findViewById(R.id.share_textView_proof);
        textView_base = v.findViewById(R.id.share_textView_base);
        textView_ingredient = v.findViewById(R.id.share_textView_ingredient);
        textView_equipment = v.findViewById(R.id.share_textView_equipment);
        textView_description = v.findViewById(R.id.share_textView_description);
        button_like = v.findViewById(R.id.share_button_like);
        button_bookmark = v.findViewById(R.id.share_button_bookmark);
        progressBar = v.findViewById(R.id.share_progressBar);
        linearLayout = v.findViewById(R.id.share_linearLayout);

        drawable_alreadyLike = getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24, getActivity().getTheme());
        drawable_waitLike = getResources().getDrawable(R.drawable.ic_outline_thumb_up_24, getActivity().getTheme());
        drawable_alreadyBookmark = getResources().getDrawable(R.drawable.ic_baseline_bookmark_true_24, getActivity().getTheme());
        drawable_waitBookmark = getResources().getDrawable(R.drawable.ic_baseline_bookmark_false_24, getActivity().getTheme());

        //
        recycler_comments = v.findViewById(R.id.share_recyclerView_comments);
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

        mDatabase.child("user").child(mAuth.getUid()).child("bookmarkedPost").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String checkText = snapshot.getValue(String.class);
                if(checkText != null) {
                    button_bookmark.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_alreadyBookmark, null, null, null);
                    button_bookmark.setOnClickListener(new UnBookmarkListener());
                } else {
                    button_bookmark.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_waitBookmark, null, null, null);
                    button_bookmark.setOnClickListener(new BookmarkListener());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("forum").child(forumType).child(postId).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                commentArrayList.clear();

                for(DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    comment.setCommentId(commentSnapshot.getKey());
                    commentArrayList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();

                if(commentArrayList.size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        //
        StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://cock-fit-ebaa7.appspot.com");
        mStorage.child("Users").child(writerUid).child("CocktailImage").child(recipeId + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(v)
                    .load(task.getResult())
                    .into(imageView_picture);
            }
        });

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

    private class BookmarkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mDatabase.child("user").child(mAuth.getUid()).child("bookmarkedPost").child(postId).setValue("true");
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(mAuth.getUid(), mAuth.getCurrentUser().getDisplayName());
            mDatabase.child("forum").child(forumType).child(postId).child("bookmarkUidMap").updateChildren(childUpdates);
        }
    }

    private class UnBookmarkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mDatabase.child("user").child(mAuth.getUid()).child("bookmarkedPost").child(postId).removeValue();
            mDatabase.child("forum").child(forumType).child(postId).child("bookmarkUidMap").child(mAuth.getUid()).removeValue();
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
            CommentAdapter.CommentViewHolder commentViewHolder = new CommentAdapter.CommentViewHolder(view);

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
            CommentAdapter.CommentViewHolder commentViewHolder = (CommentAdapter.CommentViewHolder) holder;
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
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //프로필 사진이 없는 댓글사용자
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
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