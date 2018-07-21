package com.imadji.grocelist.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imadji.grocelist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroceryDetailsActivity extends AppCompatActivity {
    private static final String TAG = GroceryDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rel_edit_groceries_name)
    RelativeLayout layoutEditGroceriesName;
    @BindView(R.id.input_layout_groceries_name)
    TextInputLayout inputLayoutGroceriesName;
    @BindView(R.id.input_groceries_name)
    TextInputEditText inputGroceriesName;

    @BindView(R.id.long_text)
    TextView longText;

    private AppBarLayout.LayoutParams layoutParams;

    private long groceriesId = 99;
    private boolean isEditMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        layoutParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
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

        inputGroceriesName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        if (isEditMode) {
            showEditMode();
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
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

    private void showEditMode() {
        isEditMode = true;
        layoutEditGroceriesName.setVisibility(View.VISIBLE);
        layoutParams.setScrollFlags(0);
        toolbarLayout.setLayoutParams(layoutParams);
        toolbarLayout.setTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        appBarLayout.setExpanded(true,true);
        showSoftKeyboard(inputGroceriesName);
    }

    private void hideEditMode() {
        isEditMode = false;
        layoutEditGroceriesName.setVisibility(View.INVISIBLE);
        layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        toolbarLayout.setLayoutParams(layoutParams);
        toolbarLayout.setTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        hideSoftKeyboard(inputGroceriesName);
    }

    private boolean validateGroceriesName() {
        if (inputGroceriesName.getText().toString().equals("")) {
            inputLayoutGroceriesName.setError(getString(R.string.msg_enter_groceries_name));
            return false;
        }
        inputLayoutGroceriesName.setError("");
        return true;
    }

    private void setToolbarTitle(String title) {
        toolbarLayout.setTitle(title);
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
