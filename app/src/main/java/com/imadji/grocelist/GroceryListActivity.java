package com.imadji.grocelist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = GroceryListActivity.class.getSimpleName();

    private TextView textNoGroceries;
    private RecyclerView recyclerGroceries;
    private GroceryAdapter groceryAdapter;
    private FloatingActionButton fabAddGrocery;

    private List<Grocery> groceryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textNoGroceries = findViewById(R.id.text_no_groceries);
        groceryAdapter = new GroceryAdapter(this);
        recyclerGroceries = findViewById(R.id.recycler_groceries);
        recyclerGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroceries.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerGroceries.setAdapter(groceryAdapter);

        fabAddGrocery = findViewById(R.id.fab_add_grocery);
        fabAddGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDummyGrocery();
                refreshGroceryList();
                recyclerGroceries.getLayoutManager().scrollToPosition(0);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshGroceryList();
    }

    private void showEmptyList() {
        if (textNoGroceries.getVisibility() == View.GONE) {
            textNoGroceries.setVisibility(View.VISIBLE);
            recyclerGroceries.setVisibility(View.GONE);
        }
    }

    private void showGroceryList() {
        if (recyclerGroceries.getVisibility() == View.GONE) {
            textNoGroceries.setVisibility(View.GONE);
            recyclerGroceries.setVisibility(View.VISIBLE);
        }
    }

    private void refreshGroceryList() {
        if (groceryList.size() == 0) {
            showEmptyList();
            return;
        }

        showGroceryList();
    }

    private void addGrocery(Grocery newGrocery) {
        groceryList.add(0, newGrocery);
        groceryAdapter.addItem(0, newGrocery);
    }

    private void editGrocery(int position, Grocery editedGrocery) {
        groceryList.set(position, editedGrocery);
        groceryAdapter.editItem(position, editedGrocery);
    }

    private void deleteGrocery(int position) {
        groceryList.remove(position);
        groceryAdapter.removeItem(position);
    }

    private void deleteAllGroceries() {
        groceryList.clear();
        groceryAdapter.clearAllItems();
    }

    private void createDummyGrocery() {
        String[] groceryNames = {"Milk", "Eggs", "Bread", "Apple", "Yogurt", "Tea", "Cereal"};
        Grocery grocery = new Grocery(groceryNames[generateRandomNumber(0, 6)],
                generateRandomNumber(1, 10), "Lorem ipsum dolor sit amet, at sit commodo " +
                "nominavi abhorreant, ius in epicuri sensibus laboramus.");
        addGrocery(grocery);
    }

    private int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

}
