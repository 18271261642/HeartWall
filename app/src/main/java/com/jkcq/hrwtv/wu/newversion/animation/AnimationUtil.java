package com.jkcq.hrwtv.wu.newversion.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import androidx.core.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.wu.newversion.AdapterUtil;
import com.plattysoft.leonids.ParticleSystem;

public class AnimationUtil {

    private static final int SPEED = 1000;

    public static void rotateView(View view) {
        ObjectAnimator AnimatorRotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        AnimatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                Log.e("animatin", animation.getAnimatedValue() + "");
            }

        });
        AnimatorRotate.setDuration(500);
        AnimatorRotate.start();

    }

    /**
     * 旋转
     *
     * @param view
     */
    public static void rotateXView(View view) {
        ObjectAnimator AnimatorRotate = ObjectAnimator.ofFloat(view, "rotationX", 0f, 25f);
        AnimatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.e("animatin", animation.getAnimatedValue() + "");

            }

        });
        AnimatorRotate.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator AnimatorRotate1 = ObjectAnimator.ofFloat(view, "rotationX", 25f, -25f);
                AnimatorRotate1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator AnimatorRotate1 = ObjectAnimator.ofFloat(view, "rotationX", -25f, 0f);
                        AnimatorRotate1.setDuration(SPEED);
                        AnimatorRotate1.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                AnimatorRotate1.setDuration(SPEED);
                AnimatorRotate1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorRotate.setDuration(SPEED);
        AnimatorRotate.start();

    }

    public static void rotateYView(View view) {
        ObjectAnimator AnimatorRotate = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f);
        AnimatorRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                Log.e("animatin", animation.getAnimatedValue() + "");
            }

        });
        AnimatorRotate.setDuration(500);
        AnimatorRotate.start();

    }

    /**
     * 撒花动画
     *
     * @param activity
     * @param layoutView
     */
    public static void particleView(Activity activity, View layoutView, String colorState) {
        float percent = Float.valueOf(colorState);
        LogUtil.e("Animation", "percent =" + percent);
        int drawableResId = R.mipmap.icon_heart_red;
        switch (AdapterUtil.convertRang(percent)) {
            case 2:
                drawableResId = R.mipmap.icon_heart_blue;
                break;
            case 3:
                drawableResId = R.mipmap.icon_heart_green;
                break;
            case 4:
                drawableResId = R.mipmap.icon_heart_yellow;
                break;
            case 5:
                drawableResId = R.mipmap.icon_heart_red;
                break;
            default:
                return;
        }
        LogUtil.e("Animation", "percent =" + percent);
        new ParticleSystem(activity, 100, drawableResId, 2000)
                .setAcceleration(0.002f, 90)
                .setSpeedByComponentsRange(0f, 0f, 0.5f, 0.9f)
                .setFadeOut(200, new AccelerateInterpolator())
                .emitWithGravity(layoutView, Gravity.BOTTOM, 90, 1000);
    }

    /**
     * 放大缩小
     */
    public static void ScaleUpView(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1.02f)
                .scaleY(1.02f)
                .start();
    }
    public static void ScaleUpViewCourse(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1.02f)
                .scaleY(1.02f)
                .start();
    }

    public static void ScaleDownView(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1f)
                .scaleY(1f)
                .start();
    }
}
