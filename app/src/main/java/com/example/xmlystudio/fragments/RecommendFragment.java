package com.example.xmlystudio.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xmlystudio.R;
import com.example.xmlystudio.adapter.RecommendListAdapter;
import com.example.xmlystudio.base.BaseFragment;
import com.example.xmlystudio.utils.Constants;
import com.example.xmlystudio.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {

    private static final String TAG = "RecommendFragment";
    private View view;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter recommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        //View加载完成
        view = inflater.inflate(R.layout.fragment_recommend, container, false);

        //RecycleView 的使用
        //1.找到对应的空间
        mRecommendRv = view.findViewById(R.id.recommend_list);
        //2.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //设置为横向
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置layoutManager
        mRecommendRv.setLayoutManager(linearLayoutManager);

        //3.设置适配器
        recommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(recommendListAdapter);
        //设置步骤

        //去拿数据回来
        getRecommendData();

        //返回View ，给界面显示
        return view;
    }

    /**
     * 获取推荐内容，
     * 这个接口 ： 3.10.6 获取猜你喜欢专辑
     */
    private void getRecommendData() {

        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                //获取数据成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();

                    upRecommendUI(albumList);
                    LogUtil.d(TAG, "size -- >> " + albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取数据失败
                LogUtil.d(TAG, "error ->> " + i);
                LogUtil.d(TAG, "errorMsg ->> " + s);
            }
        });

    }

    private void upRecommendUI(List<Album> albumList) {
        //把数据设置给适配器，并且更新UI
        recommendListAdapter.setData(albumList);
    }

}
