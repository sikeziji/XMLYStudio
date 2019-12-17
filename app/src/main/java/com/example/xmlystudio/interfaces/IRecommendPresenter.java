package com.example.xmlystudio.interfaces;

public interface IRecommendPresenter {

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
