package com.kbd.cockfit;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IngredientListActivity extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientArrayList;
    private ArrayList<Ingredient> sortIngredientList;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);


        initIngredientRecycler();

    }

    public void initIngredientRecycler() {

        ingredientArrayList = new ArrayList<>();
        RecyclerView ingredientRecyclerView = (RecyclerView)findViewById(R.id.ingredient_list_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        ingredientRecyclerView.setLayoutManager(manager); // LayoutManager 등록
        ingredientRecyclerView.setAdapter(new IngredientAdapter(this, ingredientArrayList));  // Adapter 등록

        try {
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicIngredient.json");
            JSONArray jsonArray = new JSONArray(jsonData);


            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");
                String description = jo.getString("description");


                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/ingredient/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                ingredientArrayList.add(new Ingredient(bitmap, name, description));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        sortIngredientList = ingredientArrayList;
        ingredientAdapter = new IngredientAdapter(this, sortIngredientList);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.ingredient_list_button_backButton) {
            this.onBackPressed();
        }
        else if (view.getId() == R.id.ingredient_list_button_sort) {
            PopupMenu p = new PopupMenu(getApplicationContext(), view);
            getMenuInflater().inflate(R.menu.sort_popup, p.getMenu());
            p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.sortPopup_name_asc:
                            Collections.sort(sortIngredientList, new Comparator<Ingredient>() {
                                @Override
                                public int compare(Ingredient o1, Ingredient o2) {
                                    String name1 = o1.getName();
                                    String name2 = o2.getName();
                                    if (name1.compareTo(name2) > 0) {
                                        return 1;
                                    } else if (name1.compareTo(name2) == 0) {
                                        return 0;
                                    } else {
                                        return -1;
                                    }
                                }
                            });
                            ingredientAdapter.notifyDataSetChanged();
                            break;
                        case R.id.sortPopup_name_desc:
                            Collections.sort(sortIngredientList, new Comparator<Ingredient>() {
                                @Override
                                public int compare(Ingredient o1, Ingredient o2) {
                                    String name1 = o1.getName();
                                    String name2 = o2.getName();
                                    if (name1.compareTo(name2) < 0) {
                                        return 1;
                                    } else if (name1.compareTo(name2) == 0) {
                                        return 0;
                                    } else {
                                        return -1;
                                    }
                                }
                            });
                            ingredientAdapter.notifyDataSetChanged();
                            break;
                    }
                    return true;
                }
            });
            p.show(); // 메뉴를 띄우기
        }
    }
    public static class Ingredient {

        private Bitmap image;
        private String name;
        private String description;

        public Ingredient(Bitmap src, String name, String description) {
            this.image = src;
            this.name=name;
            this.description=description;
        }


        public Bitmap getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setImage(Bitmap src) {
            this.image = src;
        }

        public void setName(String name) {
            this.name=name;
        }

        public void setDescription(String description) {
            this.description=description;
        }

    }

    //Adapter
    public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<Ingredient> ingredientArrayList = null;

        IngredientAdapter(Context context, ArrayList<Ingredient> ingredientList) {
            this.ingredientArrayList=ingredientList;
            this.context=context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.descript_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new IngredientViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) viewHolder;

            ingredientViewHolder.imageView.setImageBitmap(ingredientArrayList.get(position).getImage());
            ingredientViewHolder.name.setText(ingredientArrayList.get(position).getName());
            ingredientViewHolder.description.setText(ingredientArrayList.get(position).getDescription());


        }

        @Override
        public int getItemCount() {
            return ingredientArrayList.size();
        }

        //ViewHolder 구현
        public class IngredientViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;
            TextView description;

            public IngredientViewHolder(View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.descript_image);
                name=itemView.findViewById(R.id.descript_name);
                description=itemView.findViewById(R.id.descript_description);

            }

        }
    }
}