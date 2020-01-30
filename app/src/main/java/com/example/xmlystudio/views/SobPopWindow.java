package com.example.xmlystudio.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xmlystudio.R;
import com.example.xmlystudio.adapter.PlayListAdapter;
import com.example.xmlystudio.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class SobPopWindow extends PopupWindow {


    private final View mPopView;
    private TextView mCloseBtn;
    private RecyclerView mTracksList;
    private PlayListAdapter mPlayListAdapter;

    public SobPopWindow() {
        //设置它的宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //这里注意设置setOUtsideTouchable之前要先设置setBackGroundDrawable;
        //否则点击外部无法关闭pop
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置点击外部关闭
        setOutsideTouchable(true);
        //
        setTouchable(true);
        setFocusable(true);

        //载进来View
        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        //设置内容
        setContentView(mPopView);
        //设置窗口进入和退出的动画
        setAnimationStyle(R.style.pop_animation);

        initView();
        initEvent();
    }

    private void initView() {
        //关闭按钮
        mCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);

        //数据列表
        mTracksList = mPopView.findViewById(R.id.play_list_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mTracksList.setLayoutManager(linearLayoutManager);
        //设置适配器 TODO：
        mPlayListAdapter = new PlayListAdapter();
        mTracksList.setAdapter(mPlayListAdapter);
    }

    private void initEvent() {
        //点击关闭以后，pop窗口消失
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SobPopWindow.this.dismiss();
            }
        });
    }


    /**
     * 给适配器设置数据
     * @param data
     */
    public void setListData(List<Track> data) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setData(data);
        }
    }

    public void setCurrentPlayPosition(int position) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setCurrentPlayPosition(position);
            mTracksList.scrollToPosition(position);
        }
    }

    public interface PlayListItemClickListener {
        void onItemClick(int position);
    }
}
