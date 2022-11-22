package com.jkcq.hrwtv.wu.newversion.activity;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.service.UserContans;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.util.LoadImageUtil;
import com.jkcq.hrwtv.util.TimeUtil;
import com.jkcq.hrwtv.wu.newversion.AdapterUtil;
import com.jkcq.hrwtv.wu.newversion.view.RealMatchView;

import java.util.List;

/**
 * Created by Admin
 * Date 2022/1/6
 */
public class NewPkAdapter extends RecyclerView.Adapter<NewPkAdapter.NewPkViewHolder> {

    private Context mContext;
    private List<DevicesDataShowBean> list;

    Resources resource;
    private int size = 8;

    private String url;

    public NewPkAdapter(Context mContext, List<DevicesDataShowBean> list) {
        this.mContext = mContext;
        this.list = list;
        resource = mContext.getResources();
    }

    @NonNull
    @Override
    public NewPkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_heartrate4,viewGroup,false);
        return new NewPkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewPkViewHolder holder, int position) {
        DevicesDataShowBean info = list.get(position);
        size = list.size();
        AdapterUtil.setVaule(holder.tv_cal, info, Constant.TYPE_CAL);
        AdapterUtil.setVaule(holder.tv_hr, info, Constant.TYPE_HR);
        AdapterUtil.setVaule(holder.tv_streng_value, info, Constant.TYPE_PERCENT);
        holder.tv_name.setText(info.getNikeName());
        //设置用户头像和昵称
        if (info.getHeadUrl() == null) {
            url = "";
        } else {
            url = info.getHeadUrl();
        }
        //设置背景
        Log.e("size", "size------------" + size + "info.getUser().getJoinTime()=" + info.getJoinTime());
        setCenterLayout(holder, resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_content_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_content_height));
        setImageHeightAndWith(holder.iv_head, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_head_height), mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_head_with));
        LoadImageUtil.getInstance().loadCirc(mContext, url, holder.iv_head, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp4));
        setViewScale(holder, resource.getDimension(com.jkcq.hrwtv.base.R.dimen.sp9), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.size_21), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard4_ceter_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.sp12), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.sp24));
        ViewGroup.LayoutParams paramsSeekbar = holder.hrStageView.getLayoutParams();
        paramsSeekbar.width = DisplayUtils.dip2px(mContext, 10f);
        holder.hrStageView.setLayoutParams(paramsSeekbar);


        setImaveScale(holder.iv_hr, holder.iv_cal, holder.iv_point, true);
        AdapterUtil.setItemBg(holder.l_layout, Integer.parseInt(info.getPrecent()), mContext, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp8), holder.iv_current_stren, true);
        long currenttime = System.currentTimeMillis();
        Log.e("jionTime", "(currenttime - info.getJoinTime()) / 1000" + (currenttime - info.getJoinTime()) / 1000 + ",UserContans.couserTime=" + UserContans.couserTime + "CacheDataUtil.mCurrentRange=" + CacheDataUtil.mCurrentRange);
        if (CacheDataUtil.mCurrentRange > 5) {
            holder.tv_time.setText(TimeUtil.getHallTime(info.getJoinTime()));
        } else {
            if ((currenttime - info.getJoinTime()) / 1000 >= UserContans.couserTime) {
                holder.tv_time.setText(TimeUtil.getHallTime(currenttime - UserContans.couserTime * 1000));
            } else {
                holder.tv_time.setText(TimeUtil.getHallTime(info.getJoinTime()));
            }
        }


        setMatchPercent(holder, info);
        holder.hrStageView.setToalSecend(info.getCourseTime());
        if (CacheDataUtil.mCurrentRange > 5) {
            holder.hrStageView.setValue(info.getmDatas(), false);
        } else {
            holder.hrStageView.setValue(info.getmDatas(), true);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setCenterLayout(NewPkViewHolder viewHolder, float width, float height, float contentWidth, float contentHeight) {


        Log.e("setCenterLayout", "width=" + width + ",height=" + height);
        ViewGroup.LayoutParams layoutParamss = viewHolder.l_layout.getLayoutParams();
        layoutParamss.width = (int) contentWidth;
        layoutParamss.height = (int) contentHeight;
        ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
        params.height = (int) height;
        params.width = (int) width;
        viewHolder.itemView.setLayoutParams(params);


    }

    private void setMatchPercent(NewPkViewHolder holder, DevicesDataShowBean info) {

        double point = 0;
        //计算经验值
        Log.e("setMatchPercent", "CacheDataUtil.mCurrentRange" + CacheDataUtil.mCurrentRange);
        if (CacheDataUtil.mCurrentRange > 5) {
//            point = HeartRateConvertUtils.hearRate2Point(
//                    info.getCal(),
//                    Constant.MODE_ALL,
//                    0
//            );
            point = info.getPoint();
            Log.e("setMatchPercentFree", "info.getCal()" + info.getCal() + "point" + point);
            info.setPoint(point);
            holder.tv_point.setText(HeartRateConvertUtils.doubleParseStr(point));
            return;
        }
        int hearStrength = Integer.valueOf(info.getPrecent());
        int type = 0;
        if (hearStrength < 50) {
            type = 0;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            type = 1;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            type = 2;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            type = 3;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            type = 4;
        } else if (hearStrength >= 90) {
            type = 5;
        }
        info.setTotalCount();
        if (type == CacheDataUtil.mCurrentRange) {
            info.setMatchCount();
        }
        Log.e("setMatchPercentCourse", "info.getCal()" + info.getCal() + "point" + point + ",info.getTotalCount()=" + info.getTotalCount() + ",info.getMatchCount=" + info.getMatchCount() + "type=" + type + ", CacheDataUtil.mCurrentRange=" + CacheDataUtil.mCurrentRange);
        info.setMatchRate(info.getMatchCount() * 100 / info.getTotalCount());
//        point = HeartRateConvertUtils.hearRate2Point(
//                info.getCal(),
//                Constant.MODE_COURSE,
//                info.getMatchCount() * 100 / info.getTotalCount()
//        );
        point = info.getPoint();
        info.setPoint(point);
        holder.tv_point.setText(HeartRateConvertUtils.doubleParseStr(point));


    }

    private void setImaveScale(ImageView ivHr, ImageView ivCal, ImageView ivPoint, boolean isCard1) {
        if (isCard1) {
            ivHr.setImageResource(R.mipmap.icon_card1_hr_course_nor);
            ivCal.setImageResource(R.mipmap.icon_card1_main_cal);
            ivPoint.setImageResource(R.mipmap.icon_card1_main_point);
        } else {
            ivHr.setImageResource(R.mipmap.icon_hr_course_nor);
            ivCal.setImageResource(R.mipmap.icon_main_cal);
            ivPoint.setImageResource(R.mipmap.icon_main_point);
        }
    }

    private void setViewScale(NewPkViewHolder viewHolder, float tvNameTextSize, float tvCalSize, float tvStrenSize, float tvTimeSize, float tvStrengImageSize) {
        viewHolder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvNameTextSize);
        viewHolder.tv_cal.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvCalSize);
        viewHolder.tv_hr.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvCalSize);
        viewHolder.tv_point.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvCalSize);
        viewHolder.tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvTimeSize);
        viewHolder.iv_center.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvStrengImageSize);
        viewHolder.tv_streng_value.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvStrenSize);

    }


    private void setImageHeightAndWith(View imageView, float height, float width) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = (int) height;
        params.width = (int) width;
        imageView.setLayoutParams(params);
    }


    class NewPkViewHolder extends RecyclerView.ViewHolder{

        //头像
        ImageView iv_head;

        RealMatchView hrStageView;

        ImageView iv_current_stren;
        TextView iv_center;
        ConstraintLayout l_layout;

        TextView tv_streng_value;
        TextView tv_cal;
        TextView tv_hr;
        TextView tv_point;

        TextView tv_name;
        TextView tv_time;
        TextView tv_match;

        ImageView iv_hr, iv_cal, iv_point;

        public NewPkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);
            iv_hr = itemView.findViewById(R.id.iv_hr);
            iv_cal = itemView.findViewById(R.id.iv_cal);
            iv_point = itemView.findViewById(R.id.iv_point);
            tv_time = itemView.findViewById(R.id.tv_time);
            l_layout = itemView.findViewById(R.id.l_layout);
            iv_current_stren = itemView.findViewById(R.id.iv_current_stren);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_match = itemView.findViewById(R.id.tv_match);
            tv_cal = itemView.findViewById(R.id.tv_one);
            tv_hr = itemView.findViewById(R.id.tv_two);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_streng_value = itemView.findViewById(R.id.tv_point_value);
            iv_center = itemView.findViewById(R.id.iv_center);
            hrStageView = itemView.findViewById(R.id.hrStageView);
        }
    }
}
