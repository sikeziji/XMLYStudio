package com.example.xmlystudio.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.xmlystudio.PlayActivity;
import com.example.xmlystudio.R;
import com.example.xmlystudio.adapter.TrackListAdapter;
import com.example.xmlystudio.base.BaseApplication;
import com.example.xmlystudio.base.BaseFragment;
import com.example.xmlystudio.interfaces.IHistoryCallback;
import com.example.xmlystudio.presenters.HistoryPresenter;
import com.example.xmlystudio.presenters.PlayerPresenter;
import com.example.xmlystudio.views.ConfirmCheckBoxDialog;
import com.example.xmlystudio.views.UILoader;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class HistoryFragment extends BaseFragment implements IHistoryCallback, TrackListAdapter.ItemClickListener, TrackListAdapter.ItemLongClickListener, ConfirmCheckBoxDialog.OnDialogActionClickListener {

    private FrameLayout view;
    private UILoader mUiLoader;
    private HistoryPresenter mHistoryPresenter;
    private TwinklingRefreshLayout mRefreshLayout;

    private RecyclerView mHistoryList;
    private TrackListAdapter mTrackListAdapter;
    private Track mCurrentClickHistoryItem;

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        view = (FrameLayout) inflater.inflate(R.layout.fragment_history, container, false);

        if (mUiLoader == null) {
            mUiLoader = new UILoader(BaseApplication.getAppContext()) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }

                @Override
                protected View getEmptyView() {
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
                    TextView tips = emptyView.findViewById(R.id.empty_view_tips_tv);
                    tips.setText("没有历史记录呢！");
                    return emptyView;
                }
            };
        } else {
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
        }

        mHistoryPresenter = HistoryPresenter.getHistoryPresenter();
        //注册事件
        mHistoryPresenter.registerViewCallback(this);
        //设置默认加载
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        //获取历史内容
        mHistoryPresenter.listHistories();
        //添加状态View到默认View
        view.addView(mUiLoader);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHistoryPresenter != null) {
            mHistoryPresenter.unRegisterViewCallback(this);
        }
    }

    private View createSuccessView(ViewGroup container) {

        View successView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_histor, container, false);
        mRefreshLayout = successView.findViewById(R.id.over_scroll_view);
        //设置mRefreshLayout 的属性
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableOverScroll(true);

        mHistoryList = mRefreshLayout.findViewById(R.id.history_list);
        mHistoryList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //设置item的上下间距
        mHistoryList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 2);
                outRect.right = UIUtil.dip2px(view.getContext(), 2);
            }
        });

        //设置适配器
        mTrackListAdapter = new TrackListAdapter();
        mTrackListAdapter.setItemClickListener(this);
        mTrackListAdapter.setItemLongClickListener(this);
        mHistoryList.setAdapter(mTrackListAdapter);

        return successView;
    }

    @Override
    public void onHistoriesLoaded(List<Track> tracks) {

        if(tracks == null || tracks.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        } else {
            //更新数据
            mTrackListAdapter.setData(tracks);
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放器的数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayList(detailData,position);
        //跳转到播放器界面
        Intent intent = new Intent(getActivity(), PlayActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Track track) {
        this.mCurrentClickHistoryItem = track;
        //去删除历史
        //Toast.makeText(getActivity(),"历史记录长按..." + track.getTrackTitle(),Toast.LENGTH_SHORT).show();
        ConfirmCheckBoxDialog dialog = new ConfirmCheckBoxDialog(getActivity());
        dialog.setOnDialogActionClickListener(this);
        dialog.show();
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onConfirmClick(boolean isCheck) {
        //去删除历史
        if(mHistoryPresenter != null && mCurrentClickHistoryItem != null) {
            if(!isCheck) {
                mHistoryPresenter.delHistory(mCurrentClickHistoryItem);
            } else {
                mHistoryPresenter.cleanHistories();
            }
        }
    }
}
