package com.example.xmlystudio;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xmlystudio.adapter.DetailListAdapter;
import com.example.xmlystudio.base.BaseActivity;
import com.example.xmlystudio.interfaces.IAlbumDetialViewCallBack;
import com.example.xmlystudio.presenters.AlbumDetailPresenter;
import com.example.xmlystudio.utils.ImageBlur;
import com.example.xmlystudio.utils.LogUtil;
import com.example.xmlystudio.views.RoundRectImageView;
import com.example.xmlystudio.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetialViewCallBack, UILoader.OnRetryClickListener, DetailListAdapter.ItemClickListener {


    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private static final String TAG = "DetailActivity";
    private int mCurrentPage = 1;
    private RecyclerView mDetailList;
    private LinearLayoutManager mLayoutManager;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        initView();


        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

    }

    private void initView() {

        mDetailListContainer = this.findViewById(R.id.detail_list_container);

        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    System.out.println("获取信息成功");
                    return createSuccessView(container);
                }

            };

            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);

            mUiLoader.setOnRetryClickListener(DetailActivity.this);
        }
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.viv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAthor = this.findViewById(R.id.tv_album_athor);


        //设置数据


    }

    private View createSuccessView(ViewGroup container) {

        View inflate = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);


        mDetailList = inflate.findViewById(R.id.album_detail_list);

        mLayoutManager = new LinearLayoutManager(this);

        //设置LayoutManger  布局管理器
        mDetailList.setLayoutManager(mLayoutManager);
        //设置适配器
        mDetailListAdapter = new DetailListAdapter();
        System.out.println("mDetailListContainer == null " + (mDetailListContainer == null));
        mDetailList.setAdapter(mDetailListAdapter);
        //设置每个item的上下间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });

        mDetailListAdapter.setItemClickListener(this);
        return inflate;
    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        System.out.println(tracks.size());
        //判断数据结果、根据结果显示
        if (tracks == null || tracks.size() == 0) {
            //拿数据，显示Loading状态
            if (mUiLoader != null) {
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);

            }
        }

        //更新/设置UI
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }

        //成功拿到数据
        System.out.println("mDetailListContainer != null " + (mDetailListContainer != null));
        if (mDetailListAdapter != null) {
            mDetailListAdapter.setData(tracks);
        }

    }

    @Override
    public void OnNetWorkError(int errorCode, String errorMsg) {
        //请求发生错误，显示网络异常状态
        mUiLoader.updateStatus(UILoader.UIStatus.ERROR);
    }

    @Override
    public void onAlbumLoaded(Album album) {

        long id = album.getId();

        LogUtil.d(TAG, "album ->>" + album.getId());

        mCurrentId = id;

        //获取专辑的详情内容

        //TODO
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlumDetail((int) id, mCurrentPage);
        }

        //拿数据，显示Loading状态
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);

        }

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }

        if (mAlbumAthor != null) {
            mAlbumAthor.setText(album.getAnnouncer().getNickname());
        }

        /**
         * 毛玻璃效果
         */
        if (mSmallCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
        }

        if (mLargeCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = mLargeCover.getDrawable();
                    if (drawable != null) {
                        ImageBlur.makeBlur(mLargeCover, DetailActivity.this);
                    }
                }

                @Override
                public void onError() {
                    LogUtil.d(TAG, "获取图片出错");
                }
            });
        }


    }

    @Override
    public void onRetryClick() {
        //表示用户网络不佳的时候，去点击了重新加载
        //获取专辑的详情内容

        //TODO
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlumDetail((int) mCurrentId, mCurrentPage);
        }

    }

    @Override
    public void onItemClick() {
        //跳转界面
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
}
