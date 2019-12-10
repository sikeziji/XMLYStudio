package com.example.xmlystudio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.icu.util.ULocale;
import android.net.wifi.hotspot2.ConfigParser;
import android.os.Bundle;

import com.example.xmlystudio.adapter.IndicatorAdapter;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager contentPager;
    private IndicatorAdapter indicatorAdapter;
    private ViewPager mContentPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initEvent();
    }

    private void initEvent() {
        indicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                if(mContentPager != null) {
                    mContentPager.setCurrentItem(index,false);
                }
            }
        });


    }

    /**
     * 初始化视图
     */
    private void initView() {
        mMagicIndicator = this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator的适配器
        indicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(indicatorAdapter);

        //ViewPager
        contentPager = this.findViewById(R.id.content_pager);


        //把Viewpager绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, contentPager);

    }
}
