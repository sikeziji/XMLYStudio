package com.example.xmlystudio.presenters;

import com.example.xmlystudio.base.BaseApplication;
import com.example.xmlystudio.interfaces.IPlayPresenter;
import com.example.xmlystudio.interfaces.IPlayerCallback;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.CommonTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private static final String TAG = "PlayerPresenter";

    private List<IPlayerCallback> mIPlayPresenters = new ArrayList<>();

    private final XmPlayerManager mPlayerManager;


    public static final int PLAY_MODEL_LIST_INT = 0;
    public static final int PLAY_MODEL_LIST_LOOP_INT = 1;
    public static final int PLAY_MODEL_RANDOM_INT = 2;
    public static final int PLAY_MODEL_SINGLE_LOOP_INT = 3;

    //sp's key and name
    public static final String PLAY_MODE_SP_NAME = "PlayMod";
    public static final String PLAY_MODE_SP_KEY = "currentPlayMode";
    private int mCurrentProgressPosition = 0;
    private int mProgressDuration = 0;
    private Track mCurrentTrack;
    private int mCurrentIndex = 0;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关的接口
        mPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关的接口
        mPlayerManager.addPlayerStatusListener(this);
        //需要记录当前的播放模式
//        mPlayModSp = BaseApplication.getAppContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);

    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }

        return sPlayerPresenter;
    }


    private boolean isPlayListSet = false;


    public void setPlayList(List<Track> list, int playIndex) {
        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(list, playIndex);
            isPlayListSet = true;
            mCurrentTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        } else {
            LogUtil.d(TAG, "player is  null");
        }
    }


    @Override
    public void play() {
        if (isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        mPlayerManager.pause();
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        //播放前一个节目
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playnext() {
        //播放下一个节目
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> playList = mPlayerManager.getPlayList();
            for (IPlayerCallback iPlayPresenter : mIPlayPresenters) {
                iPlayPresenter.onListLoaded(playList);
            }
        }

    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到index的位置进行播放
        if (mPlayerManager != null) {
            mPlayerManager.play();
        }


    }

    @Override
    public void seekTo(int progress) {
        //更新播放器的进度
        mPlayerManager.seekTo(progress);

    }

    @Override
    public boolean isPlay() {
        //返回当前是否正在播放
        return mPlayerManager.isPlaying();
    }


    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
        if (!mIPlayPresenters.contains(iPlayerCallback)) {
            mIPlayPresenters.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        if (mIPlayPresenters.contains(iPlayerCallback)) {
            mIPlayPresenters.remove(iPlayerCallback);
        }
    }


    //=============================广告物料 start ========================================
    @Override
    public void onStartGetAdsInfo() {

    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {

    }

    @Override
    public void onAdsStartBuffering() {

    }

    @Override
    public void onAdsStopBuffering() {

    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {

    }

    @Override
    public void onCompletePlayAds() {

    }

    @Override
    public void onError(int i, int i1) {

    }
    //=============================广告物料 end ========================================

    //=============================播放器相关的回调方法 start ========================================
    @Override
    public void onPlayStart() {
        for (IPlayerCallback iPlayerCallback : mIPlayPresenters) {
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        for (IPlayerCallback iPlayerCallback : mIPlayPresenters) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        for (IPlayerCallback iPlayerCallback : mIPlayPresenters) {
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {

    }

    @Override
    public void onSoundPrepared() {

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel currModel) {

        mCurrentIndex = mPlayerManager.getCurrentIndex();
        if (currModel instanceof Track) {
            Track currentTrack = (Track) currModel;
            mCurrentTrack = currentTrack;
            //跟新UI
            for (IPlayerCallback iPlayPresenter : mIPlayPresenters) {
                iPlayPresenter.onTrackUpdate(mCurrentTrack,mCurrentIndex);
            }
        }



    }

    @Override
    public void onBufferingStart() {

    }

    @Override
    public void onBufferingStop() {

    }

    @Override
    public void onBufferProgress(int i) {

    }

    @Override
    public void onPlayProgress(int currPos, int duration) {
        //单位是毫秒
        for (IPlayerCallback iPlayPresenter : mIPlayPresenters) {
            iPlayPresenter.onProgressChange(currPos, duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }
    //========================播放器回调方法 end===============================
}
