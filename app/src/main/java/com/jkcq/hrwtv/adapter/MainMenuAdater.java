package com.jkcq.hrwtv.adapter;

import android.content.Context;
import android.os.Build;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.heartrate.bean.MenuBean;
import com.jkcq.hrwtv.ui.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peng on 2018/5/10.
 */

public class MainMenuAdater extends BaseCommonRefreshRecyclerAdapter<MenuBean, MainMenuAdater.MyViewHold> {


    public MainMenuAdater(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_menu_layout;
    }

    @Override
    protected MyViewHold bindBaseViewHolder(View contentView) {
        return new MyViewHold(contentView);
    }

    @Override
    protected void initData(MyViewHold viewHolder, int position, MenuBean item) {

        viewHolder.itemView.setFocusable(true);

        viewHolder.ivMenuIon.setImageResource(item.getResId());
        viewHolder.tvMenuName.setText(item.getName());
        //选中放大效果
//        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    focusStatus(v);
//                }else {
//                    normalStatus(v);
//                }
//            }
//        });
    }

    @Override
    protected void updatItemData(MyViewHold viewHolder, int position, MenuBean item) {

    }

    @Override
    protected void initEvent(MyViewHold viewHolder, int position, final MenuBean item) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toView(item.getMenuType());
            }
        });
      /*  viewHolder.ivMenuIon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

    /*    viewHolder.tvMenuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toView(item.getMenuType());
            }
        });*/
    }


    private void toView(String type) {
        switch (type) {
            case Constant.MENU_TASK:
                sendEvent(Constant.MENU_TASK);
                break;
            case Constant.MENU_DISPLAY:
                sendEvent(Constant.MENU_DISPLAY);
                break;
            case Constant.MENU_FILTER_USER:
                sendEvent(Constant.MENU_FILTER_USER);
                break;
            case Constant.MENU_SORT:
                sendEvent(Constant.MENU_SORT);
                break;
            case Constant.MENU_TIMER:
                sendEvent(Constant.MENU_TIMER);
                break;
            case Constant.MENU_CLUB_NAME:
                sendEvent(Constant.MENU_CLUB_NAME);
                break;
        }
    }

    private void sendEvent(String toView) {
        BackEvent event = new BackEvent();
        event.setToFragment(toView);
        EventBus.getDefault().post(event);
    }

    class MyViewHold extends BaseViewHolder {
        @BindView(R.id.iv_menu_ion)
        ImageView ivMenuIon;
        @BindView(R.id.tv_menu_name)
        TextView tvMenuName;

        public MyViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * item获得焦点时调用
     *
     * @param itemView view
     */
    private void focusStatus(View itemView) {
        if (itemView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            //抬高Z轴
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).translationZ(1).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
//        onItemFocus(itemView);
    }
    /**
     * 当item获得焦点时处理
     *
     * @param itemView itemView
     */
//    protected abstract void onItemFocus(View itemView);
    /**
     * item失去焦点时
     *
     * @param itemView item对应的View
     */
    private void normalStatus(View itemView) {
        if (itemView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).translationZ(0).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }
}
