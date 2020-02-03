package com.example.xmlystudio.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetialViewCallBack {

    /**
     * 专辑详情内容加载
     *
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);


    /**
     * 网络错误
     * @param errorCode
     * @param errorMsg
     */
    void OnNetWorkError(int errorCode, String errorMsg);


    /**
     * 把album传UI使用
     *
     * @param album
     */
    void onAlbumLoaded(Album album);




    /**
     * 下拉加载更多的结果
     *
     * @param size size>0表示加载成功，否则表示加载失败.
     */
    void onRefreshFinished(int size);




    /**
     * 下拉加载更多的结果
     *
     * @param size size>0表示加载成功，否则表示加载失败.
     */
    void onLoaderMoreFinished(int size);
}
