package com.example.xmlystudio;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xmlystudio.adapter.PlayerTrackPagerAdapter;
import com.example.xmlystudio.base.BaseActivity;
import com.example.xmlystudio.interfaces.IPlayerCallback;
import com.example.xmlystudio.presenters.PlayerPresenter;
import com.example.xmlystudio.utils.LogUtil;
import com.example.xmlystudio.views.SobPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {


    private static final String TAG = "PlayActivity";
    private PlayerPresenter mPlayerPresenter;

    private ImageView mControlBtn;

    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mToalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurarionBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlay_pre;
    private ImageView mPlay_next;
    private TextView mTackTitleTv;
    private String mTrackTitleText;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager = false;
    private ImageView mPlayModeSwitchbtn;
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayModelRule = new HashMap<>();
    private XmPlayListControl.PlayMode mCurrentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;


    //1.默认的是： Play_MODEL_LIST
    //2. 切换到列表循环 ： PLAY_MODEL_LIST_LOOP
    //3. 切换到随机播放： PLAY_MODEL_RANDOM
    //4. 单曲循环： PLAY_MODEL_SINGLE_LOOP
    static {
        sPlayModelRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST, XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
        sPlayModelRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM);
        sPlayModelRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM, XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP);
        sPlayModelRule.put(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP, XmPlayListControl.PlayMode.PLAY_MODEL_LIST);
    }

    private ImageView mPlayerListBtn;
    private SobPopWindow mSobPopWindow;
    private ValueAnimator mEntrBgAnmator;
    private ValueAnimator mOutBgAnmator;
    private int mDuration = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //初始化View
        initView();
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        //注册ViewCallBack
        mPlayerPresenter.registerViewCallback(this);
        //初始化事件
        initEvent();
        initBgAnimation();

    }

    private void initBgAnimation() {
        mEntrBgAnmator = ValueAnimator.ofFloat(1.0f, 0.7f);
        mEntrBgAnmator.setDuration(mDuration);
        mEntrBgAnmator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //设置透明度
                updateBgAlpha(value);
            }
        });
        //退出
        mOutBgAnmator = ValueAnimator.ofFloat(0.7f,1.0f);
        mOutBgAnmator.setDuration(mDuration);
        mOutBgAnmator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //设置透明度
                updateBgAlpha(value);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }


    /**
     * 初始化事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前的状态是正在播放，那么就暂停

                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    //如果当前的状态是非播放，那么就开始播放
                    mPlayerPresenter.play();
                }
            }
        });
        mDurarionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFormUser) {
                if (isFormUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                //手离开拖动进度条的时候更新进度
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });

        mPlay_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放前一个节目
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });

        mPlay_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //播放下一个节目
                mPlayerPresenter.playnext();
            }
        });

        //设置ViewPage 的时间
        mTrackPageView.addOnPageChangeListener(this);

        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager = true;
                }
                return false;
            }
        });

        mPlayModeSwitchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //处理播放模式的切换

                //根据当前的mode 获取到下一个mode
                XmPlayListControl.PlayMode playMode = sPlayModelRule.get(mCurrentMode);
                //修改播放模式
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.switchPlayMode(playMode);
                    mCurrentMode = playMode;
                }
            }
        });

        mPlayerListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 展示播放列表
                mSobPopWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                //修改背景的渐变动画
                mEntrBgAnmator.start();

            }
        });
        mSobPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //Pop窗体消失之后，恢复透明度
                mOutBgAnmator.start();
            }
        });
    }

    public void updateBgAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attibutes = window.getAttributes();
        attibutes.alpha = alpha;
        window.setAttributes(attibutes);
    }

    /**
     * 根据当前的状态更新播放模式图标
     */
    private void updatePlayModeBtnImag() {
        int resId = R.drawable.selector_play_mode_list_order;
        switch (mCurrentMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_play_mode_list_order;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_paly_mode_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_paly_mode_list_order_looper;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_paly_mode_single_loop;
                break;
        }

        mPlayModeSwitchbtn.setImageResource(resId);
    }

    /**
     * 初始化相关视图控件
     */
    private void initView() {
        //播放按钮
        mControlBtn = findViewById(R.id.play_or_pause_btn);

        //进度条左边进度时间
        mToalDuration = findViewById(R.id.track_duration);

        //进度条右边显示进度时间
        mCurrentPosition = findViewById(R.id.current_position);

        //进度条控件
        mDurarionBar = findViewById(R.id.track_seek_bar);


        //上一首按钮
        mPlay_pre = findViewById(R.id.play_pre);

        //下一首按钮
        mPlay_next = findViewById(R.id.play_next);

        //标题
        mTackTitleTv = findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mTackTitleTv.setText(mTrackTitleText);
        }

        mTrackPageView = findViewById(R.id.track_pager_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //设置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);

        mPlayModeSwitchbtn = findViewById(R.id.player_mode_switch_btn);

        //播放列表
        mPlayerListBtn = findViewById(R.id.player_list);
        //初始化popwindow
        mSobPopWindow = new SobPopWindow();

    }

    //===============IPlayerCallback 的 相关接口 START===================
    @Override
    public void onPlayStart() {
        //開始播放 ,修改UI为暂停按钮
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_palyer_pause);
        }

    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_palyer_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_palyer_play);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {
//        LogUtil.d(TAG,"list -- > " + list);

        //把数据设置到适配器里面
        if (mTrackPagerAdapter != null) {
            mTrackPagerAdapter.setData(list);
        }

        //数据回来后，要给节目列表分发一份
        if (mSobPopWindow != null) {
            mSobPopWindow.setListData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode mode) {
        //更新播放模式，并修改UI
        mCurrentMode = mode;
        updatePlayModeBtnImag();
    }

    @Override
    public void onProgressChange(int currentDuration, int total) {
        mDurarionBar.setMax(total);
        //更新播放進度，更新进度条
        String totlaDuration;
        String currentPosition;
        if (total > 1000 * 60 * 60) {
            totlaDuration = mHourFormat.format(total);
            currentPosition = mHourFormat.format(currentDuration);
        } else {
            totlaDuration = mMinFormat.format(total);
            currentPosition = mMinFormat.format(currentDuration);
        }

        if (mToalDuration != null) {
            mToalDuration.setText(totlaDuration);
        }

        //跟新当前的时间
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }

        if (!mIsUserTouchProgressBar) {
            // 更新进度
            mDurarionBar.setProgress(currentDuration);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (track == null) {
            LogUtil.d(TAG, "onTrackUpdate -- > track null.");
            return;
        }
        this.mTrackTitleText = track.getTrackTitle();
        if (mTackTitleTv != null) {
            //设置当前的标题
            mTackTitleTv.setText(track.getTrackTitle());
        }

        //当节目改编的时候，我们就获取到当前节目的位置
        if (mTrackPageView != null) {
            mTrackPageView.setCurrentItem(playIndex, true);
        }

        //修改播放里里的播放位置
        if (mSobPopWindow != null) {
            mSobPopWindow.setCurrentPlayPosition(playIndex);
        }
    }


    //===============IPlayerCallback 的 相关接口 END===================


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.d(TAG, "position -- > " + position);
        //当页面选中的时候，就去切换播放的内容
        if (mPlayerPresenter != null && mIsUserSlidePager) {
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSlidePager = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
