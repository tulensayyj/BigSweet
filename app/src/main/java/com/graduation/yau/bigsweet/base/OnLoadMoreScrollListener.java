package com.graduation.yau.bigsweet.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yyj on 2019/6/16.
 */

public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {


    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager == null) {
            return;
        }

        int visibleItemCount = layoutManager.getChildCount(); // 已经显示出来的item的数目

        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                && isScrollToLast(recyclerView)) {
            onLoadMoreScroll(recyclerView);
        }
    }

    private boolean isScrollToLast(RecyclerView recyclerView) {
        View lastView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int position = recyclerView.getChildLayoutPosition(lastView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();

        return totalItemCount == position + 1;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    public abstract void onLoadMoreScroll(RecyclerView recyclerView);
}
