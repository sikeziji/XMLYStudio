package com.example.xmlystudio.presenters;

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


    private Album album = null;

    private List<IAlbumDetialViewCallBack> mCallbacks = new ArrayList<>();
    private static final String TAG = "AlbumDetailPresenter";

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

    }

    @Override
    public void getAlumDetail(int albumId, int page) {
        //根据页码和专辑ID获取
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.PAGE, page + "");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                List<Track> tracks = trackList.getTracks();
                handlerAlbumDetailResult(tracks);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

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
