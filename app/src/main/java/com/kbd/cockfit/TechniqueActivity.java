package com.kbd.cockfit;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

public class TechniqueActivity extends AppCompatActivity {

    private ArrayList<Technique> techniqueArrayList;
    private RecyclerView techniqueRecycler;
    private TechniqueAdapter techniqueAdapter;
    private ArrayList<Technique> sorttechniqueList;
    private Toolbar techniquetoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technique);

        techniquetoolbar = findViewById(R.id.technique_materialToolbar);
        setSupportActionBar(techniquetoolbar);

        initTechniqueRecycler();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_etc_list, menu);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_sort_24);
        techniquetoolbar.setOverflowIcon(drawable);

        techniquetoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TechniqueActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void initTechniqueRecycler() {

        techniqueRecycler = findViewById(R.id.technique_recyclerView);
        techniqueArrayList = new ArrayList<>();
        sorttechniqueList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        techniqueRecycler.setLayoutManager(manager); // LayoutManager 등록
        try {
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicTechnique.json");
            JSONArray jsonArray = new JSONArray(jsonData);


            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");
                String description = jo.getString("description");


                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/commonsense/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                techniqueArrayList.add(new Technique(bitmap, name, description));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        sorttechniqueList = techniqueArrayList;
        techniqueAdapter = new TechniqueAdapter(this, sorttechniqueList);
        techniqueRecycler.setAdapter(techniqueAdapter);
    }

    public void clickButton(View view) {
        if(view.getId() == R.id.technique_button_backButton) {
            this.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.sortMenu_name_asc:
                Collections.sort(sorttechniqueList, new Comparator<Technique>() {
                    @Override
                    public int compare(Technique o1, Technique o2) {
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
                techniqueAdapter.notifyDataSetChanged();
                break;
            case R.id.sortMenu_name_desc:
                Collections.sort(sorttechniqueList, new Comparator<Technique>() {
                    @Override
                    public int compare(Technique o1, Technique o2) {
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
                 techniqueAdapter.notifyDataSetChanged();
                 break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class Technique {

        private Bitmap image;
        private String name;
        private String description;

        public Technique(Bitmap src, String name, String description) {
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
    public class TechniqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<Technique> techniqueArrayList = null;

        TechniqueAdapter(Context context, ArrayList<Technique> techniqueList) {
            this.techniqueArrayList=techniqueList;
            this.context=context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.descript_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new TechniqueViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            TechniqueViewHolder techniqueViewHolder = (TechniqueViewHolder) viewHolder;

            techniqueViewHolder.imageView.setImageBitmap(techniqueArrayList.get(position).getImage());
            techniqueViewHolder.name.setText(techniqueArrayList.get(position).getName());
            techniqueViewHolder.description.setText(techniqueArrayList.get(position).getDescription());


        }

        @Override
        public int getItemCount() {
            return techniqueArrayList.size();
        }

        //ViewHolder 구현
        public class TechniqueViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;
            TextView description;

            public TechniqueViewHolder(View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.descript_image);
                name=itemView.findViewById(R.id.descript_name);
                description=itemView.findViewById(R.id.descript_description);

            }

        }
    }
}