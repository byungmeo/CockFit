package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeRecipeActivity extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    EditText name = ((EditText) findViewById(R.id.make_editText_name));
    EditText proof = ((EditText) findViewById(R.id.make_editText_proof));
    EditText base = ((EditText) findViewById(R.id.make_editText_base));
    EditText ingredient = ((EditText) findViewById(R.id.make_editText_ingredient));
    EditText equipment = ((EditText) findViewById(R.id.make_editText_equipment));
    EditText description = ((EditText) findViewById(R.id.make_editText_description));
    Button storeButton = ((Button) findViewById(R.id.button12));
/*
    public void DataSet() {
        FirebaseUser user = mAuth.getCurrentUser(); //현재 로그인한 사람의 정보를 가져옴
        final String uid = user.getUid(); //uid는 계정 하나당 하나의 값을 가짐

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 변경이 감지가 되면 이 함수가 자동으로 콜백이 됩니다 이때 dataSnapashot 는 값을 내려 받을떄 사용함으로 지금은 쓰지 않습니
                mDatabase.child("user").child(uid).child("name").setValue("");
                mDatabase.child("user").child(uid).child("firstLunch").setValue("");
                //RealTimeDB는 기본적으로 parent , child , value 값으로 이루어져 있습니다 지금은 최초로 로그인한 사람의
                //색인을 만들고자 지금과 같은 작업을 하는 중입니다 즉 처음 들어오는 사람에게 DB자리를 내준다고 생각하시면됩니다
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // RealTimeDB와 통신 에러 등등 데이터를 정상적으로 받지 못할때 콜백함수로서 이곳으로 들어옵니다
            }
        });
    }*/

    public class Recipe {

        public String name = ""; //칵테일 이름
        public String proof = ""; //칵테일 도수
        public String base = ""; //칵테일 기주
        public String ingredient = ""; //칵테일 재료
        public String equipment = ""; //칵테일 장비
        public String description = ""; //칵테일 제조에 대한 상세설명


        public Recipe(String name, String proof, String base, String ingredient, String equipment, String description) {
            this.name = name;
            this.proof = proof;
            this.base = base;
            this.ingredient = ingredient;
            this.equipment = equipment;
            this.description = description;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                String name = (String) dataSnapshot.child("user").child(uid).child("name").getValue();
                // name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    protected void onStart() {
        super.onStart();
/*
        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mRootRef.child("user").child("recipe").child("name").setValue(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

/*
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.setValue(name.getText().toString());
                conditionRef.setValue(proof.getText().toString());
                conditionRef.setValue(base.getText().toString());
                conditionRef.setValue(ingredient.getText().toString());
                conditionRef.setValue(equipment.getText().toString());
                conditionRef.setValue(description.getText().toString());
            }
        });
    }*/


        /*public void recipeUpdate () {
            if (name.length() > 0 && description.length() > 0 && ingredient.length() > 0) {
                Recipe newrecipe = new Recipe(name, proof, base, ingredient, equipment, description);
                uploader(newrecipe);
            } else {
                startToast(msg '필수정보를 입력해주세요');
            }
        }*/

       /* public void onDataChange (Recipe newrecipe){


        }*/
    }


        @Override
        public void onBackPressed () {
            super.onBackPressed();
        }

        public void clickBackButton (View view){
            onBackPressed();
        }
    }
}
