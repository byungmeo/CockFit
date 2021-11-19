package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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

public class EquipmentListActivity extends AppCompatActivity {

    private ArrayList<Equipment> equipmentArrayList;
    private ArrayList<Equipment> sortEquipmentList;
    private EquipmentAdapter equipmentAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);
        toolbar = findViewById(R.id.etc_list_materialToolbar);
        setSupportActionBar(toolbar);

        initEquipmentRecycler();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_etc_list, menu);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_sort_24);
        toolbar.setOverflowIcon(drawable);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquipmentListActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void initEquipmentRecycler() {

        equipmentArrayList = new ArrayList<>();
        RecyclerView equipmentRecyclerView = (RecyclerView)findViewById(R.id.equipment_list_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        equipmentRecyclerView.setLayoutManager(manager); // LayoutManager 등록
        equipmentRecyclerView.setAdapter(new EquipmentAdapter(this, equipmentArrayList));  // Adapter 등록

        try {
            String jsonData = UtilitySet.jsonToString(this, "jsons/basicEquipment.json");
            JSONArray jsonArray = new JSONArray(jsonData);


            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String name = jo.getString("name");
                String description = jo.getString("description");


                AssetManager assetManager = this.getResources().getAssets();
                InputStream is = assetManager.open("image/equipment/" + name.replace(" ", "_") + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                equipmentArrayList.add(new Equipment(bitmap, name, description));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        sortEquipmentList = equipmentArrayList;
        equipmentAdapter = new EquipmentAdapter(this, sortEquipmentList);
        equipmentRecyclerView.setAdapter(equipmentAdapter);

    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.sortMenu_name_asc:
                Collections.sort(sortEquipmentList, new Comparator<Equipment>() {
                    @Override
                    public int compare(Equipment o1, Equipment o2) {
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
                equipmentAdapter.notifyDataSetChanged();

                break;
            case R.id.sortMenu_name_desc:
                Collections.sort(sortEquipmentList, new Comparator<Equipment>() {
                    @Override
                    public int compare(Equipment o1, Equipment o2) {
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
                equipmentAdapter.notifyDataSetChanged();

                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public static class Equipment {

        private Bitmap image;
        private String name;
        private String description;

        public Equipment(Bitmap src, String name, String description) {
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
    public class EquipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<Equipment> equipmentArrayList = null;

        EquipmentAdapter(Context context, ArrayList<Equipment> equipmentList) {
            this.equipmentArrayList=equipmentList;
            this.context=context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
            View view = inflater.inflate(R.layout.descript_layout, parent, false);
            RecyclerView.ViewHolder viewHolder = new EquipmentViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            EquipmentViewHolder equipmentViewHolder = (EquipmentViewHolder) viewHolder;

            equipmentViewHolder.imageView.setImageBitmap(equipmentArrayList.get(position).getImage());
            equipmentViewHolder.name.setText(equipmentArrayList.get(position).getName());
            equipmentViewHolder.description.setText(equipmentArrayList.get(position).getDescription());


        }

        @Override
        public int getItemCount() {
            return equipmentArrayList.size();
        }

        //ViewHolder 구현
        public class EquipmentViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;
            TextView description;

            public EquipmentViewHolder(View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.descript_image);
                name=itemView.findViewById(R.id.descript_name);
                description=itemView.findViewById(R.id.descript_description);

            }

        }
    }
}