package com.example.xmlystudio.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xmlystudio.R;
import com.example.xmlystudio.base.BaseApplication;
import com.example.xmlystudio.interfaces.ISubscriptionCallback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PlayerTrackPagerAdapter extends PagerAdapter {


    private static final String TAG = "PlayerTrackPagerAdapter";
    private List<Track> mData = new ArrayList<>();
    private final int SUCCESS = 1;
    private ImageView mItem;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    mItem.setImageBitmap(bitmap);
                    System.out.println("设置成功");

//                    notifyDataSetChanged();
                    break;
            }
        }
    };


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_track_pager, container, false);
        container.addView(itemView);

        //设置数据
        //找到控件
        mItem = itemView.findViewById(R.id.track_pager);
        //设置图片
        Track track = mData.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();


        //用Picasso 设置图片
        if (coverUrlLarge.trim().length() == 0) {
            Picasso.with(container.getContext()).load(R.mipmap.network_error).into(mItem);
        } else {
            Picasso.with(container.getContext()).load(coverUrlLarge).into(mItem);
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
