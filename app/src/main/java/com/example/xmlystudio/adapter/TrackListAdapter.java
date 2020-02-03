package com.example.xmlystudio.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xmlystudio.R;
import com.example.xmlystudio.fragments.HistoryFragment;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.InnerHolder> {


    private static final String TAG = "TrackListAdapter";
    private List<Track> mDetailData = new ArrayList<>();
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;


    public void setItemClickListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(List<Track> detailData, int position);
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }
    public interface ItemLongClickListener {
        void onItemLongClick(Track track);
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
            //TODO 获取数据
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(View itemView) {
            super(itemView);
        }

    }

    public void setData(List<Track> tacks) {
        mDetailData.clear();
        mDetailData.addAll(tacks);
        notifyDataSetChanged();
    }


}
