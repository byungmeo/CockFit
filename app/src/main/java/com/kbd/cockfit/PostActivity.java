package com.kbd.cockfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.post_button_backButton) {
            this.onBackPressed();
        }
    }
}