package com.example.xmlystudio.interfaces;

import com.example.xmlystudio.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayPresenter extends IBasePresenter<IPlayerCallback> {

    /**
     * 播放
     */
    void  play();


    /**
     * 暂停
     */
    void pause();


    /**
     * 停止播放
     */
    void stop();


    /**
     *  播放上一首
     */
    void playPre();

    /**
     * 播放下一首
     */
    void playnext();

    /**
     * 切换播放模式
     *
     * @param mode  播放类型
     */
    void switchPlayMode(XmPlayListControl.PlayMode mode);


    /**
     * 获取播放列表
     */
    void getPlayList();


    /**
     * 根据节目的位置进行播放
     * @param index 节目在列表中的位置
     */
    void  playByIndex(int index);


    /**
     * 切换播放进度
     * @param progress 进度位置
     */
    void seekTo(int progress);


    /**
     * 判断是否正在播放
     * @return
     */
    boolean isPlay();
}