package com.imadji.grocelist.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imadji.grocelist.Grocery;
import com.imadji.grocelist.GroceryAdapter;
import com.imadji.grocelist.R;
import com.imadji.grocelist.helper.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroceryDetailsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.Listener {
    private static final String TAG = GroceryDetailsActivity.class.getSimpleName();

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorGroceries;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_grocery)
    FloatingActionButton fabAddGrocery;
    @BindView(R.id.rel_edit_groceries_name)
    RelativeLayout layoutEditGroceriesName;
    @BindView(R.id.input_layout_groceries_name)
    TextInputLayout inputLayoutGroceriesName;
    @BindView(R.id.input_groceries_name)
    TextInputEditText inputGroceriesName;

    @BindView(R.id.text_no_groceries)
    TextView textNoGrocery;
    @BindView(R.id.recycler_groceries)
    RecyclerView recyclerGroceries;

    private AppBarLayout.LayoutParams layoutParams;
    private GroceryAdapter groceryAdapter;

    private long groceriesId;
    private String groceriesName;
    private boolean isEditMode;

    private List<Grocery> groceryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_details);
        ButterKnife.bind(this);

        setupToolbar();
        setupView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshGroceryList();

        if (isEditMode) {
            showEditMode();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(!isEditMode);
        menu.findItem(R.id.action_save).setVisible(isEditMode);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditMode();
                break;
            case R.id.action_save:
                if (!validateGroceriesName()) return false;
                hideEditMode();
                setToolbarTitle(inputGroceriesName.getText().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isEditMode && groceriesId > 0) {
            hideEditMode();
            return false;
        }
        onBackPressed();
        return super.onSupportNavigateUp();
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

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        layoutParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
    }

    private void setupView() {
        groceryAdapter = new GroceryAdapter(this);
        recyclerGroceries.setLayoutManager(new LinearLayoutManager(this));
        recyclerGroceries.setItemAnimator(null);
        recyclerGroceries.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerGroceries.setAdapter(groceryAdapter);

        fabAddGrocery.setOnClickListener(view -> {
            createDummyGrocery();
            refreshGroceryList();
            recyclerGroceries.getLayoutManager().scrollToPosition(0);
        });

        inputGroceriesName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateGroceriesName();
            }
        });

        ItemTouchHelper.SimpleCallback callback = new RecyclerItemTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerGroceries);

    }

    private void initData() {
        groceriesId = 99;
        groceriesName = "Sample";
        isEditMode = groceriesId > 0 ? false : true;
        inputGroceriesName.setText(groceriesName);
        setToolbarTitle(groceriesName);
    }

    private void setToolbarTitle(String title) {
        toolbarLayout.setTitle(title);
    }

    private void lockToolbar() {
        layoutParams.setScrollFlags(0);
        toolbarLayout.setLayoutParams(layoutParams);
        invalidateOptionsMenu();
    }

    private void unlockToolbar() {
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        toolbarLayout.setLayoutParams(layoutParams);
        invalidateOptionsMenu();
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

    private void showEmptyList() {
        lockToolbar();
        if (textNoGrocery.getVisibility() == View.GONE) {
            textNoGrocery.setVisibility(View.VISIBLE);
            recyclerGroceries.setVisibility(View.GONE);
        }
    }

    private void showGroceryList() {
        unlockToolbar();
        if (recyclerGroceries.getVisibility() == View.GONE) {
            textNoGrocery.setVisibility(View.GONE);
            recyclerGroceries.setVisibility(View.VISIBLE);
        }
    }

    private void addGrocery(int position, Grocery newGrocery) {
        groceryList.add(position, newGrocery);
        groceryAdapter.addItem(position, newGrocery);
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

    private void restoreGrocery(int position, Grocery grocery) {
        addGrocery(position, grocery);
    }

    private void showEditMode() {
        isEditMode = true;
        lockToolbar();
        layoutEditGroceriesName.setVisibility(View.VISIBLE);
        toolbarLayout.setTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        appBarLayout.setExpanded(true,true);
        showSoftKeyboard(inputGroceriesName);
        recyclerGroceries.setNestedScrollingEnabled(false);
        fabAddGrocery.hide();
    }

    private void hideEditMode() {
        isEditMode = false;
        unlockToolbar();
        layoutEditGroceriesName.setVisibility(View.INVISIBLE);
        toolbarLayout.setTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        hideSoftKeyboard(inputGroceriesName);
        recyclerGroceries.setNestedScrollingEnabled(true);
        fabAddGrocery.show();
        refreshGroceryList();
    }

    private void checkAddButtonState() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerGroceries.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (groceryList.isEmpty() || firstVisibleItemPosition <= 0 && fabAddGrocery.getVisibility() != View.VISIBLE) {
            fabAddGrocery.show();
        }
    }

    private boolean validateGroceriesName() {
        if (inputGroceriesName.getText().toString().equals("")) {
            inputLayoutGroceriesName.setError(getString(R.string.msg_enter_groceries_name));
            return false;
        }
        inputLayoutGroceriesName.setError("");
        return true;
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideSoftKeyboard(View view) {
        if (view.hasFocus()) view.clearFocus();
        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showCheckedMessage(Grocery checkedGrocery) {
        String message = checkedGrocery.getName() + " " + getResources().getString(R.string.msg_grocery_checked);
        Snackbar.make(coordinatorGroceries, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showDeletedMessage(final int position, final Grocery deletedGrocery) {
        String message = deletedGrocery.getName() + " " + getResources().getString(R.string.msg_grocery_deleted);
        Snackbar.make(coordinatorGroceries, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, view -> {
                    restoreGrocery(position, deletedGrocery);
                    refreshGroceryList();
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
