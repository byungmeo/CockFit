package com.kbd.cockfit;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MakeRecipeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String uid;
    String name;
    String proof;
    String base;
    String ingredient;
    String equipment;
    String description;
    Button storeButton;

    public class myRecipe {

        public String name = ""; //칵테일 이름
        public String proof = ""; //칵테일 도수
        public String base = ""; //칵테일 기주
        public String ingredient = ""; //칵테일 재료
        public String equipment = ""; //칵테일 장비
        public String description = ""; //칵테일 제조에 대한 상세설명

        public myRecipe(String name, String proof, String base, String ingredient, String equipment, String description )
        {
            this.name = name;
            this.proof = proof;
            this.base = base;
            this.ingredient = ingredient;
            this.equipment = equipment;
            this.description = description;
        }

        public String getName(){
            return name;
        }
        public String getProof(){
            return proof;
        }
        public String getBase(){
            return base;
        }
        public String getIngredient(){
            return ingredient;
        }
        public String getEquipment(){
            return equipment;
        }
        public String getDescription(){
            return description;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_recipe);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storeButton = ((Button) findViewById(R.id.button12));
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
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ((EditText) findViewById(R.id.make_editText_name)).getText().toString();
                proof = ((EditText) findViewById(R.id.make_editText_proof)).getText().toString();
                base = ((EditText) findViewById(R.id.make_editText_base)).getText().toString();
                ingredient = ((EditText) findViewById(R.id.make_editText_ingredient)).getText().toString();
                equipment = ((EditText) findViewById(R.id.make_editText_equipment)).getText().toString();
                description = ((EditText) findViewById(R.id.make_editText_description)).getText().toString();
                if( name.equals("")){
                    return;
                }

                if( description.equals("")){
                    return;
                }

                myRecipe recipe = new myRecipe(name,proof,base,ingredient,equipment,description);
                mDatabase.child("user").child(uid).child("MyRecipe").child(name).setValue(recipe);
            }
        });
    }

    @Override
        public void onBackPressed () {
            super.onBackPressed();
        }

        public void clickBackButton (View view){
            onBackPressed();
        }
    }

