package com.example.xmlystudio.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

public interface ISearchCallback {


    /**
     * 搜索结果的回调方法
     *
     * @param result
     */
    void onSearchResultLoaded(List<Album> result);


    /**
     * 加载更多的结果返回
     * @param result 结果集合
     * @param isOkay  true 表示加载成功 ， false 表示没有更多
     */
    void onLoadMoreResult(List<Album> result,boolean isOkay);


    /**
     *  联想关键字的结果回调方法
     * @param keyWordList
     */
    void onRecommendWordLoaded(List<QueryResult> keyWordList);



    /**
     * 获取推荐热词的结果回调方法
     *
     * @param hotWordList
     */
    void onHotWordLoaded(List<HotWord> hotWordList);


    /**
     * 错误通知
     * @param errorCode  错误码
     * @param errorMsg 错误信息
     */
    void onError(int errorCode,String errorMsg);
}
