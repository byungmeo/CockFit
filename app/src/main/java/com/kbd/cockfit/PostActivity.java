package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {
    private Post post;

    private TextView textView_title;
    private TextView textView_writer;
    private TextView textView_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        post = getIntent().getParcelableExtra("post");

        textView_title = findViewById(R.id.post_textView_title);
        textView_writer = findViewById(R.id.post_textView_writer);
        textView_content = findViewById(R.id.post_textView_content);

        textView_title.setText(post.getTitle());
        textView_writer.setText(post.getWriter());
        textView_content.setText(post.getContent());
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_backButton) {
            this.onBackPressed();
        }
    }
}