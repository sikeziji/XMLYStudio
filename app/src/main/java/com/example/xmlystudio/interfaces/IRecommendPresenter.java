package com.example.xmlystudio.interfaces;

import com.example.xmlystudio.base.IBasePresenter;

public interface IRecommendPresenter  extends IBasePresenter<IRecommendViewCallback> {

    /**
     * 获取推荐内容
     */
    void getRecommendList();


    /**
     * 下拉刷新内容
     */
    void pull2RefreshMore();


    /**
     * 上拉加载更多
     */
    void loadMore();

}
