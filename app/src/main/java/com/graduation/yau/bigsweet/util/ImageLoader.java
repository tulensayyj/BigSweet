package com.graduation.yau.bigsweet.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.graduation.yau.bigsweet.R;

/**
 * Created by yyj on 2019/6/14.
 */

public class ImageLoader {
    public static void loadPicture(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_toast_no))
                .into(imageView);
    }

    public static void loadPicture(Context context, ImageView imageView, int ResId) {
        Glide.with(context).load(ResId)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_toast_no))
                .into(imageView);
    }

    public static void loadPicture(Context context, ImageView imageView, Drawable drawable) {
        Glide.with(context).load(drawable)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_toast_no))
                .into(imageView);
    }


}
