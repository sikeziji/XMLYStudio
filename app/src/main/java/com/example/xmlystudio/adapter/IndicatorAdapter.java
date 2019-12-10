package com.example.xmlystudio.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.example.xmlystudio.R;
import com.ximalaya.ting.android.opensdk.auth.utils.c;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class IndicatorAdapter extends CommonNavigatorAdapter {

    /**
     * title  头部名称
     */
    private String[] title;

    /**
     * 移动操作
     */
    private OnIndicatorTapClickListener mOnIndicatorTapClickListener;

    public IndicatorAdapter(Context context) {
        title = context.getResources().getStringArray(R.array.indicator_title);
    }


    @Override
    public int getCount() {
        if (title != null) {
            return title.length;
        }
        return 0;
    }


    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        //创建View
        SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        //设置一般情况下颜色为灰色
        simplePagerTitleView.setNormalColor(Color.GRAY);
        //设置选中情况下颜色为黑色
        simplePagerTitleView.setSelectedColor(Color.WHITE);
        //设置单位
        simplePagerTitleView.setTextSize(18);
        //设置显示的内容
        simplePagerTitleView.setText(title[index]);
        //设置title的点击事件，如果点击了title，那么就选中下面的viewPager到队形的index 里面去
        //也就是说当我们点击了title的时候，下面的viewpager会对应这index进行切换内容
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换viewPager的内容，如果index不一样的话
                if (mOnIndicatorTapClickListener != null) {
                    mOnIndicatorTapClickListener.onTabClick(index);
                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(Color.parseColor("#ffffff"));
        return indicator;
    }

    public void setOnIndicatorTapClickListener(OnIndicatorTapClickListener listener) {
        this.mOnIndicatorTapClickListener = listener;
    }


   public  interface OnIndicatorTapClickListener {
        void onTabClick(int index);
    }
}
