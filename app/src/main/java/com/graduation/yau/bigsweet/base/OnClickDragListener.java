package com.graduation.yau.bigsweet.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by yyj on 2019/6/17.
 */

public class OnClickDragListener<T> extends ItemTouchHelper.Callback {

    private List<T> mDataList;

    private boolean isLongPressDrag = false;

    public OnClickDragListener(List<T> dataList) {
        mDataList = dataList;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        int swipeFlag = LEFT | RIGHT;
        int swipeFlag = 0;
        int dragFlag = UP | DOWN | LEFT | RIGHT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder viewHolder1) {
        if (recyclerView.getAdapter() == null || mDataList == null) {
            return false;
        }
        recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        Collections.swap(mDataList, viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDrag();
    }


    public boolean isLongPressDrag() {
        return isLongPressDrag;
    }

    public OnClickDragListener setLongPressDrag(boolean longPressDrag) {
        isLongPressDrag = longPressDrag;
        return this;
    }

}
