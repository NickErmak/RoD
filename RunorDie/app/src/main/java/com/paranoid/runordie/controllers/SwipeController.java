package com.paranoid.runordie.controllers;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.paranoid.runordie.adapters.recycler.NotificationRecyclerAdapter;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;


public class SwipeController extends ItemTouchHelper.Callback {

    public interface ISwipeEvent{
        void handleSwipe(int itemPosition);
    }

    private ISwipeEvent swipeEvent;

    public SwipeController(ISwipeEvent swipeEvent){
        this.swipeEvent = swipeEvent;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        swipeEvent.handleSwipe(viewHolder.getAdapterPosition());
    }
}
