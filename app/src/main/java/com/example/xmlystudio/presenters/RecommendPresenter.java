package com.example.xmlystudio.presenters;

import android.support.annotation.Nullable;

import com.example.xmlystudio.data.XimalayApi;
import com.example.xmlystudio.interfaces.IRecommendPresenter;
import com.example.xmlystudio.interfaces.IRecommendViewCallback;
import com.example.xmlystudio.utils.Constants;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {


    private static final String TAG = "RecommendPresenter";

    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Album> mCurrentRecommend = null;
    private List<Album> mRecommendList;

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


    /**
     * 获取推荐内容，
     * 这个接口 ： 3.10.6 获取猜你喜欢专辑
     */
    @Override
    public void getRecommendList() {
        //如果内容不空的话，那么直接使用当前的内容
        if (mRecommendList != null && mRecommendList.size() > 0) {
            handlerRecommendResult(mRecommendList);
            return;
        }
        //获取推荐内容
        //封装参数
        //正在加载界面显示
        updateLoading();

        XimalayApi ximalayApi = XimalayApi.getXimalayApi();
        ximalayApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG,"thread name -- >" + Thread.currentThread().getName());
                //数据获取成功
                if(gussLikeAlbumList != null) {
                    LogUtil.d(TAG,"getRecommendList -- > from network..");
                    mRecommendList = gussLikeAlbumList.getAlbumList();
                    //数据回来以后，我们要去更新UI
                    //upRecommendUI(albumList);
                    handlerRecommendResult(mRecommendList);
                }
            }

            @Override
            public void onError(int i,String s) {
                //数据获取出错
                LogUtil.d(TAG,"error  -- > " + i);
                LogUtil.d(TAG,"errorMsg  -- > " + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onNetworkError();
            }
        }

    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if (albumList != null) {
            if (albumList.size() == 0) {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                //通知UI更新
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoaded(albumList);
                }
                //当前使用的数据赋值给mCurrentRecommend
                this.mCurrentRecommend = albumList;
            }
        } else {
            System.out.println("获取的数据为空");
        }

    }


    private void updateLoading() {
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }


    @Override

    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && mCallbacks.contains(callback))
            mCallbacks.remove(callback);
    }

    public List<Album> getCurrentRecommend() {
        return mCurrentRecommend;
    }
}
