package com.example.xmlystudio.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xmlystudio.R;
import com.example.xmlystudio.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetworkErrorView;
    private View mEmptyView;
    private View networkErrorView;
    private OnRetryClickListener onRetryClickListener;

    public enum UIStatus {
        LOADING, SUCCESS, ERROR, EMPTY, NONE
    }


    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context, null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //
        init();
    }


    public void updateStatus(UIStatus status) {
        System.out.println("状态" + status);
        mCurrentStatus = status;

        //更新UI一定要在主线程上执行
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }


    /**
     * 初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        //加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        //根据状态设置是否可见
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? View.VISIBLE : View.GONE);


        //成功
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        //根据状态设置是否可见
        mSuccessView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? View.VISIBLE : View.GONE);


        //失败
        if (mNetworkErrorView == null) {
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        //根据状态设置是否可见
        mNetworkErrorView.setVisibility(mCurrentStatus == UIStatus.ERROR ? View.VISIBLE : View.GONE);


        //返回数据为空
        if (mEmptyView == null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        //根据状态设置是否可见
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? View.VISIBLE : View.GONE);

        System.out.println(mCurrentStatus  + "");
    }

    /**
     * 获取空视图
     *
     * @return
     */
    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view, this, false);
    }

    /**
     * 获取网络错误视图
     *
     * @return
     */
    private View getNetworkErrorView() {
        networkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view, this, false);
        networkErrorView.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新刷新界面
                if (onRetryClickListener != null) {
                    onRetryClickListener.onRetryClick();
                }
            }
        });
        return networkErrorView;

    }

    /**
     * 获取成功视图
     *
     * @return
     */
    protected abstract View getSuccessView(ViewGroup container);

    /**
     * 获取正在加载视图
     *
     * @return
     */
    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view, this, false);
    }


    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;

    }


    public interface OnRetryClickListener {
        void onRetryClick();
    }
}
