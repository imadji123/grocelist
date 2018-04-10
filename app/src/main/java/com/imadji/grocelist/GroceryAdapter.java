package com.imadji.grocelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imadji on 4/5/18.
 */

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {
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
        holder.textName.setText(grocery.getName());
        holder.textAmount.setText(String.valueOf(grocery.getAmount()));
        holder.textNote.setText(grocery.getNote());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public void addItem(int position, Grocery newGrocery) {
        groceryList.add(position, newGrocery);
        notifyItemInserted(position);
    }

    public void editItem(int position, Grocery editedGrocery) {
        groceryList.set(position, editedGrocery);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        groceryList.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllItems() {
        groceryList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public TextView textAmount;
        public TextView textNote;

        public ViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text_grocery_name);
            textAmount = itemView.findViewById(R.id.text_grocery_amount);
            textNote = itemView.findViewById(R.id.text_grocery_note);
        }
    }

}
