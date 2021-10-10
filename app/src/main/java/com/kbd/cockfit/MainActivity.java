package com.kbd.cockfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MyRecipeFragment myRecipeFragment = new MyRecipeFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private CocktailInfoFragment cocktailInfoFragment = new CocktailInfoFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, myRecipeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int realDeviceHeight = displayMetrics.heightPixels;

        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        int bottomBarHeight = 0;
        int resourceIdBottom = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceIdBottom > 0) bottomBarHeight = getResources().getDimensionPixelSize(resourceIdBottom);

        int frameHeight = realDeviceHeight - statusBarHeight - bottomNavigationView.getHeight() - bottomBarHeight;
        FrameLayout fl = findViewById(R.id.frameLayout);
        fl.setLayoutParams(new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, frameHeight));
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.page_myRecipe:
                    transaction.replace(R.id.frameLayout, myRecipeFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_community:
                    transaction.replace(R.id.frameLayout, communityFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_cocktailInfo:
                    transaction.replace(R.id.frameLayout, cocktailInfoFragment).commitAllowingStateLoss();
                    break;
                case R.id.page_profile:
                    transaction.replace(R.id.frameLayout, profileFragment).commitAllowingStateLoss();
            }
            return true;
        }
    }
}