package com.jkcq.hrwtv.wu.newversion.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.ui.view.HeartRateStageView;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.wu.newversion.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private List<DevicesDataShowBean> mDatas;
    private Context mContext;
    Resources resource;
    private int size = 24;

    public CourseAdapter(Context context, List<DevicesDataShowBean> data) {
        this.mDatas = data;
        mContext = context;
        resource = mContext.getResources();
        if (data == null) {
            mDatas = new ArrayList<>();
        }

    }

    public void replaceData(List<DevicesDataShowBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_heartrate_result, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        DevicesDataShowBean info = mDatas.get(position);

        if (holder != null) {
            if (size == 1) {
                setLayoutValue(holder, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_with), mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_height));
                setViewScale(holder, resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_ceter_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_name_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_head_with),
                        resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_head_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_image_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_image_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_bar_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard1_match_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_head_border_width));
            } else if (size == 2) {
                setLayoutValue(holder, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_with), mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_height));
                setViewScale(holder, resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_ceter_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_name_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_head_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_head_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_image_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_image_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_bar_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_match_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard2_head_border_width));
            } else {
                setLayoutValue(holder, mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_with), mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_height));
                setViewScale(holder, resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_ceter_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_name_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_text_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_head_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_head_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_image_with), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_image_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_bar_height), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_match_size), resource.getDimension(com.jkcq.hrwtv.base.R.dimen.ncard3_head_border_width));
            }
           /* if (position % 6 == 0) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_one);
            } else if (position % 6 == 1) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_two);
            } else if (position % 6 == 2) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_three);
            } else if (position % 6 == 3) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_four);
            } else if (position % 6 == 4) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_five);
            } else if (position % 6 == 5) {
                holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_six);
            }*/

            holder.tv_name.setText("忆往昔");
//            holder.tv_name.setText("138****8888");
            holder.tv_match.setText("匹配度：89%");
            //  AdapterUtil.setVaule(Constant.LOCATION_ONE, holder.tv_one, holder.iv_one, null);
            // AdapterUtil.setVaule(Constant.LOCATION_TWO, holder.tv_two, holder.iv_two, null);
            //  AdapterUtil.setVaule(Constant.LOCATION_THREE, holder.tv_three, holder.iv_three, null);
            //  AdapterUtil.setVaule(Constant.LOCATION_CENTER, holder.tv_certer_value, holder.iv_center, null);
//            holder.tv_one.setText("920");
//            holder.tv_two.setText("146");
//            holder.tv_three.setText("20");
//            holder.tv_certer_value.setText("66");
//            int imagewidth = (int) resource.getDimension(R.dimen.card4_head_with) / 2;
//            LoadImageUtil.getInstance().loadTransform(mContext, "", holder.iv_head, imagewidth);
//            holder.iv_head.setBorderWidth();
//            holder.iv_one.setImageResource(R.mipmap.icon_heart_cal);
//            holder.iv_two.setImageResource(R.mipmap.icon_heart_hr);
//            holder.iv_three.setImageResource(R.mipmap.icon_heart_point);
//       holder.iv_center.setImageResource(R.mipmap.icon_heart_point);
            holder.iv_head.setImageResource(R.mipmap.icon_default_head);
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }


    private void setLayoutValue(MyViewHolder viewHolder, float width, float height) {
        if (viewHolder != null) {
            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
//        layoutParams.width = DisplayUtils.dip2px(mContext, width);
//        layoutParams.height = DisplayUtils.dip2px(mContext, height);
            layoutParams.width = (int) width;
            layoutParams.height = (int) height;
        }
    }

    private void setViewScale(MyViewHolder viewHolder, float certerSize, float nameTxtSize, float detailTxtSize, float ivheadWith, float ivheadHeigh, float detailWith, float detailHeight, float barHeight, float matchSize, float imageBorderWidth) {


//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, 0, 0, 0);//4个参数按顺序分别是左上右下
//        layoutParams.addRule(RelativeLayout.ABOVE, R.id.tv_flag);
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        layoutParams.bottomMargin = DisplayUtils.dip2px(mContext, margBotton);
//        layoutParams.bottomMargin = (int) margBotton;
//        viewHolder.tvCerterValue.setLayoutParams(layoutParams);

        setImageHeightAndWith(viewHolder.iv_two, detailWith, detailWith);
        setImageHeightAndWith(viewHolder.iv_one, detailWith, detailWith);
        setImageHeightAndWith(viewHolder.iv_three, detailWith, detailWith);
        setImageHeightAndWith(viewHolder.iv_center, detailHeight, detailHeight);//
        setImageHeightAndWith(viewHolder.iv_head, ivheadHeigh, ivheadWith);

        viewHolder.iv_head.setBorderWidth((int) imageBorderWidth);
//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(viewHolder.iv_head.getLayoutParams());
//        layoutParams.leftMargin=DisplayUtils.dip2px(mContext,28);
//        layoutParams.topMargin=DisplayUtils.dip2px(mContext,28);
////        layoutParams.setMargins(DisplayUtils.dip2px(mContext,28), DisplayUtils.dip2px(mContext,28), 0, 0);
//        viewHolder.iv_head.setLayoutParams(layoutParams);

        viewHolder.tv_certer_value.setTextSize(certerSize);
        viewHolder.tv_name.setTextSize(nameTxtSize);
        viewHolder.tv_one.setTextSize(detailTxtSize);
        viewHolder.tv_two.setTextSize(detailTxtSize);
        viewHolder.tv_three.setTextSize(detailTxtSize);
        viewHolder.tv_match.setTextSize(matchSize);

        ViewGroup.LayoutParams paramsSeekbar = viewHolder.hrStageView.getLayoutParams();
        paramsSeekbar.height = DisplayUtils.dip2px(mContext, barHeight);
        viewHolder.hrStageView.setLayoutParams(paramsSeekbar);

        viewHolder.hrStageView.setLayoutParams(paramsSeekbar);
    }

    private void setImageHeightAndWith(ImageView imageView, float height, float width) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//        params.height = DisplayUtils.dip2px(mContext, height);
//        params.width = DisplayUtils.dip2px(mContext, width);
        params.height = (int) height;
        params.width = (int) width;
        imageView.setLayoutParams(params);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        HeartRateStageView hrStageView;
        CircleImageView iv_head;
        ImageView iv_one;
        ImageView iv_two;
        ImageView iv_three;
        ImageView iv_center;

        TextView tv_certer_value;
        TextView tv_one;
        TextView tv_two;
        TextView tv_three;

        TextView tv_name;
        TextView tv_match;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);

            iv_one = itemView.findViewById(R.id.iv_one);
            iv_two = itemView.findViewById(R.id.iv_two);
            iv_three = itemView.findViewById(R.id.iv_three);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_match = itemView.findViewById(R.id.tv_match);
            tv_one = itemView.findViewById(R.id.tv_one);
            tv_two = itemView.findViewById(R.id.tv_two);
            tv_three = itemView.findViewById(R.id.tv_point);
            tv_certer_value = itemView.findViewById(R.id.tv_point_value);
            iv_center = itemView.findViewById(R.id.iv_center);
            hrStageView = itemView.findViewById(R.id.hrStageView);

        }
    }
}
