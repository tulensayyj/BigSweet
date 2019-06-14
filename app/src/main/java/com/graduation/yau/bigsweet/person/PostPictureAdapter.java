package com.graduation.yau.bigsweet.person;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.util.ImageLoader;
import com.graduation.yau.bigsweet.util.TextUtil;
import com.graduation.yau.bigsweet.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyj on 2019/6/14.
 */

public class PostPictureAdapter extends BaseAdapter {

    private boolean isShowDelete = false;  //是否显示删除按钮
    private boolean isAdd = true; //是否显示添加按钮
    private int mPictureNum = 3;  // 最大图片数
    private OnclickAddBtnListener mOnclickAddBtnListener;
    private Context mContext;

    private List<String> mPicturesList = new ArrayList<>();

    public PostPictureAdapter(Context context, int pictureNum) {
        this.mPictureNum = pictureNum;
        this.mContext = context;
        showAddBtn();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mPicturesList.size();
    }

    @Override
    public Object getItem(int i) {
        if (i < mPicturesList.size()) {
            return mPicturesList.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_picture_layout, viewGroup, false);
        }
        ImageView showImage = ViewUtil.get(view, R.id.item_grid_picture_show);
        ImageView deleteImageBtn = ViewUtil.get(view, R.id.item_grid_picture_delete_btn);

        String url = mPicturesList.get(i);


        if (!url.equals("")) {
            deleteImageBtn.setVisibility(View.VISIBLE);
            ImageLoader.loadPicture(mContext, showImage, url);
        } else {
            ImageLoader.loadPicture(mContext, showImage, R.drawable.ic_add_picture);
            deleteImageBtn.setVisibility(View.GONE);
        }

        if (!isAdd) {
            showAddBtn();
        }

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickAddBtnListener != null && getItem(i).equals("") && i == getCount() - 1) {
                    mOnclickAddBtnListener.onClickAdd(i);
                }
            }
        });

        deleteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(i);
                if (!isAdd) {
                    showAddBtn();
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }


    public void setAllItem(List<String> urls) {
        mPicturesList = urls;
    }

    public void addAllItem(List<String> urls) {
        if (mPicturesList == null) {
            mPicturesList = new ArrayList<>();
        }
        mPicturesList.addAll(urls);
    }

    public void addItem(String url) {
        if (mPicturesList == null) {
            mPicturesList = new ArrayList<>();
        }
        mPicturesList.add(url);
    }

    public void addItemAt(int position, String url) {
        if (mPicturesList == null) {
            mPicturesList = new ArrayList<>();
        }
        mPicturesList.add(position, url);
    }

    public void removeItem(int i) {
        if (mPicturesList == null) {
            return;
        }
        mPicturesList.remove(i);
    }

    public void removeAll() {
        if (mPicturesList == null) {
            return;
        }
        mPicturesList.clear();
    }

    public List<String> getDataList() {
        return mPicturesList;
    }


    /**
     * 移除add按钮
     */
//    public void checkHideShowAddBtn() {
//        int lastPosition = getCount() - 1;
//        if (lastPosition == mPictureNum - 1
//                && getDataList().get(lastPosition) != null
//                && TextUtils.isEmpty(getDataList().get(lastPosition))) {
//            getDataList().remove(lastPosition);
//            isAdd = false;
//            notifyDataSetChanged();
//        } else if (!isAdd) {
//            showAddBtn();
//        }
//    }

    /**
     * 移除add按钮
     */
    public void HideAddBtn() {
        int lastPosition = getDataList().size() - 1;
        if (getDataList().get(lastPosition) != null && TextUtils.isEmpty(getDataList().get(lastPosition))) {
            getDataList().remove(lastPosition);
            isAdd = false;
        }
    }

    /**
     * 显示add按钮
     */
    public void showAddBtn() {
        if (getCount() < mPictureNum) {
            addItem("");
            isAdd = true;
        }
    }

    public void setOnClickAddBtnListener(OnclickAddBtnListener onClickAddBtnListener) {
        mOnclickAddBtnListener = onClickAddBtnListener;
    }


    public interface OnclickAddBtnListener {
        void onClickAdd(int position);
    }
}
