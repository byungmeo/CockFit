package com.kbd.cockfit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CommonSenseActivity extends AppCompatActivity {
    private Toolbar commonsensetoolbar;
    private Long mLastClickTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_sense);

        commonsensetoolbar = findViewById(R.id.commonsense_materialToolbar);
        setSupportActionBar(commonsensetoolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        commonsensetoolbar.setNavigationOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CommonSenseActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void clickButton(View view) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        if(elapsedTime > 600) {
            if (view.getId() == R.id.commonsense_technique_button) {
                Intent intent = new Intent(getApplicationContext(), TechniqueActivity.class);
                startActivity(intent);
            } else if (view.getId() == R.id.commonsense_basespirit_button) {
                Intent intent = new Intent(getApplicationContext(), BaseSpiritActivity.class);
                startActivity(intent);
            }
        }
    }
}