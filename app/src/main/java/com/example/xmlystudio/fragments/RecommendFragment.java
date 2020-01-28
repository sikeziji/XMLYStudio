package com.example.xmlystudio.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.xmlystudio.DetailActivity;
import com.example.xmlystudio.R;
import com.example.xmlystudio.adapter.RecommendListAdapter;
import com.example.xmlystudio.base.BaseFragment;
import com.example.xmlystudio.interfaces.IRecommendViewCallback;
import com.example.xmlystudio.presenters.AlbumDetailPresenter;
import com.example.xmlystudio.presenters.RecommendPresenter;
import com.example.xmlystudio.utils.LogUtil;
import com.example.xmlystudio.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, RecommendListAdapter.OnRecommendItemClickLinstener,UILoader.OnRetryClickListener {

    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter recommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater inflater, ViewGroup container) {

        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                LogUtil.d(TAG,"创建成功视图");
                return createSuccessView(inflater, container);
            }
        };


        //获取到逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先设置通知接口的注册
        mRecommendPresenter.registerViewCallback(this);


        //获取推荐列表内容
        mRecommendPresenter.getRecommendList();


        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);

        }


        mUiLoader.setOnRetryClickListener(this);

        //返回View ，给界面显示
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater inflater, ViewGroup container) {
        //View加载完成
        mRootView = inflater.inflate(R.layout.fragment_recommend, container, false);

        //RecycleView 的使用

        //1.找到对应的空间
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //设置为横向
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置layoutManager
        mRecommendRv.setLayoutManager(linearLayoutManager);

        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });

        //3.设置适配器
        recommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(recommendListAdapter);

        recommendListAdapter.setOnRecommendItemClickLinstener(this);

        return mRootView;
    }


    private void upRecommendUI(List<Album> albumList) {
        //把数据设置给适配器，并且更新UI
        recommendListAdapter.setData(albumList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecommendPresenter != null) {
            //取消接口的注册
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRecommendListLoaded(List<Album> result) {
        LogUtil.d(TAG,result.toString());
        //获取完成推荐内容，该方法就会被调用成功
        recommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        LogUtil.d(TAG,"onNetworkError");
        mUiLoader.updateStatus(UILoader.UIStatus.ERROR);
    }

    @Override
    public void onEmpty() {
        LogUtil.d(TAG,"onEmpty");
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);

    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG,"onLoading");
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onRetryClick() {
        //表示网络不佳的时候，用户点击重试
        //重新加载推荐的内容
        if (mRecommendPresenter != null) {
            //重新获取数据即可
            mRecommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position , Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        //Item被点击了,跳转到详情界面
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
