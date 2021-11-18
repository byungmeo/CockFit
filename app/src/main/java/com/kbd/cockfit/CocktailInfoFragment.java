package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

public class CocktailInfoFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cocktail_info, container, false);


        AppCompatImageButton listButton = v.findViewById(R.id.cock_listButton);
        AppCompatImageButton ingrdButton = v.findViewById(R.id.cock_ingrdButton);
        listButton.setOnClickListener(this);
        ingrdButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cock_listButton: {
                Context context = v.getContext();
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("keyword", "every");
                context.startActivity(intent);
                break;
            }
            case R.id.cock_ingrdButton: {
                Context context = v.getContext();
                Intent intent = new Intent(context, IngredientListActivity.class);
                intent.putExtra("keyword", "ingre");
                context.startActivity(intent);
                break;
            }
        }
    }
}