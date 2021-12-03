package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CommunityFragment extends Fragment {
    private View v;
    private Context context;
    private DatabaseReference mDatabase;
    private ArrayList<RecentForum> recentForumArrayList;
    private RecyclerView recentForumRecycler;
    private LinearLayoutManager layoutManager;
    private RecentAdapter recentAdapter;
    private HashMap<Post, String> postIdMap;

    //private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_community, container, false);
        context = v.getContext();

        //progressBar = v.findViewById(R.id.community_progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        recentForumRecycler = v.findViewById(R.id.RecyclerView_recentPost);

        mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recentForumRecycler.setHasFixedSize(true);
        recentForumRecycler.setLayoutManager(layoutManager);

        postIdMap = new HashMap<>();
        recentForumArrayList = new ArrayList<RecentForum>();
        recentAdapter = new RecentAdapter(recentForumArrayList);
        recentForumRecycler.setAdapter(recentAdapter);

        addRecentPost();

        return v;
    }

    //게시판을 새로 추가하려면 여기에 새로운 게시판의
    //firebase상의 이름을 추가해주세요.
    //또한 RecentAdapter상의 onBindViewHolder내에
    //switch구문도 적절하게 업데이트해주세요.
    enum ForumType {
        qa,
        share,
        general
    }

    public void addRecentPost() {
        ForumType[] forumTypes = ForumType.values();
        int done = 0;
        for(int i = 0; i < forumTypes.length; i++) {
            ForumType forumType = forumTypes[i];
            RecentForum recentForum = new RecentForum(forumType.name());

            mDatabase.child("forum").child(forumType.name()).limitToLast(4).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Post> recentPostArrayList = new ArrayList<>();
                    for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Post post = null;
                        if(forumType.equals(ForumType.share)) { post = postSnapshot.getValue(RecipePost.class); }
                        else { post = postSnapshot.getValue(Post.class); }
                        recentPostArrayList.add(post);
                        postIdMap.put(post, postSnapshot.getKey());
                    }

                    Collections.reverse(recentPostArrayList);
                    recentForum.setRecentPostArrayList(recentPostArrayList);
                    recentForumArrayList.add(recentForum);

                    recentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentPostViewHolder> {
        private ArrayList<RecentForum> forumArrayList;

        public RecentAdapter(ArrayList<RecentForum> forumArrayList) { this.forumArrayList = forumArrayList; }

        @NonNull
        @Override
        public RecentAdapter.RecentPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_item_layout, parent, false);
            return new RecentAdapter.RecentPostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentAdapter.RecentPostViewHolder holder, int position) {
            RecentForum forum = forumArrayList.get(holder.getAdapterPosition());
            String forumType = forum.getForumTitle();
            ArrayList<Post> recentPostArrayList = forum.getRecentPostArrayList();

            String forumTitle = null;
            switch (forumType) {
                case "share" : {
                    forumTitle = "레시피 공유 게시판";
                    break;
                }
                case "qa" : {
                    forumTitle = "질문 게시판";
                    break;
                }
                case "general" : {
                    forumTitle = "자유 게시판";
                    break;
                }
                default : {
                    forumTitle = "알 수 없는 게시판";
                }
            }
            holder.forumTitle.setText(forumTitle);

            Intent moreIntent = new Intent(context, ForumActivity.class);
            moreIntent.putExtra("forumType", forumType);
            holder.textButton_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(moreIntent);
                }
            });

            int maxIndex = 4;
            if(recentPostArrayList.size() < 4) {
                maxIndex = recentPostArrayList.size();
            }

            for(int i = 0; i < maxIndex; i++) {
                Post recentPost = recentPostArrayList.get(i);
                holder.title_post[i].setText(recentPost.getTitle());
                holder.date_post[i].setText(recentPost.getDate());
                holder.constraintLayouts[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent constraintIntent = new Intent(context, PostActivity.class);
                        constraintIntent.putExtra("post", recentPost);
                        constraintIntent.putExtra("postId", postIdMap.get(recentPost));
                        constraintIntent.putExtra("forumType", forumType);
                        startActivity(constraintIntent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return this.forumArrayList.size();
        }

        public class RecentPostViewHolder extends RecyclerView.ViewHolder {
            private TextView forumTitle;
            private TextView textButton_more;
            private TextView[] title_post;
            private TextView[] date_post;
            private ConstraintLayout[] constraintLayouts;

            public RecentPostViewHolder(@NonNull View itemView) {
                super(itemView);
                forumTitle = itemView.findViewById(R.id.forumItem_textView_title);
                textButton_more = itemView.findViewById(R.id.forumItem_textView_more);

                title_post = new TextView[4];
                title_post[0] = itemView.findViewById(R.id.textView_title_post1);
                title_post[1] = itemView.findViewById(R.id.textView_title_post2);
                title_post[2] = itemView.findViewById(R.id.textView_title_post3);
                title_post[3] = itemView.findViewById(R.id.textView_title_post4);

                date_post = new TextView[4];
                date_post[0] = itemView.findViewById(R.id.textView_date_post1);
                date_post[1] = itemView.findViewById(R.id.textView_date_post2);
                date_post[2] = itemView.findViewById(R.id.textView_date_post3);
                date_post[3] = itemView.findViewById(R.id.textView_date_post4);

                constraintLayouts = new ConstraintLayout[4];
                constraintLayouts[0] = itemView.findViewById(R.id.constraintLayout_post1);
                constraintLayouts[1] = itemView.findViewById(R.id.constraintLayout_post2);
                constraintLayouts[2] = itemView.findViewById(R.id.constraintLayout_post3);
                constraintLayouts[3] = itemView.findViewById(R.id.constraintLayout_post4);
            }
        }
    }
}