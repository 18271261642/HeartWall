package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jkcq.hrwtv.R;

/**
 * 用于显示文字的dialog，可以设置自动关闭时长
 */
public class ShowEmptyDialog extends Dialog {


    private TextView contentTv;

    //显示的时长 默认1s
    private long showTime = 1000;


    private OnEmptyDialogListener onEmptyDialogListener;

    public void setOnEmptyDialogListener(OnEmptyDialogListener onEmptyDialogListener) {
        this.onEmptyDialogListener = onEmptyDialogListener;
    }

    private final Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x00){
                if(onEmptyDialogListener != null)
                    onEmptyDialogListener.autoDismiss();
            }
        }
    };


    public ShowEmptyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

    }

    public ShowEmptyDialog(@NonNull Context context) {
       this(context,R.style.MyDialogStyle);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(),R.layout.show_empty_dialog_layout,null);
        setContentView(view);

        contentTv = findViewById(R.id.showEmptyDialogTv);
    }


    public void setContentTvTxt(String txt){
        contentTv.setText(txt);

        handler.sendEmptyMessageDelayed(0x00,showTime);

    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public interface OnEmptyDialogListener{
        void autoDismiss();
    }

}
