package com.example.xmlystudio.presenters;

import com.example.xmlystudio.interfaces.IRecommendPresenter;

public class RecommendPresenter implements IRecommendPresenter {


    private RecommendPresenter() {

    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void getRecommendList() {

    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }
}
