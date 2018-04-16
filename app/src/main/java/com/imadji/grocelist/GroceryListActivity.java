package com.imadji.grocelist;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.imadji.grocelist.helper.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroceryListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.Listener {
    private static final String TAG = GroceryListActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorGroceryList;
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

        coordinatorGroceryList = findViewById(R.id.coordinator_grocery_list);
        textNoGroceries = findViewById(R.id.text_no_groceries);
        groceryAdapter = new GroceryAdapter(this);
        recyclerGroceries = findViewById(R.id.recycler_groceries);
        recyclerGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroceries.setItemAnimator(null);
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

        ItemTouchHelper.SimpleCallback callback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerGroceries);

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshGroceryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_clear).setVisible(!groceryList.isEmpty());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {
            deleteAllGroceries();
            refreshGroceryList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            deleteGrocery(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.RIGHT) {
            checkGrocery(viewHolder.getAdapterPosition());
        }

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
        checkAddButtonState();
        invalidateOptionsMenu();

        if (groceryList.isEmpty()) {
            showEmptyList();
            return;
        }

        showGroceryList();
    }

    private void checkAddButtonState() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerGroceries.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (groceryList.isEmpty() || firstVisibleItemPosition <= 0 && fabAddGrocery.getVisibility() != View.VISIBLE) {
            fabAddGrocery.show();
        }
    }

    private void addGrocery(int position, Grocery newGrocery) {
        groceryList.add(position, newGrocery);
        groceryAdapter.addItem(position, newGrocery);
    }

    private void editGrocery(int position, Grocery editedGrocery) {
        groceryList.set(position, editedGrocery);
        groceryAdapter.editItem(position, editedGrocery);
    }

    private void checkGrocery(int position) {
        Grocery grocery = groceryList.get(position);
        grocery.setChecked(true);
        groceryList.remove(position);
        groceryList.add(grocery);
        groceryAdapter.moveItem(position, grocery);
        showCheckedMessage(grocery);
    }

    private void deleteGrocery(int position) {
        Grocery deletedGrocery = groceryList.get(position);
        groceryList.remove(position);
        groceryAdapter.removeItem(position);
        showDeletedMessage(position, deletedGrocery);
    }

    private void deleteAllGroceries() {
        groceryList.clear();
        groceryAdapter.clearAllItems();
    }

    private void restoreGrocery(int position, Grocery grocery) {
        addGrocery(position, grocery);
    }

    private void showCheckedMessage(Grocery checkedGrocery) {
        String message = checkedGrocery.getName() + " " + getResources().getString(R.string.grocery_checked_message);
        Snackbar.make(coordinatorGroceryList, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showDeletedMessage(final int position, final Grocery deletedGrocery) {
        String message = deletedGrocery.getName() + " " + getResources().getString(R.string.grocery_deleted_message);
        Snackbar.make(coordinatorGroceryList, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreGrocery(position, deletedGrocery);
                        refreshGroceryList();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.material_blue_100))
                .show();
    }

    private void createDummyGrocery() {
        String[] groceryNames = {"Milk", "Eggs", "Bread", "Apple", "Yogurt", "Tea", "Cereal"};
        Grocery grocery = new Grocery(groceryNames[generateRandomNumber(0, 6)],
                generateRandomNumber(1, 10), "Lorem ipsum dolor sit amet, at sit commodo " +
                "nominavi abhorreant, ius in epicuri sensibus laboramus.");
        addGrocery(0, grocery);
    }

    private int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

}
