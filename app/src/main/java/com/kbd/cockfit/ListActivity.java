package com.kbd.cockfit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recipeRecycler;
    private RecipeAdapter recipeAdapter;
    //private ArrayList<Recipe> recipeArrayList;
    private ArrayList<Recipe> sortRecipeList;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        toolbar = findViewById(R.id.list_materialToolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.list_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        keyword = getIntent().getStringExtra("keyword");

        initRecipeRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_list, menu);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_sort_24);
        toolbar.setOverflowIcon(drawable);

        if(keyword.equals("every")) {
            toolbar.setTitle("?????? ????????? ??????");
        } else if(keyword.equals("favorite")) {
            toolbar.setTitle("??????????????? ????????? ??????");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.this.onBackPressed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void initRecipeRecycler() {
        if(MainActivity.recipeArrayList == null) {
            Toast.makeText(this, "???????????? ??????????????? ?????????????????????.\n??????????????? ??????????????????.", Toast.LENGTH_SHORT);
            onBackPressed();
            return;
        }

        recipeRecycler = findViewById(R.id.list_recycler);
        sortRecipeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recipeRecycler.setLayoutManager(linearLayoutManager);

        if(keyword.equals("every")) {
            sortRecipeList = MainActivity.recipeArrayList;
            recipeAdapter = new RecipeAdapter(this, sortRecipeList);
            recipeRecycler.setAdapter(recipeAdapter);
            progressBar.setVisibility(View.GONE);
        } else if(keyword.equals("favorite")) {
            loadFavoriteRecipeList();
        }
    }

    public void loadFavoriteRecipeList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://cock-fit-ebaa7-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        String uid = mAuth.getUid();
        ArrayList<Recipe> favoriteRecipeList = new ArrayList<>();

        mDatabase.child("user").child(uid).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteRecipeList.clear();
                for(DataSnapshot numberSnapshot : snapshot.getChildren()) {
                    int recipeNumber = numberSnapshot.getValue(int.class);
                    for(Recipe recipe : MainActivity.recipeArrayList) {
                        if(recipe.getNumber() == recipeNumber) {
                            favoriteRecipeList.add(recipe);
                            break;
                        }
                    }
                }
                sortRecipeList = favoriteRecipeList;
                recipeAdapter = new RecipeAdapter(ListActivity.this, sortRecipeList);
                recipeRecycler.setAdapter(recipeAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.list_button_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        recipeAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
            case R.id.sortMenu_proof_desc:
                Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe o1, Recipe o2) {
                        int proof1 = Integer.parseInt(o1.getProof());
                        int proof2 = Integer.parseInt(o2.getProof());

                        if (proof1 < proof2) { return 1; }
                        else if (proof1 == proof2) { return 0; }
                        else { return -1; }
                    }
                });
                recipeAdapter.notifyDataSetChanged();
                break;
            case R.id.sortMenu_proof_asc:
                Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe o1, Recipe o2) {
                        int proof1 = Integer.parseInt(o1.getProof());
                        int proof2 = Integer.parseInt(o2.getProof());
                        if (proof1 > proof2) {
                            return 1;
                        } else if (proof1 == proof2) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
                recipeAdapter.notifyDataSetChanged();
                break;
            case R.id.sortMenu_name_asc:
                Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe o1, Recipe o2) {
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
                recipeAdapter.notifyDataSetChanged();
                break;
            case R.id.sortMenu_name_desc:
                Collections.sort(sortRecipeList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe o1, Recipe o2) {
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
                recipeAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

        private Context context;
        private ArrayList<Recipe> recipeArrayList;
        private ArrayList<Recipe> recipeArrayListFull;

        public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
            this.context = context;
            this.recipeArrayList = recipeArrayList;
            this.recipeArrayListFull = new ArrayList<>(recipeArrayList);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipeitem_layout, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

            recipeViewHolder.name.setText(recipeArrayList.get(position).getName());
            recipeViewHolder.base.setText("????????? : ");
            recipeViewHolder.tags.setText("?????? : ");
            recipeViewHolder.proof.setText("?????? : ");

            String baseText = recipeViewHolder.base.getText().toString();
            String tagsText = recipeViewHolder.tags.getText().toString();
            baseText += (recipeArrayList.get(position).getBase());
            for(String tag : recipeArrayList.get(position).getTags()) {
                tagsText += tag + " ";
            }
            String proofText = recipeViewHolder.proof.getText().toString();
            proofText += (recipeArrayList.get(position).getProof());

            recipeViewHolder.image.setImageBitmap(recipeArrayList.get(position).getImage());
            recipeViewHolder.base.setText(baseText);
            recipeViewHolder.tags.setText(tagsText);
            recipeViewHolder.proof.setText(proofText);

            recipeViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeActivity.class);
                    intent.putExtra("recipe_number", recipeArrayList.get(holder.getAdapterPosition()).getNumber());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipeArrayList.size();
        }

        @Override
        public Filter getFilter() {
            return recipeFilter;
        }

        private Filter recipeFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Recipe> filteredList = new ArrayList<>();

                if(constraint == null || constraint.length() == 0) {
                    filteredList.addAll(recipeArrayListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for(Recipe recipe : recipeArrayListFull) {
                        if(recipe.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(recipe);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                recipeArrayList.clear();
                recipeArrayList.addAll((ArrayList) results.values);
                sortRecipeList = recipeArrayList;
                notifyDataSetChanged();
            }
        };

        public class RecipeViewHolder extends RecyclerView.ViewHolder {
            private CardView card;
            private TextView name;
            private TextView base;
            private TextView tags;
            private TextView proof;
            private ImageView image;

            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                card = itemView.findViewById(R.id.recipeitem_cardView);
                name = itemView.findViewById(R.id.recipeitem_name);
                base = itemView.findViewById(R.id.recipeitem_base);
                tags = itemView.findViewById(R.id.recipeitem_tags);
                proof = itemView.findViewById(R.id.recipeitem_proof);
                image = itemView.findViewById(R.id.recipeitem_image);
            }
        }
    }
}