package com.example.xmlystudio.presenters;

import android.support.annotation.Nullable;

import com.example.xmlystudio.interfaces.IAlbumDetailPresenter;
import com.example.xmlystudio.interfaces.IAlbumDetialViewCallBack;
import com.example.xmlystudio.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter";

    private Album album = null;

    private List<IAlbumDetialViewCallBack> mCallbacks = new ArrayList<>();

    private List<Track> mTracks = new ArrayList<>();
    //当前的专辑id
    private int mCurrentAlbumId = -1;
    //当前的页面
    private int mCurrentPageIndex = 0;

    private AlbumDetailPresenter() {
    }


    private static AlbumDetailPresenter sInstance = null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                sInstance = new AlbumDetailPresenter();
            }
        }
        return sInstance;
    }


    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {
        //去加载更多内容
        mCurrentPageIndex++;

        //传入True ，表示结果会追加到列表的后方
        doLoaded(true);
    }

    private void doLoaded(final boolean isLoaderMore) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, mCurrentAlbumId + "");
        map.put(DTransferConstants.PAGE, mCurrentPageIndex + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    if (isLoaderMore) {
                        //这个是上拉加载，结果放到后面去
                        mTracks.addAll(mTracks.size() - 1, tracks);
                    } else {
                        //这个是下拉加载，结果放到前面去
                        mTracks.addAll(0, tracks);
                    }
                    handlerAlbumDetailResult(mTracks);

                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (isLoaderMore) {
                    mCurrentPageIndex--;
                }
                handlerError(errorCode, errorMsg);
            }
        });
    }


    @Override
    public void getAlumDetail(int albumId, int page) {
        mTracks.clear();
        this.mCurrentAlbumId = albumId;
        this.mCurrentPageIndex = page;
        //根据页码和专辑ID获取
        doLoaded(false);

    }

    /**
     * 如果发生错我，通知UI
     *
     * @param errorCode
     * @param errorMsg
     */
    private void handlerError(int errorCode, String errorMsg) {

        for (IAlbumDetialViewCallBack callback : mCallbacks) {
            callback.OnNetWorkError(errorCode, errorMsg);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetialViewCallBack mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetialViewCallBack detialViewCallBack) {
        if (mCallbacks != null && !mCallbacks.contains(detialViewCallBack)) {
            mCallbacks.add(detialViewCallBack);
            if (album != null) {
                detialViewCallBack.onAlbumLoaded(album);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetialViewCallBack detialViewCallBack) {
        if (mCallbacks != null && mCallbacks.contains(detialViewCallBack)) {
            mCallbacks.remove(detialViewCallBack);
        }
    }


    public void setTargetAlbum(Album album) {
        this.album = album;
    }

}
