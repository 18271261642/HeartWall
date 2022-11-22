package com.jkcq.hrwtv.wu.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShapeUtil {

    @SuppressLint("WrongConstant")
    public static void setColorRec(View view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setBackground(drawable);
    }

    @SuppressLint("WrongConstant")
    public static void setColorRec(LinearLayout view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setBackground(drawable);
    }

    @SuppressLint("WrongConstant")
    public static void setColorRec(ImageView view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setImageDrawable(drawable);
    }

    @SuppressLint("WrongConstant")
    public static void setColorRecLeftTop(View view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);

        drawable.setCornerRadii(new float[]{0f, 0f,
                radius, radius,
                radius, radius,
                radius, radius
        });
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setBackground(drawable);
    }

    @SuppressLint("WrongConstant")
    public static void setColorRecLeftBottom(View view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{radius, radius,
                radius, radius,
                radius, radius,
                0f, 0f

        });
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setBackground(drawable);
    }

    @SuppressLint("WrongConstant")
    public static void setColorOval(View view, int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setGradientType(GradientDrawable.OVAL);
        drawable.setCornerRadius(radius);
        drawable.setColor(color);
/*        drawable.setColor(Color.parseColor(item.getRangeBackColor()));
        TextView textRelation=helper.getView(你要设置背景的控件名);*/
        view.setBackground(drawable);
    }
}
