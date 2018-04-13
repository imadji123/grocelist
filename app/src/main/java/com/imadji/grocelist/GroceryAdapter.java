package com.imadji.grocelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imadji on 4/5/18.
 */

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {
    private static final String TAG = GroceryAdapter.class.getSimpleName();

    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_CHECKED = 2;

    private Context context;
    private List<Grocery> groceryList;

    public GroceryAdapter(Context context) {
        this.context = context;
        groceryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_grocery,
                parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);
        holder.setItem(grocery);
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (groceryList.get(position).isChecked()) {
            return TYPE_CHECKED;
        }
        return TYPE_NORMAL;
    }

    public void addItem(int position, Grocery newGrocery) {
        groceryList.add(position, newGrocery);
        notifyItemInserted(position);
    }

    public void editItem(int position, Grocery editedGrocery) {
        groceryList.set(position, editedGrocery);
        notifyItemChanged(position);
    }

    public void moveItem(int position, Grocery checkedGrocery) {
        groceryList.remove(position);
        groceryList.add(checkedGrocery);
        notifyItemMoved(position, groceryList.size() - 1);
        notifyItemInserted(groceryList.size() - 1);
    }

    public void removeItem(int position) {
        groceryList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAllItems() {
        groceryList.clear();
        notifyItemRemoved(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public TextView textAmount;
        public TextView textNote;
        public RelativeLayout layoutChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_grocery_name);
            textAmount = itemView.findViewById(R.id.text_grocery_amount);
            textNote = itemView.findViewById(R.id.text_grocery_note);
            layoutChecked = itemView.findViewById(R.id.relative_grocery_status_checked);
        }

        public void setItem(Grocery grocery) {
            textName.setText(grocery.getName());
            textAmount.setText(String.valueOf(grocery.getAmount()));
            textNote.setText(grocery.getNote());
            if (grocery.isChecked()) {
                layoutChecked.setVisibility(View.VISIBLE);
            }
        }
    }

}
