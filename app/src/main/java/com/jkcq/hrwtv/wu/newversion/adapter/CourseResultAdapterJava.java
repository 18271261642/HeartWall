package com.jkcq.hrwtv.wu.newversion.adapter;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.eventBean.EventConstant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.util.LoadImageUtil;
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

/**
 * Created by Admin
 * Date 2022/1/11
 */
public class CourseResultAdapterJava extends RecyclerView.Adapter<CourseResultAdapterJava.MyViewHolder> {

    private static final String TAG = "CourseResultAdapterJava";

    private Context mContext;
    private List<DevicesDataShowBean> mDatas;

    public CourseResultAdapterJava(Context mContext, List<DevicesDataShowBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_course_sort, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        DevicesDataShowBean info = mDatas.get(i);
        holder.tv_cal.setText("" + HeartRateConvertUtils.doubleParseStr(info.getCal()) + "kcal");

        if (CacheDataUtil.mCurrentRange > 5) {
            holder.tv_match.setVisibility(View.GONE);
        } else {
            holder.tv_match.setVisibility(View.VISIBLE);
        }
        holder.tv_match.setText(info.getMatchRate() + "%");
        holder.tv_point.setText(HeartRateConvertUtils.doubleParseStr(info.getPoint()));
        holder.tv_hr.setText(HeartRateConvertUtils.doubleParseStr(info.getAverageHeartRate()) + "bpm");
        holder.tv_heart_strength.setText(info.getAverageHeartPercent() + "%");
        holder.tv_name.setText(info.getNikeName() + "");
        holder.tv_rank.setText((i + 1) + "");

        holder.heart_strenght_view.setValue(info.getmDatas());
        LoadImageUtil.getInstance()
                .loadCirc(
                        mContext,
                        info.getHeadUrl(),
                        holder.iv_rank_head,
                        mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp8)
                );
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CourseMatchView heart_strenght_view;
        private ImageView iv_rank_head;
        private TextView tv_name;
        private TextView tv_rank;
        private TextView tv_cal;
        private TextView tv_match;
        private TextView tv_point;
        private TextView tv_hr;
        private TextView tv_heart_strength;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heart_strenght_view = itemView.findViewById(R.id.heart_strenght_view);
            iv_rank_head = itemView.findViewById(R.id.iv_rank_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_cal = itemView.findViewById(R.id.tv_cal);
            tv_match = itemView.findViewById(R.id.tv_match);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_hr = itemView.findViewById(R.id.tv_hr);
            tv_heart_strength = itemView.findViewById(R.id.tv_heart_strength);

        }
    }

    List<DevicesDataShowBean> tmpList = new ArrayList<>();


    public void sortDataAndResetView(int sortType) {
        Timber.e("-------mDatas="+new Gson().toJson(mDatas));
        tmpList.clear();
        switch (sortType) {
            case EventConstant.SORT_DATA_CAL:

                Collections.sort(mDatas, new Comparator<DevicesDataShowBean>() {
                    @Override
                    public int compare(DevicesDataShowBean devicesDataShowBean, DevicesDataShowBean t1) {
                        int cal = String.valueOf(t1.getCal()).compareTo(String.valueOf(devicesDataShowBean.getCal()));
                        int point = String.valueOf(t1.getPoint()).compareTo(String.valueOf(devicesDataShowBean.getPoint()));
                        if(cal == 0){
                            return point;
                        }
                        return cal;
                    }
                });

                Log.e(TAG,"------卡路里排序="+new Gson().toJson(mDatas));

                break;
            case EventConstant.SORT_DATA_POINT:
                Collections.sort(mDatas, new Comparator<DevicesDataShowBean>() {
                    @Override
                    public int compare(DevicesDataShowBean devicesDataShowBean, DevicesDataShowBean t1) {
                        int cal = String.valueOf(t1.getCal()).compareTo(String.valueOf(devicesDataShowBean.getCal()));
                        int point = String.valueOf(t1.getPoint()).compareTo(String.valueOf(devicesDataShowBean.getPoint()));
                        if(point == 0){   //相等
                            return cal;
                        }else{
                            return point;
                        }
                    }
                });
                Log.e(TAG,"------经验值排序="+new Gson().toJson(mDatas));
//                List<DevicesDataShowBean> pointList = mDatas.stream().sorted(Comparator.comparing(DevicesDataShowBean::getPoint).
//                        thenComparing(DevicesDataShowBean::getMatchRate)).collect(Collectors.toList());
//                tmpList.addAll(pointList);
                break;
            case EventConstant.SORT_DATA_MATCH:
                Collections.sort(mDatas, new Comparator<DevicesDataShowBean>() {
                    @Override
                    public int compare(DevicesDataShowBean devicesDataShowBean, DevicesDataShowBean t1) {
                        int match = String.valueOf(t1.getMatchCount()).compareTo(String.valueOf(devicesDataShowBean.getMatchCount()));
                        int point = String.valueOf(t1.getPoint()).compareTo(String.valueOf(devicesDataShowBean.getPoint()));
                        if(match == 0){   //相等
                            return point;
                        }
                        return match;
                    }
                });


//                mDatas.sort(Comparator.comparing(DevicesDataShowBean::getMatchRate).reversed().thenComparing(DevicesDataShowBean::getPoint).reversed());
                Log.e(TAG,"------匹配度排序="+new Gson().toJson(mDatas));
//                List<DevicesDataShowBean> matchList = mDatas.stream().sorted(Comparator.comparing(DevicesDataShowBean::getMatchRate).
//                        thenComparing(DevicesDataShowBean::getPoint)).collect(Collectors.toList());
//                tmpList.addAll(matchList);
                break;
            case EventConstant.SORT_DATA_HR:
            case EventConstant.SORT_DATA_HR_STRENGTH:
                Collections.sort(mDatas, new Comparator<DevicesDataShowBean>() {
                    @Override
                    public int compare(DevicesDataShowBean devicesDataShowBean, DevicesDataShowBean t1) {
                        int hr = String.valueOf(t1.getAverageHeartRate()).compareTo(String.valueOf(devicesDataShowBean.getAverageHeartRate()));
                        int point = String.valueOf(t1.getPoint()).compareTo(String.valueOf(devicesDataShowBean.getPoint()));
                        if(hr == 0){   //相等
                            return point;
                        }
                        return hr;
                    }
                });
//                mDatas.sort(Comparator.comparing(DevicesDataShowBean::getAverageHeartRate).reversed().thenComparing(DevicesDataShowBean::getPoint).reversed());
                Log.e(TAG,"-----心率强度排序="+new Gson().toJson(mDatas));

//                List<DevicesDataShowBean> hrList = mDatas.stream().sorted(Comparator.comparing(DevicesDataShowBean::getAverageHeartRate).
//                        thenComparing(DevicesDataShowBean::getPoint)).collect(Collectors.toList());
//                tmpList.addAll(hrList);
                break;
            default:
                break;
        }
//        mDatas.clear();
//        mDatas.addAll(tmpList);
        notifyDataSetChanged();

    }


}
