package com.imadji.grocelist.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.imadji.grocelist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroceryListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_add_groceries)
    FloatingActionButton fabAddGroceries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fabAddGroceries.setOnClickListener(view -> {
//            Intent intent = new Intent(this, GroceryDetailsActivity.class);
//            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.grocery_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
