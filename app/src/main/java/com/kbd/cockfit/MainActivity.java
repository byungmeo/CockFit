package com.kbd.cockfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private PanelFragment panelFragment = new PanelFragment();
    private MyRecipeFragment myRecipeFragment = new MyRecipeFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private CocktailInfoFragment cocktailInfoFragment = new CocktailInfoFragment();
    public static ArrayList<Recipe> recipeArrayList;
    public static ArrayList<HotRecipe> hotRecipeArrayList;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        //UI Thread를 방해하지 않도록 별도의 Thread에서 데이터를 로드합니다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadData(); //몇몇 데이터는 미리 로그인 시 불러옵니다.
            }
        }).start();

        progressBar = findViewById(R.id.main_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout, panelFragment).commitAllowingStateLoss();

                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

                toolbar = findViewById(R.id.topAppBarFragment);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.main_menuItem_profile) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
            }
        }, 2000);
    }

    public void loadData() {
        loadCommonRecipe();
        loadHotRecipe();
    }

    public void loadCommonRecipe() {
        try {
            if(recipeArrayList != null) {
                //이미 데이터를 불러왔으므로, 다시 불러오진 않습니다.
                return;
            }
            recipeArrayList = new ArrayList<>();

            String jsonData = UtilitySet.jsonToString(this, "jsons/basicRecipe.json");
            JSONArray jsonArray = new JSONArray(jsonData);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                int number = jo.getInt("number");
                String name = jo.getString("name");
                String proof = jo.getString("proof");
                String base = jo.getString("base");
                String[] ingredient = UtilitySet.jsonArrayToArray(jo.getJSONArray("ingredient"));
                String[] equipment = UtilitySet.jsonArrayToArray(jo.getJSONArray("equipment"));
                String description = jo.getString("description");
                String[] tags = UtilitySet.jsonArrayToArray(jo.getJSONArray("tags"));


                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/recipe/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                recipeArrayList.add(new Recipe(number, bitmap, name, proof, base, ingredient, equipment, description, tags));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHotRecipe() {
        if(hotRecipeArrayList != null) {
            return;
        }
        hotRecipeArrayList = new ArrayList<>();
        
        mDatabase.child("panel").child("hotRecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotRecipeArrayList.clear();

                for(DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                    HashMap<String, String> data = new HashMap<>();
                    data = (HashMap<String, String>) recipeSnapshot.getValue();

                    String postId = data.get("postId");
                    String recipeId = data.get("recipeId");
                    String writerUid = data.get("uid");
                    HotRecipeInfo hotRecipeInfo = new HotRecipeInfo(postId, recipeId, writerUid);

                    mStorage.child("Users").child(writerUid).child("CocktailImage").child(recipeId + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri imageUri = task.getResult();

                            mDatabase.child("forum").child("share").child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    RecipePost post = task.getResult().getValue(RecipePost.class);
                                    hotRecipeArrayList.add(new HotRecipe(post.getTitle(), imageUri, hotRecipeInfo, post));
                                }
                            });
                        }
                    });
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.page_panel:
                    transaction.replace(R.id.frameLayout, panelFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_myRecipe:
                    transaction.replace(R.id.frameLayout, myRecipeFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_community:
                    transaction.replace(R.id.frameLayout, communityFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_cocktailInfo:
                    transaction.replace(R.id.frameLayout, cocktailInfoFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //메인화면에서는 뒤로가기 버튼을 누르면 종료여부를 물어봅니다
        //super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finishAndRemoveTask();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}