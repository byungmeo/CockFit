package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class CommunityFragment extends Fragment {
    private View v;
    private Context context;
    private DatabaseReference mDatabase;
    private ArrayList<RecentForum> recentForumArrayList;
    private RecyclerView recentForumRecycler;
    private LinearLayoutManager layoutManager;
    private RecentAdapter recentAdapter;

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

        /*
        TextView shareMore = v.findViewById(R.id.community_textView_shareMore);
        TextView qaMore = v.findViewById(R.id.community_textView_qaMore);
        TextView generalMore = v.findViewById(R.id.community_textView_generalMore);

        shareMore.setOnClickListener(this);
        qaMore.setOnClickListener(this);
        generalMore.setOnClickListener(this);

        loadRecentPost(ForumType.Share);
        loadRecentPost(ForumType.QA);
        loadRecentPost(ForumType.General);
        */
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recentForumRecycler.setHasFixedSize(true);
        recentForumRecycler.setLayoutManager(layoutManager);

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
                        if(forumType.equals(ForumType.share)) recentPostArrayList.add(postSnapshot.getValue(RecipePost.class));
                        else recentPostArrayList.add(postSnapshot.getValue(Post.class));
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

            Intent intent = new Intent(context, ForumActivity.class);
            intent.putExtra("forumType", forumType);
            holder.textButton_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(intent);
                }
            });

            for(int i = 0; i < 4; i++) {
                Post recentPost = recentPostArrayList.get(i);
                holder.title_post[i].setText(recentPost.getTitle());
                holder.date_post[i].setText(recentPost.getDate());
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
            }
        }
    }

    /*
    public void loadRecentPost(ForumType forumType) {
        String type = null;
        switch (forumType) {
            case Share:
                type = "share";
                break;
            case QA:
                type = "qa";
                break;
            case General:
                type = "general";
                break;
            default:
                break;
        }
        if(type == null) {
            Log.d("error", "게시판유형 식별 실패");
            return;
        }

        String finalType = type; //익명클래스 참조 문제
        mDatabase.child("forum").child(type).limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long i = 4;
                long postNum = snapshot.getChildrenCount();

                if(postNum == 0) {
                    String _titleViewTag = "textView_" + finalType + 1;
                    String _dateViewTag = _titleViewTag + "_date";

                    TextView title = v.findViewWithTag(_titleViewTag);
                    TextView date = v.findViewWithTag(_dateViewTag);

                    title.setText("");
                    date.setText("");

                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //최근 표시 게시물을 표시할 때, 실시간으로 최근 게시물이 삭제될 경우
                //똑같은 게시판이 2개 표시되는 현상을 방지합니다.
                if(postNum < 4) {
                    for(long index = 4; index > postNum; index--) {
                        String _titleViewTag = "textView_" + finalType + String.valueOf(index);
                        String _dateViewTag = _titleViewTag + "_date";

                        TextView title = v.findViewWithTag(_titleViewTag);
                        TextView date = v.findViewWithTag(_dateViewTag);

                        title.setText("");
                        date.setText("");
                    }
                    i = postNum;
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String titleViewTag = "textView_" + finalType + String.valueOf(i);
                    String dateViewTag = titleViewTag + "_date";
                    String constraintTag = "constraintLayout_" + finalType + String.valueOf(i);

                    TextView title = v.findViewWithTag(titleViewTag);
                    TextView date = v.findViewWithTag(dateViewTag);
                    ConstraintLayout constraintLayout = v.findViewWithTag(constraintTag);

                    Post post;
                    if(finalType.equals("share")) {
                        post = postSnapshot.getValue(RecipePost.class);
                    } else {
                        post = postSnapshot.getValue(Post.class);
                    }

                    title.setText(post.getTitle());
                    try {
                        date.setText(UtilitySet.formatTimeString(post.getDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    constraintLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), PostActivity.class);
                            intent.putExtra("post", post);
                            intent.putExtra("postId", postSnapshot.getKey());
                            intent.putExtra("forumType", finalType);
                            startActivity(intent);
                        }
                    });
                    i--;
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, ForumActivity.class);
        switch (v.getId()) {
            case R.id.community_textView_qaMore: {
                //더보기 버튼
                intent.putExtra("forumType", "qa");
                break;
            }
            case R.id.community_textView_shareMore: {
                intent.putExtra("forumType", "share");
                break;
            }
            case R.id.community_textView_generalMore: {
                intent.putExtra("forumType", "general");
                break;
            }
            default: {
                break;
            }
        }
        context.startActivity(intent);
    }
    */
}