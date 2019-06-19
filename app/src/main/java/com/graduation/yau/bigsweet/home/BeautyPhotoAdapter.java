package com.graduation.yau.bigsweet.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.model.BeautyPhoto;
import com.graduation.yau.bigsweet.model.BeautyPhotoData;
import com.graduation.yau.bigsweet.util.ImageLoader;
import com.graduation.yau.bigsweet.util.RequestBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yyj on 2019/6/16.
 */

public class BeautyPhotoAdapter extends RecyclerView.Adapter<BeautyPhotoAdapter.ViewHolder> {

    private static final String TAG = "BeautyPhotoAdapter";
    private static final int MSG_LOAD_FINISH = 1001;

    private List<BeautyPhoto> mBeautyPhotos;
    private Context mContext;
    private BeautyPhotoAdapter mAdapter;
    private OnLoadFinishListener mOnLoadFinishListener;

    private static int PRE_SIZE = 20;
    private int mStartPage = 0;

    public BeautyPhotoAdapter(@NonNull Context context) {
        this.mContext = context;
        mBeautyPhotos = new ArrayList<>();
        mAdapter = this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_beauty_photo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageLoader.loadPicture(mContext, viewHolder.imageView, mBeautyPhotos.get(i).getUrl());
    }

    @Override
    public int getItemCount() {
        return mBeautyPhotos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            imageView = itemView.findViewById(R.id.item_beauty_photo_view);
        }
    }

    private void getData() {
        RequestBuilder.getDefault(RequestBuilder.HostType.GANK_BEAUTY_PHOTO)
                .getPhotoList(RequestBuilder.getCacheControl(), PRE_SIZE, mStartPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BeautyPhotoData, List<BeautyPhoto>>() {
                    @Override
                    public List<BeautyPhoto> apply(BeautyPhotoData beautyPhotoData) throws Exception {
                        Log.d(TAG, "apply: " + beautyPhotoData.toString());
                        return beautyPhotoData.getResults();
                    }
                })
                .subscribe(new Consumer<List<BeautyPhoto>>() {
                    @Override
                    public void accept(List<BeautyPhoto> beautyPhotos) throws Exception {
                        mBeautyPhotos.addAll(beautyPhotos);
                        mStartPage += 1;
                        notifyDataSetChanged();
                        if(mOnLoadFinishListener!=null){
                            mOnLoadFinishListener.onLoadSucceed();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: 获取图片数据失败", throwable);
                        if(mOnLoadFinishListener!=null){
                            mOnLoadFinishListener.onLoadFailed();
                        }
                    }
                });
    }

//    private void getData2() {
//        HttpUtil.sendOkHttpRequest(
//                "http://gank.io/api/data/福利/" + PRE_SIZE + "/" + mStartPage,
//                new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String listValue = JsonUtil.getValue(response.body().string(), "results");
//                        List<BeautyPhoto> list = JsonUtil.parseDataToList(listValue, BeautyPhoto.class);
//                        mBeautyPhotos.addAll(list);
//                        mStartPage += 1;
//                        mHandler.sendEmptyMessage(MSG_LOAD_FINISH);
//                    }
//                });
//    }

    public void loadMoreData() {
        getData();
    }

    public void initData() {
        mStartPage=0;
        getDataList().clear();
        getData();
    }

    public List<BeautyPhoto> getDataList() {
        return mBeautyPhotos;
    }


    public void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener) {
        this.mOnLoadFinishListener = onLoadFinishListener;
    }


//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_LOAD_FINISH:
//                    mAdapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    };



    public interface OnLoadFinishListener{
        public void onLoadSucceed();
        public void onLoadFailed();
    }
}
