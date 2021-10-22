package com.kbd.cockfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CocktailInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cocktail_info, container, false);
        return v;
       // return inflater.inflate(R.layout.fragment_cocktail_info, container, false);
    }

    //TextView imageView = (TextView) getView().findViewById(R.id.textView9);
}