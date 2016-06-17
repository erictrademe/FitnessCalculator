package com.shalskar.fitnesscalculator.viewholders;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.utils.AnimationUtil;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Vincent on 10/06/2016.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    /**
     * We could make this a variable to adjust the performance of the app on older phones
     */
    private final static float IMAGE_QUALITY_REDUCTION = 1f;
    private static DisplayMetrics displayMetrics;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.card_view)
    CardView cardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    protected View baseView;

    public BaseViewHolder(View itemView) {
        super(itemView);

    }

    protected void animateSideLayout() {
        sideLayout.setTranslationX(sideLayout.getWidth());
        sideLayout.setAlpha(0);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.animate().alpha(1).translationX(0).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
    }


    protected void loadImage(int widthResource, int heightResource, int imageResource) {
        int width = (int) (baseView.getResources().getDimension(widthResource) * IMAGE_QUALITY_REDUCTION);
        int height = (int) (baseView.getResources().getDimension(heightResource) * IMAGE_QUALITY_REDUCTION);
        processImage(width, height, imageResource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        AnimationUtil.animateDrawableIn(imageView, imageView.getDrawable());
                    }
                });
    }


    private Observable<Bitmap> processImage(final int width, final int height, final int imageResource) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), imageResource, width, height);
                    subscriber.onNext(bitmap);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    protected int getUnconvertedDimension(int dimensionRes){
        if (displayMetrics == null) displayMetrics = baseView.getResources().getDisplayMetrics();
        return (int) (baseView.getContext().getResources().getDimension(dimensionRes) / displayMetrics.density);
    }
}
