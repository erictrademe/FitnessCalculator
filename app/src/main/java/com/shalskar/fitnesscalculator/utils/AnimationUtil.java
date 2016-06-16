package com.shalskar.fitnesscalculator.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by RachelTeTau on 18/05/16.
 */
public class AnimationUtil {

    private final static int DURATION_DRAWABLE_FADE_IN = 500;
    private final static AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator ();
    private final static ColorMatrixColorFilter filter = new ColorMatrixColorFilter(evaluator.getColorMatrix());

    // todo move all animations here

    /**
     * Rotates, fades and shrinks a view to give it a feel of being refreshed.
     * Used for pie charts when they are updated.
     *
     * @param view
     */
    public static void refreshView(@NonNull final View view){

        view.animate()
                .alpha(0.5f)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.animate()
                                .alpha(1f)
                                .scaleX(1)
                                .scaleY(1)
                                //.rotation(360)
                                .setDuration(150)
                                .setInterpolator(new DecelerateInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        view.setAlpha(1f);
                                        view.setScaleX(1f);
                                        view.setScaleY(1f);

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public static void animateDrawableIn(@NonNull final View view, @NonNull final Drawable drawable){
        drawable.setColorFilter(filter);
        ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());

        animator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawable.setColorFilter (filter);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // todo compat
                view.setBackground(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(DURATION_DRAWABLE_FADE_IN);
        animator.start();
    }
}
