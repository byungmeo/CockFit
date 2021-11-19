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

public class BaseSpiritActivity extends AppCompatActivity {

    private ArrayList<BaseSpirit> basespiritArrayList;
    private RecyclerView basespiritRecycler;
    private BaseSpiritAdapter basespiritAdapter;
    private ArrayList<BaseSpirit> sortbasespiritList;
    private Toolbar basespirittoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_spirit);

        basespirittoolbar = findViewById(R.id.basespirit_materialToolbar);
        setSupportActionBar(basespirittoolbar);


        initBaseSpiritRecycler();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_etc_list, menu);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_sort_24);
        basespirittoolbar.setOverflowIcon(drawable);

        basespirittoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseSpiritActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void initBaseSpiritRecycler() {

        basespiritRecycler = findViewById(R.id.basespirit_recyclerView);
        basespiritArrayList = new ArrayList<>();
        sortbasespiritList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        basespiritRecycler.setLayoutManager(manager); // LayoutManager 등록

        try {
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicBaseSpirit.json");
            JSONArray jsonArray = new JSONArray(jsonData);


            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");
                String description = jo.getString("description");


                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/commonsense/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                basespiritArrayList.add(new BaseSpirit(bitmap, name, description));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        sortbasespiritList = basespiritArrayList;
        basespiritAdapter = new BaseSpiritAdapter(this, sortbasespiritList);
        basespiritRecycler.setAdapter(basespiritAdapter);

    }


    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.sortMenu_name_asc:
                Collections.sort(sortbasespiritList, new Comparator<BaseSpirit>() {
                    @Override
                    public int compare(BaseSpirit o1, BaseSpirit o2) {
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
                basespiritAdapter.notifyDataSetChanged();
                break;
            case R.id.sortMenu_name_desc:
                Collections.sort(sortbasespiritList, new Comparator<BaseSpirit>() {
                    @Override
                    public int compare(BaseSpirit o1, BaseSpirit o2) {
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
                basespiritAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class BaseSpirit {

        private Bitmap image;
        private String name;
        private String description;

        public BaseSpirit(Bitmap src, String name, String description) {
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
    public class BaseSpiritAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<BaseSpirit> basespiritArrayList = null;

        BaseSpiritAdapter(Context context, ArrayList<BaseSpirit> basespiritList) {
            this.context = context;
            this.basespiritArrayList=basespiritList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.descript_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new BaseSpiritViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            BaseSpiritViewHolder basespiritViewHolder = (BaseSpiritViewHolder) viewHolder;

            basespiritViewHolder.imageView.setImageBitmap(basespiritArrayList.get(position).getImage());
            basespiritViewHolder.name.setText(basespiritArrayList.get(position).getName());
            basespiritViewHolder.description.setText(basespiritArrayList.get(position).getDescription());


        }

        @Override
        public int getItemCount() {
            return basespiritArrayList.size();
        }

        //ViewHolder 구현
        public class BaseSpiritViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;
            TextView description;

            public BaseSpiritViewHolder(View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.descript_image);
                name=itemView.findViewById(R.id.descript_name);
                description=itemView.findViewById(R.id.descript_description);

            }

        }
    }
}