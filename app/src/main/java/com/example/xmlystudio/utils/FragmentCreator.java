package com.example.xmlystudio.utils;

import com.example.xmlystudio.base.BaseFragment;
import com.example.xmlystudio.fragments.HistoryFragment;
import com.example.xmlystudio.fragments.RecommendFragment;
import com.example.xmlystudio.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentCreator {

    //推荐
    public final static int INDEX_RECOMMEND = 0;
    //订阅
    public final static int INDEX_SUBSCRIPITON = 1;
    //历史记录
    public final static int INDEX_HISTORY = 2;

    /**
     * 页面数量
     */
    public final static int PAGE_COUNT = 3;

    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index) {
        BaseFragment baseFragment = sCache.get(index);
        if (baseFragment != null) {
            return baseFragment;
        }

        switch (index) {
            case INDEX_RECOMMEND:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPITON:
                baseFragment = new SubscriptionFragment();
                break;
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;
        }

        sCache.put(index, baseFragment);
        return baseFragment;
    }

}
