package com.kbd.cockfit;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRecipeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onStart(){
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.make_button_backButton) {
            this.onBackPressed();
        } else if(view.getId() == R.id.make_button_store) {
            String name = ((EditText) findViewById(R.id.make_editText_name)).getText().toString();
            String proof = ((EditText) findViewById(R.id.make_editText_proof)).getText().toString();
            String base = ((EditText) findViewById(R.id.make_editText_base)).getText().toString();
            String[] ingredient = ((EditText) findViewById(R.id.make_editText_ingredient)).getText().toString().split(", ");
            String[] equipment = ((EditText) findViewById(R.id.make_editText_equipment)).getText().toString().split(", ");
            String description = ((EditText) findViewById(R.id.make_editText_description)).getText().toString();
            String[] tags = ((EditText) findViewById(R.id.make_editText_tags)).getText().toString().split(", ");

            if(name.equals("") || proof.equals("") || base.equals("") || ingredient.equals("") || equipment.equals("") || description.equals("") || tags.equals("")) {
                Toast.makeText(this , "모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }

            Recipe recipe = new Recipe(0, name,proof,base,ingredient,equipment,description,tags);

            mDatabase.child("user").child(uid).child("MyRecipe").push().setValue(recipe);

            this.onBackPressed();
        }
    }
}

