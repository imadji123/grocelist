package com.imadji.grocelist.view.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by imadji on 4/12/18.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final Listener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, Listener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == 2) return 0;
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    public interface Listener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
    }

}
