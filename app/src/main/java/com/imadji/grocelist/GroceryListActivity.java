package com.imadji.grocelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = GroceryListActivity.class.getSimpleName();

    private TextView textNoGroceries;
    private RecyclerView recyclerGroceries;
    private GroceryAdapter groceryAdapter;

    private List<Grocery> groceryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        textNoGroceries = findViewById(R.id.text_no_groceries);
        groceryAdapter = new GroceryAdapter(this);
        recyclerGroceries = findViewById(R.id.recycler_groceries);
        recyclerGroceries.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerGroceries.setAdapter(groceryAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshGroceryList();
    }

    private void showEmptyList() {
        textNoGroceries.setVisibility(View.VISIBLE);
        recyclerGroceries.setVisibility(View.GONE);
    }

    private void showGroceryList() {
        textNoGroceries.setVisibility(View.GONE);
        recyclerGroceries.setVisibility(View.VISIBLE);
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

}
