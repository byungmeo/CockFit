package com.kbd.cockfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MyPost> myPostArrayList;

    public MyPostAdapter(Context context, ArrayList<MyPost> myPostArrayList) {
        this.context = context;
        this.myPostArrayList = myPostArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypostitem_layout, parent, false);
        return new MyPostAdapter.MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyPostAdapter.MyPostViewHolder myPostViewHolder = (MyPostAdapter.MyPostViewHolder) holder;
        myPostViewHolder.postName.setText(myPostArrayList.get(position).getPostName());
        myPostViewHolder.postAbstract.setText(myPostArrayList.get(position).getPostAbstract());
        myPostViewHolder.postDate.setText(myPostArrayList.get(position).getPostDate());
    }

    @Override
    public int getItemCount() {
        return myPostArrayList.size();
    }

    public class MyPostViewHolder extends RecyclerView.ViewHolder {
        private TextView postName;
        private TextView postAbstract;
        private TextView postDate;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            postName = itemView.findViewById(R.id.myPostItem_textView_postName);
            postAbstract = itemView.findViewById(R.id.myPostItem_textView_postAbstract);
            postDate = itemView.findViewById(R.id.myPostItem_textView_postDate);
        }
    }
}