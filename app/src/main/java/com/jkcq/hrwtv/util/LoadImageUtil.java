package com.jkcq.hrwtv.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jkcq.hrwtv.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Bravo  图片加载的基类。
 */
public class LoadImageUtil {

    private static LoadImageUtil instance;

    public static LoadImageUtil getInstance() {
        if (null == instance) {
            synchronized (LoadImageUtil.class) {
                if (null == instance) {
                    instance = new LoadImageUtil();
                }
            }
        }
        return instance;
    }


    public void setGlide(Context context) {
        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
//        RequestOptions options = RequestOptions.centerCropTransform();
    }

    /**
     * 加载默认配置图片
     *
     * @param context
     * @param url
     * @param iv
     */
    public void load(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_avatar_picture)
                .error(R.drawable.default_avatar_picture)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        load(context, url, iv, options);
    }

    public void loadCirc(Context ctx, String url, final ImageView iv, float roundCirc) {
        if (ctx == null) {
            return;
        }
        try {


            /**
             * 圆角图片 new RoundedCornersTransformation 参数为 ：半径 , 外边距 , 圆角方向(ALL,BOTTOM,TOP,RIGHT,LEFT,BOTTOM_LEFT等等)
             */
           /* MultiTransformation mation5 = new MultiTransformation(
                    new RoundedCornersTransformation(roundCirc, 0, RoundedCornersTransformation.CornerType.ALL));*/
            RequestOptions myOptions = new RequestOptions().transform(new GlideCerterTransformation(ctx, roundCirc));

            Glide.with(ctx)
                    .load(url)
                    .apply(myOptions
                            // 圆角图片
                    ).into(iv);
            // .apply(RequestOptions.bitmapTransform(mation5).centerCrop())


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载默认配置图片
     *
     * @param context
     * @param url
     * @param iv
     */

    public void loadTransform(Context context, String url, ImageView iv, int placeResId, int errorResId) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .placeholder(placeResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//              .bitmapTransform(new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL))
                .transform(new CenterCrop(), new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.TOP));
//        load(context, url, iv, options);
        load(context, url, iv, options, placeResId, errorResId);
    }

    public void loadTransform(Context context, String url, ImageView iv, int placeResId, int errorResId, int radius) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .placeholder(placeResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//              .bitmapTransform(new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.ALL))
                .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));
//        load(context, url, iv, options);
        load(context, url, iv, options, placeResId, errorResId);
    }

    public void loadTransform(Context context, String url, ImageView iv, int radius) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_avatar_picture)
                .error(R.drawable.default_avatar_picture)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));

        load(context, url, iv, options);
    }

    /**
     * 传入配置参数
     *
     * @param context
     * @param url
     * @param iv
     * @param options
     */
    public void load(Context context, String url, ImageView iv, RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(iv);
    }

    public void load(Context context, String url, ImageView iv, RequestOptions options, int placeResId, int errorId) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .thumbnail(loadTransform(context, placeResId, 30))
                .thumbnail(loadTransform(context, errorId, 30))
                .into(iv);
    }

    public void load(Context ctx, String url, ImageView iv, int errorResId) {
        iv.setTag(iv.getId(), url);
        Glide.with(ctx)
                .load(url)
                .placeholder(R.drawable.default_avatar_picture)
                .error(errorResId)
                .centerCrop()
                .dontAnimate()
                .into(iv);
    }

    public void load(Context ctx, String url, ImageView iv, int placeholderResId, int errorResId) {
        Glide.with(ctx)
                .load(url)
                .centerCrop()
                .placeholder(placeholderResId)
                .error(errorResId)
                .into(iv);
    }

    public void clearCache() {

    }

    public void clearDiskCache() {

    }

    /**
     * 当ImageView是centerCrop属性时，并且占位符也需要圆角的时候
     *
     * @param imageView
     * @param url
     * @param placeholderId
     * @param errorId
     * @param radius
     */
    public void loadRoundImg(ImageView imageView, String url, @DrawableRes int placeholderId,
                             @DrawableRes int errorId, int radius) {

        Glide.with(imageView.getContext()).load(url)
                .apply(new RequestOptions().placeholder(placeholderId).error(errorId).centerCrop()
                        .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.TOP)))
                .thumbnail(loadTransform(imageView.getContext(), placeholderId, radius))
                .thumbnail(loadTransform(imageView.getContext(), errorId, radius))
                .into(imageView);
    }

    private static RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, int radius) {

        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.TOP)));

    }
}