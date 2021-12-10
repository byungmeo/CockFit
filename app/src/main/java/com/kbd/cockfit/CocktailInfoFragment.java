package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

public class CocktailInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cocktail_info, container, false);


        AppCompatButton listButton = v.findViewById(R.id.cock_listButton);
        AppCompatButton ingrdButton = v.findViewById(R.id.cock_ingrdButton);
        AppCompatButton equipButton = v.findViewById(R.id.cock_equipButton);
        AppCompatButton senseButton = v.findViewById(R.id.cock_senseButton);

        listButton.setOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("keyword", "every");
                context.startActivity(intent);
            }
        });
        ingrdButton.setOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, IngredientListActivity.class);
                context.startActivity(intent);
            }
        });
        equipButton.setOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, EquipmentListActivity.class);
                context.startActivity(intent);
            }
        });
        senseButton.setOnClickListener(new UtilitySet.OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommonSenseActivity.class);
                context.startActivity(intent);
            }
        });

        return v;
    }
}