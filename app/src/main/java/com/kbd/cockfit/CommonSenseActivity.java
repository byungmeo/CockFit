package com.kbd.cockfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CommonSenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_sense);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.commonsense_button_backButton) {
            this.onBackPressed();
        }
        else if(view.getId() == R.id.commonsense_technique_button) {
            Intent intent = new Intent(getApplicationContext(), TechniqueActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.commonsense_basespirit_button) {

        }
    }
}