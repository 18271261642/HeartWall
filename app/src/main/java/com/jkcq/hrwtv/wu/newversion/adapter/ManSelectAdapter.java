package com.jkcq.hrwtv.wu.newversion.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.wu.newversion.view.CircleImageView;

public class ManSelectAdapter extends RecyclerView.Adapter<ManSelectAdapter.MyViewHolder> {

    private Context mContext;

    public ManSelectAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_man_select, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.roundimg_head.setImageResource(R.mipmap.icon_default_head);
        if (position % 5 == 0) {
            holder.tv_state.setVisibility(View.GONE);
        }
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //   holder.itemView.setBackgroundResource(R.drawable.shape_linear_sport_state_three);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.shape_bg_white);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView roundimg_head;
        TextView tv_name;
        TextView tv_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            roundimg_head = itemView.findViewById(R.id.roundimg_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }
}
