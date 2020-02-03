package com.example.xmlystudio.presenters;

import com.example.xmlystudio.interfaces.IHistoryCallback;
import com.example.xmlystudio.interfaces.IHistoryPresenter;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

public class HistoryPresenter implements IHistoryPresenter {

    private static final String TAG = "HistoryPresenter";
    private static HistoryPresenter sHistoryPresenter = null;

    private HistoryPresenter() {
    }

    public static HistoryPresenter getHistoryPresenter() {
        if (sHistoryPresenter == null) {
            synchronized (HistoryPresenter.class) {
                if (sHistoryPresenter == null) {
                    sHistoryPresenter = new HistoryPresenter();
                }
            }
        }
        return sHistoryPresenter;
    }

    @Override
    public void listHistories() {
        LogUtil.d(TAG,"获取数据");
    }

    @Override
    public void addHistory(Track track) {

    }

    @Override
    public void delHistory(Track track) {

    }

    @Override
    public void cleanHistories() {

    }

    @Override
    public void registerViewCallback(IHistoryCallback iHistoryCallback) {

    }

    @Override
    public void unRegisterViewCallback(IHistoryCallback iHistoryCallback) {

    }
}
