package com.example.xmlystudio.interfaces;

import com.example.xmlystudio.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetialViewCallBack> {

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


}
