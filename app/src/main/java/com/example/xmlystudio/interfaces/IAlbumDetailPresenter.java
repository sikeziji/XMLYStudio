package com.example.xmlystudio.interfaces;

import com.example.xmlystudio.DetailActivity;

public interface IAlbumDetailPresenter {

    /**
     * 下拉刷新内容
     */
    void pull2RefreshMore();


    /**
     * 上拉加载更多
     */
    void loadMore();


    /**
     * 获取专辑详情
     *
     * @param albumId
     * @param page
     */
    void getAlumDetail(int albumId,int page);


    /**
     * 注册
     */
    void registerViewCallback(IAlbumDetialViewCallBack detialViewCallBack);

    /**
     * 取消注册
     */
    void unRegisterViewCallback(IAlbumDetialViewCallBack detialViewCallBack);

}
