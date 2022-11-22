package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * ClassName:CcsbcTextView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年5月2日 下午6:12:34 <br/>
 *
 * @author Administrator
 */
public class BebasNeueTextView extends androidx.appcompat.widget.AppCompatTextView {

    public BebasNeueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BebasNeueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BebasNeueTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        try {
            AssetManager assets = context.getAssets();
            Typeface font = Typeface.createFromAsset(assets, "fonts/BebasNeue.otf");
            setTypeface(font);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}