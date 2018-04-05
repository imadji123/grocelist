package com.imadji.grocelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroceryListActivity extends AppCompatActivity {
    private static final String TAG = GroceryListActivity.class.getSimpleName();

    private RecyclerView recyclerGroceries;
    private GroceryAdapter groceryAdapter;

    private List<Grocery> groceryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        recyclerGroceries = findViewById(R.id.recycler_groceries);
        groceryAdapter = new GroceryAdapter(this);
        recyclerGroceries.setAdapter(groceryAdapter);

    }

    private void showEmptyList() {

    }

    private void addGrocery(Grocery newGrocery) {

    }

    private void editGrocery(int position) {

    }

    private void deleteGrocery(int position) {

    }

    private void deleteAllGroceries() {

    }

}
