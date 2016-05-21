package com.shalskar.fitnesscalculator.utils;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by RachelTeTau on 18/05/16.
 */
public class AnimationUtil {

    // todo move all animations here

    /**
     * Rotates, fades and shrinks a view to give it a feel of being refreshed.
     * Used for pie charts when they are updated.
     *
     * @param view
     */
    public static void refreshView(@NonNull final View view){
        view.animate()
                .alpha(0f)
                .scaleX(0.5f)
                .scaleY(0.5f)
                //.rotation(180)
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
}
