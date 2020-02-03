package com.example.xmlystudio.presenters;

import android.support.annotation.Nullable;

import com.example.xmlystudio.data.XimalayApi;
import com.example.xmlystudio.interfaces.ISearchCallback;
import com.example.xmlystudio.interfaces.ISearchPresenter;
import com.example.xmlystudio.utils.Constants;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {


    private static final String TAG = "SearchPresenter";
    private List<Album> mSearchResult = new ArrayList<>();
    //XimalayApi
    private XimalayApi mXimalayApi;
    //自定义集合
    private List<ISearchCallback> mCallback = new ArrayList<>();
    //当前的搜索关键字
    private String mCurrentKeyword = null;

    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private boolean mIsLoadMore = false;

    private SearchPresenter() {
        mXimalayApi = XimalayApi.getXimalayApi();
    }

    private static SearchPresenter sSearchPresenter = null;


    public static SearchPresenter getSearchPresenter() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }


    @Override
    public void doSearch(String keyWord) {
        mCurrentPage = DEFAULT_PAGE;
        mSearchResult.clear();
        //用于得新搜索
        //当网络不好的时候 ,用户会点击重新搜索
        this.mCurrentKeyword = keyWord;
        search(keyWord);
    }

    private void search(String keyword) {
        mXimalayApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                mSearchResult.addAll(albums);
                if (albums != null) {
                    LogUtil.d(TAG, "albums size -- > " + albums.size());
                    if (mIsLoadMore) {
                        for (ISearchCallback iSearchCallback : mCallback) {
                            iSearchCallback.onLoadMoreResult(mSearchResult, albums.size() != 0);
                        }
                        mIsLoadMore = false;
                    } else {
                        for (ISearchCallback iSearchCallback : mCallback) {
                            iSearchCallback.onSearchResultLoaded(mSearchResult);
                        }
                    }
                } else {
                    LogUtil.d(TAG, "album is null..");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode -- > " + errorCode);
                LogUtil.d(TAG, "errorMsg -- > " + errorMsg);
                for (ISearchCallback iSearchCallback : mCallback) {
                    if (mIsLoadMore) {
                        iSearchCallback.onLoadMoreResult(mSearchResult, false);
                        mCurrentPage--;
                        mIsLoadMore = false;
                    } else {
                        iSearchCallback.onError(errorCode, errorMsg);
                    }
                }

            }
        });
    }

    @Override
    public void reSearch() {
        search(mCurrentKeyword);
    }

    @Override
    public void loadMore() {
        //判断有没有必要进行加载更多
        if (mSearchResult.size() < Constants.COUNT_DEFAULT) {
            for (ISearchCallback iSearchCallback : mCallback) {
                iSearchCallback.onLoadMoreResult(mSearchResult, false);
            }
        } else {
            mIsLoadMore = true;
            mCurrentPage++;
            search(mCurrentKeyword);
        }
    }

    @Override
    public void getHotWord() {
//todo:做一个热词缓存
        mXimalayApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    LogUtil.d(TAG, "hotWords size -- > " + hotWords.size());
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWord errorCode -- > " + errorCode);
                LogUtil.d(TAG, "getHotWord errorMsg -- > " + errorMsg);
            }
        });
    }

    @Override
    public void getRecommendWord(String keyWord) {
        mXimalayApi.getSuggestWord(keyWord, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(@Nullable SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    LogUtil.d(TAG, "keyWordList size -- > " + keyWordList.size());
                    for (ISearchCallback iSearchCallback : mCallback) {
                        iSearchCallback.onRecommendWordLoaded(keyWordList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getRecommendWord errorCode -- > " + errorCode);
                LogUtil.d(TAG, "getRecommendWord errorMsg -- > " + errorMsg);
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallback.contains(iSearchCallback)) {
            mCallback.add(iSearchCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISearchCallback iSearchCallback) {
        mCallback.remove(iSearchCallback);
    }
}
