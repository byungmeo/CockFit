package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_community, container, false);

        TextView shareMore = v.findViewById(R.id.community_textView_shareMore);
        TextView qaMore = v.findViewById(R.id.community_textView_qaMore);
        TextView generalMore = v.findViewById(R.id.community_textView_generalMore);

        shareMore.setOnClickListener(this);
        qaMore.setOnClickListener(this);
        generalMore.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, ForumActivity.class);
        switch (v.getId()) {
            case R.id.community_textView_qaMore: {
                //더보기 버튼
                intent.putExtra("forum", "qa");
                break;
            }
            case R.id.community_textView_shareMore: {
                intent.putExtra("forum", "share");
                break;
            }
            case R.id.community_textView_generalMore: {
                intent.putExtra("forum", "general");
                break;
            }
            default: {
                break;
            }
        }
        context.startActivity(intent);
    }
}