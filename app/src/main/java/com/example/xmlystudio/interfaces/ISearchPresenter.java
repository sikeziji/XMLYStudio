package com.example.xmlystudio.interfaces;

import com.example.xmlystudio.base.IBasePresenter;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {


    /**
     * 进行搜索
     * @param keyWord
     */
    void  doSearch(String keyWord);

    /**
     * 重新搜索
     */
    void reSearch();


    /**
     * 加载更多的搜索结果
     */
    void loadMore();


    /**
     * 获取热词
     */
    void getHotWord();


    /**
     * 获取推荐的关键字（相关的关键字）
     * @param keyWord
     */
    void getRecommendWord(String keyWord);


}
