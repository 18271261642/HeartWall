package com.jkcq.managersetting.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.base.R;
import com.jkcq.managersetting.util.DisplayUtils;



public class ProgressView extends View {
    float radius;

    float progress = 0;
    RectF arcRectF = new RectF();
    Context mContext;


    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint circlepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint arcCirclepaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // ColorInt
    private int startColor;
    // ColorInt
    private int endColor;


    LinearGradient lg;

    public ProgressView(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initPaint();
    }

    float centerX;
    float centerY;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = (getHeight()) / 2;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    private void initPaint() {


        startColor = mContext.getResources().getColor(R.color.color_00B167);
        endColor = mContext.getResources().getColor(R.color.color_87ED4D);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(DisplayUtils.dip2px(mContext, 40));
        AssetManager assets = mContext.getAssets();

        circlepaint.setStrokeWidth(DisplayUtils.dip2px(mContext, 36));
        circlepaint.setColor(mContext.getResources().getColor(R.color.common_bg));
        circlepaint.setStyle(Paint.Style.STROKE);
        circlepaint.setStrokeCap(Paint.Cap.ROUND);

        //paint.setStrokeWidth(DisplayUtils.dip2px(mContext, 10));
        paint.setColor(mContext.getResources().getColor(R.color.common_bg));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);

        //arcCirclepaint.setStrokeWidth(DisplayUtils.dip2px(mContext, 36));
        arcCirclepaint.setColor(mContext.getResources().getColor(R.color.color_00B167));
        arcCirclepaint.setStyle(Paint.Style.FILL);
        arcCirclepaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawCircle(centerX, centerY, radius, circlepaint);
        arcRectF.set(0, centerY - radius, getWidth(), centerY + radius);

        canvas.drawRoundRect(arcRectF, DisplayUtils.dip2px(mContext, 20), DisplayUtils.dip2px(mContext, 20), paint);


        arcRectF.set(0, centerY - radius, (getWidth() * progress), centerY + radius);
        lg = new LinearGradient(0, 0, 100, arcRectF.right, startColor, endColor, Shader.TileMode.MIRROR);
        arcCirclepaint.setShader(lg);

        //canvas.rotate(90, centerX, centerY);

        //progress = 90;

        //canvas.drawRoundRect(arcRectF, 0, progress * 3.6f, false, arcCirclepaint);
        canvas.drawRoundRect(arcRectF, DisplayUtils.dip2px(mContext, 20), DisplayUtils.dip2px(mContext, 20), arcCirclepaint);

    }

}
