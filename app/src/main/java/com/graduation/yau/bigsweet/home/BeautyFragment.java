package com.graduation.yau.bigsweet.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.base.OnLoadMoreScrollListener;
import com.graduation.yau.bigsweet.model.BeautyPhoto;
import com.graduation.yau.bigsweet.util.HttpUtil;
import com.graduation.yau.bigsweet.util.JsonUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by yyj on 2019/6/16.
 */

public class BeautyFragment extends Fragment {

    private RecyclerView mBeautyPhotosView;
    private BeautyPhotoAdapter mAdapter;

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
    }

}
