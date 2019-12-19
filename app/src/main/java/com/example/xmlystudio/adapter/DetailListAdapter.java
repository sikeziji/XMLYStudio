package com.example.xmlystudio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xmlystudio.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mDetailData = new ArrayList<>();
    private View mItemView;
    //格式化时间
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);

        return new InnerHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //找到控件，设置数据
        View itemView = holder.itemView;
        //顺序ID
        TextView orderTvTv = itemView.findViewById(R.id.order_text);
        //标题
        TextView titleTv = itemView.findViewById(R.id.detail_item_title);
        //播放次数
        TextView playCountTv = itemView.findViewById(R.id.detail_item_play_count);
        //时长
        TextView durationTv = itemView.findViewById(R.id.detail_item_duration);
        //更新日期
        TextView updateDateTv = itemView.findViewById(R.id.detail_item_update_time);


        //设置数据
        Track track = mDetailData.get(position);
        //顺序标签
        orderTvTv.setText(position + "");
        //播放标题
        titleTv.setText(track.getTrackTitle() + "");
        //播放次数
        playCountTv.setText(track.getPlayCount() + "");
        //播放时长
        String durationTvTime = mDurationFormat.format(track.getDuration() * 1000);
        durationTv.setText(durationTvTime);
        //更新时间
        String updateTimeText = mSimpleDateFormat.format(track.getUpdatedAt());
        updateDateTv.setText(updateTimeText);


    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        //清除原先的数据
        mDetailData.clear();
        //添加新的数据
        mDetailData.addAll(tracks);

        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
