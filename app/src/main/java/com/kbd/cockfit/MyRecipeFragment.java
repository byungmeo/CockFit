package com.kbd.cockfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyRecipeFragment extends Fragment {
    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private StorageReference storageRef;
    private StorageReference dateRef;
    private RecyclerView myRecipeRecyclerView;
    private GridLayoutManager layoutManager;
    private MyRecipeAdapter adapter;
    private ArrayList<MyRecipe> myRecipeArrayList;
    private String uid;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        context = v.getContext();

        progressBar = v.findViewById(R.id.myRecipe_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        layoutManager = new GridLayoutManager(v.getContext(), 2);
        myRecipeRecyclerView = v.findViewById(R.id.myRecipe_recyclerView);
        myRecipeRecyclerView.setHasFixedSize(true);
        myRecipeRecyclerView.setLayoutManager(layoutManager);
        myRecipeRecyclerView.setVisibility(View.INVISIBLE);

        //myRecipeArrayList = new ArrayList<>();
        //adapter = new MyRecipeAdapter(myRecipeArrayList);
        //myRecipeRecyclerView.setAdapter(adapter);

        initMyRecipeList();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
            initMyRecipeList();
            progressBar.setVisibility(View.VISIBLE);
            myRecipeRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void initMyRecipeList() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();

        mDatabase.child("user").child(uid).child("MyRecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("test", "MyRecipeFragment : RealtimeDB 나만의레시피 onDataChange");
                if(myRecipeArrayList != null) {
                    myRecipeArrayList.clear();
                } else {
                    myRecipeArrayList = new ArrayList<>();
                }
                
                for (DataSnapshot recipeSnapshot: snapshot.getChildren()) {
                    MyRecipe recipe = recipeSnapshot.getValue(MyRecipe.class);
                    recipe.setMyRecipeId(recipeSnapshot.getKey());
                    myRecipeArrayList.add(recipe);
                }

                if(myRecipeArrayList.size() == 0){
                    progressBar.setVisibility(View.GONE);
                    myRecipeRecyclerView.setVisibility(View.VISIBLE);
                }

                adapter = new MyRecipeAdapter(myRecipeArrayList);
                myRecipeRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyRecipeLoad", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private class MyRecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_ITEM = 2;

        private ArrayList<MyRecipe> myRecipeArrayList;


        public MyRecipeAdapter(ArrayList<MyRecipe> myRecipeArrayList) {
            this.myRecipeArrayList = myRecipeArrayList;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout cardViewCocktailInfo;
            private TextView name;
            private ImageButton editRecipe;
            private ImageButton deleteRecipe;
            private ImageButton button_share;
            private ImageView cocktailPhoto;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                editRecipe = itemView.findViewById(R.id.myRecipeItem_button_edit);
                deleteRecipe = itemView.findViewById(R.id.myRecipeItem_button_delete);
                cardViewCocktailInfo = itemView.findViewById(R.id.cardView_Recipe_Info);
                button_share = itemView.findViewById(R.id.myRecipeItem_imageButton_share);
                name = itemView.findViewById(R.id.myRecipeItem_textView_name);
                cocktailPhoto = itemView.findViewById(R.id.myRecipeItem_imageView_picture);
            }
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            private CardView addCardView;

            public FooterViewHolder(@NonNull View itemView) {
                super(itemView);
                addCardView = itemView.findViewById(R.id.addMyRecipe_cardView);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("test", "MyRecipeFragment : onCreateViewHolder");
            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            if(viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myrecipeitem_layout, parent, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(view);

                itemViewHolder.cardViewCocktailInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RecipeActivity.class);
                        intent.putExtra("recipe", myRecipeArrayList.get(itemViewHolder.getAdapterPosition()));
                        context.startActivity(intent);
                    }
                });
                itemViewHolder.editRecipe.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        MyRecipe recipe = myRecipeArrayList.get(itemViewHolder.getAdapterPosition());
                        Context context = v.getContext();

                        //게시글 수정
                        Intent intent = new Intent(context, MakeRecipeActivity.class);
                        intent.putExtra("isEdit", true);
                        intent.putExtra("recipe", recipe);
                        intent.putExtra("recipeId", recipe.getMyRecipeId());
                        startActivity(intent);
                    }
                });
                itemViewHolder.deleteRecipe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyRecipe myRecipe = myRecipeArrayList.get(itemViewHolder.getAdapterPosition());

                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(myRecipe.getIsShare()) {
                                    //공유된 레시피일 경우 게시판에서도 삭제된다.
                                    mDatabase.child("forum").child("share").child(myRecipe.getSharePostId()).child("bookmarkUidMap").addValueEventListener(new ValueEventListener() {
                                        HashMap<String, String> bookmarkUidMap;
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            bookmarkUidMap = (HashMap<String, String>) snapshot.getValue();
                                            if(bookmarkUidMap != null) {
                                                for(String anotherUid : bookmarkUidMap.keySet()) {
                                                    mDatabase.child("user").child(anotherUid).child("bookmarkedPost").child(myRecipe.getSharePostId()).removeValue();
                                                }
                                            }
                                            mDatabase.child("user").child(uid).child("community").child("posting").child(myRecipe.getSharePostId()).removeValue();
                                            mDatabase.child("forum").child("share").child(myRecipe.getSharePostId()).removeValue();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });
                                }
                                //realtime에서 레시피 정보 삭제
                                String myRecipeId = myRecipe.getMyRecipeId();
                                myRecipeArrayList.remove(itemViewHolder.getAdapterPosition());
                                notifyItemRemoved(itemViewHolder.getAdapterPosition());
                                notifyItemRangeChanged(itemViewHolder.getAdapterPosition(), myRecipeArrayList.size());
                                mDatabase.child("user").child(uid).child("MyRecipe").child(myRecipeId).removeValue();

                                //storage에서 레시피 이미지 삭제
                                storageRef =  FirebaseStorage.getInstance().getReference();
                                dateRef = storageRef.child("Users/"+uid+"/CocktailImage/"+myRecipeId+".jpg");
                                dateRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("delete", "사진삭제");
                                    }
                                });
                                Toast.makeText(context, "삭제하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                        AlertDialog alert = alertdialog.create();
                        alert.setMessage("정말 레시피를 삭제하시겠습니까?");
                        alert.show();
                    }
                });
                itemViewHolder.button_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setPositiveButton("공유", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyRecipe myRecipe = myRecipeArrayList.get(itemViewHolder.getAdapterPosition());
                                if(myRecipe.getIsShare()) {
                                    Toast.makeText(context, "이미 공유된 레시피입니다!", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                                    RecipePost post = new RecipePost(myRecipe.getName(), user.getDisplayName(), uid, date);
                                    post.setRecipeId(myRecipe.getMyRecipeId());

                                    String postId = mDatabase.child("forum").child("share").push().getKey();
                                    mDatabase.child("forum").child("share").child(postId).setValue(post);

                                    HashMap<String, Object> update = new HashMap<>();
                                    update.put("isShare", new Boolean(true));
                                    update.put("sharePostId", postId);
                                    mDatabase.child("user").child(uid).child("MyRecipe").child(myRecipe.getMyRecipeId()).updateChildren(update);

                                    HashMap<String, String> value = new HashMap<>();
                                    value.put("ForumType", "share");
                                    mDatabase.child("user").child(uid).child("community").child("posting").child(postId).setValue(value);

                                    Intent intent = new Intent(context, PostActivity.class);
                                    intent.putExtra("forumT", "share");
                                    intent.putExtra("post", post);
                                    intent.putExtra("postId", postId);

                                    startActivity(intent);
                                }
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setMessage("레시피 공유 게시판에 공유 하시겠습니까?");
                        dialog.show();
                    }
                });
                return itemViewHolder;
            } else if(viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmyrecipe_layout, parent, false);
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("test", "MyRecipeFragment : onBindViewHolder");
            if(holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(myRecipeArrayList.get(holder.getAdapterPosition()).getName());
                storageRef =  FirebaseStorage.getInstance().getReference();
                dateRef = storageRef.child("Users/"+uid+"/CocktailImage/"+myRecipeArrayList.get(holder.getAdapterPosition()).getMyRecipeId()+".jpg");

                dateRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // Glide 이용하여 이미지뷰에 로딩
                            if(getActivity() == null) {
                                return;
                            }
                            Glide.with(MyRecipeFragment.this)
                                    .load(task.getResult())
                                    .into(itemViewHolder.cocktailPhoto);
                            progressBar.setVisibility(View.GONE);
                            myRecipeRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("test", "MyRecipeFragment -> onBindViewHolder -> getDownloadUrl : " + task.getException().getMessage());
                        }
                    }
                });
            } else if(holder instanceof FooterViewHolder) {
                FooterViewHolder footViewHolder = (FooterViewHolder) holder;
                footViewHolder.addCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MakeRecipeActivity.class);
                        context.startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return myRecipeArrayList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == myRecipeArrayList.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }
    }
}