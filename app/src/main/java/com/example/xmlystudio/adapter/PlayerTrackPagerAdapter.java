package com.example.xmlystudio.adapter;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xmlystudio.R;
import com.example.xmlystudio.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayerTrackPagerAdapter extends PagerAdapter {


    private static final String TAG = "PlayerTrackPagerAdapter";
    private List<Track> mData = new ArrayList<>();


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_pager, container, false);
        container.addView(itemView);

        //设置数据
        //找到控件
        ImageView item = itemView.findViewById(R.id.track_pager);
        //设置图片
        Track track = mData.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();

        //用Picasso 设置图片
        if (item != null) {
            Picasso.with(container.getContext()).load(coverUrlLarge).into(item);
        }

        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<Track> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }
}
