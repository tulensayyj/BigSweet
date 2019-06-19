package com.graduation.yau.bigsweet.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.base.OnClickDragListener;
import com.graduation.yau.bigsweet.base.OnLoadMoreScrollListener;
import com.graduation.yau.bigsweet.model.BeautyPhoto;
import com.graduation.yau.bigsweet.util.BaseUtil;
import com.graduation.yau.bigsweet.util.HttpUtil;
import com.graduation.yau.bigsweet.util.ToastUtil;

/**
 * Created by yyj on 2019/6/16.
 */

public class BeautyFragment extends Fragment {

    private RecyclerView mBeautyPhotosView;
    private BeautyPhotoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty_photo, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initView(View view) {
        mBeautyPhotosView = view.findViewById(R.id.beauty_photo_recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.beauty_photo_swipe_refresh_layout);
        mBeautyPhotosView.setLayoutManager(new StaggeredGridLayoutManager(2
                , StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new BeautyPhotoAdapter(getContext());
        mBeautyPhotosView.addOnScrollListener(new OnLoadMoreScrollListener() {
            @Override
            public void onLoadMoreScroll(RecyclerView recyclerView) {
                mAdapter.loadMoreData();
            }
        });
        mBeautyPhotosView.setAdapter(mAdapter);
        mAdapter.initData();
        setLoadFinishListener();
        setAllowDragItem(true);
        setRefreshLayout();
    }

    private void setAllowDragItem(boolean isLongPressDrag) {
        if (mBeautyPhotosView == null || mAdapter == null) {
            return;
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new OnClickDragListener<BeautyPhoto>(mAdapter.getDataList())
                        .setLongPressDrag(isLongPressDrag));

        itemTouchHelper.attachToRecyclerView(mBeautyPhotosView);
    }


    private void setRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.initData();
            }
        });
    }

    private void setLoadFinishListener(){
        if(mAdapter==null){
            return;
        }

        mAdapter.setOnLoadFinishListener(new BeautyPhotoAdapter.OnLoadFinishListener() {
            @Override
            public void onLoadSucceed() {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onLoadFailed() {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                ToastUtil.show(getActivity(),R.string.base_note_network_wrong, Toast.LENGTH_SHORT,false);
            }
        });
    }
}
