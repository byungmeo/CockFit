package com.kbd.cockfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class RegisterActivity extends AppCompatActivity {

    private ConstraintLayout con;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        con=(ConstraintLayout)findViewById(R.id.register_layout_const);
        back = (ImageView)findViewById(R.id.register_button_backButton);

        final InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.hideSoftInputFromWindow(con.getWindowToken(),0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
