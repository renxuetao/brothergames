package com.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activty.MainActivity;
import com.adapter.GameGridviewAdapter;
import com.bean.GameBean;
import com.brother.games.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.widget.CircleImageView;
import com.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class GameFragment extends Fragment implements OnClickListener {

    private static final long DOUBLE_CLICK_TIME = 1000;
    private static final long REFRESH_TIME = 5 * 60 * 1000;
    private static boolean REFRESH_RECENT_LIST = false;

    private DisplayImageOptions options;

    private TextView title = null;//中间文字
    private TextView error_tv = null;
    private GridViewWithHeaderAndFooter gridview = null;
    private GridViewWithHeaderAndFooter head_gridview = null;
    private View headerView = null;
    private View footerView = null;
    private View view = null;
    private LinearLayout game_recent_root_ll = null;
    private CircleImageView my_button = null;
    private RelativeLayout complete_fl = null;

    public ArrayList<GameBean> allMembers = null;
    public ArrayList<GameBean> playedGameList = null;
    private GameGridviewAdapter adapter = null;
    private GameGridviewAdapter headAdapter = null;
    private long page = 0;
    private int count = 0;
    private long GameCount = 0;
    private MainActivity activity;
    private long refreshModify = 0;
    private int mType;
    private String myCover = "";

    private PullToRefreshGridView mPullToRefreshGridView;
    private ImageView red_dot_iv;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.game_home2, container, false);
        initPage(0, 1);
        //UI
        initView();
        return view;
    }

    /**
     * @param type 0==正常进入   1==刷新
     */
    public void initPage(int type, int State) {
        this.mType = type;
        if (isFastDoubleClick() && type == 1) {
            return;
        }
        if (State == 0) {
            //显示下拉红心
            mPullToRefreshGridView.showhead();
            //启动定时器，定时取消红心
            new InitPullToRefresh().execute();
        }
        if (mType == 1) {
            page = 0;
        }
    }

    private long lastClickTime = 0;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < DOUBLE_CLICK_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private void initView() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon72)
                .showImageForEmptyUri(R.drawable.icon72)//下载失败
                .showImageOnFail(R.drawable.icon72)//解码失败
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getString(R.string.game_title));
        error_tv = (TextView) view.findViewById(R.id.error_tv);
        complete_fl = (RelativeLayout)view.findViewById(R.id.complete_fl);
        complete_fl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        mPullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.gridview);
        mPullToRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        //上拉监听函数
        mPullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridViewWithHeaderAndFooter>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {
                //执行刷新函数
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {

            }
        });

        gridview = mPullToRefreshGridView.getRefreshableView();
        gridview.setSelector(R.color.transparent);

        // 滑动监听
        gridview.setOnScrollListener(new ScrollListener());
        //列表监听
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameBean item = allMembers.get(i);
                if (item.GameType == 0 || item.GameType == 1) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("type", item.IsVertical);
//                    bundle.putString("name", item.GameName);
//                    String sig = com.utils.Util.encryptMD5(MyApplication.getInstance().interaction.user.account + "-" + MyApplication.getInstance().interaction.user.token);
//                    String url = MyApplication.getInstance().getWebUrl() + item.Url;
//                    if (url.indexOf("?") > 0) {
//                        url = url + "&account=" + MyApplication.getInstance().interaction.user.account + "&sig=" + sig;
//
//                    } else {
//                        url = url + "?account=" + MyApplication.getInstance().interaction.user.account + "&sig=" + sig;
//                    }
//                    bundle.putString("url", url);
//                    activity.startActivity(GameWebViewActivity.class, bundle);
                }
                REFRESH_RECENT_LIST = true;
            }
        });

        allMembers = new ArrayList<GameBean>();
        adapter = new GameGridviewAdapter(activity, allMembers, activity.mScreenWidth);
        //底部布局
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.game_home_gridview_head, null);
        game_recent_root_ll = (LinearLayout) headerView.findViewById(R.id.game_recent_root_ll);
        head_gridview = (GridViewWithHeaderAndFooter) headerView.findViewById(R.id.head_gridview);
        head_gridview.setSelector(R.color.transparent);
        //列表监听
        head_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameBean item = playedGameList.get(i);
                if (item.GameType == 0 || item.GameType == 1) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("type", item.IsVertical);
//                    bundle.putString("name", item.GameName);
//                    String sig = com.util.Util.encryptMD5(MyApplication.getInstance().interaction.user.account + "-" + MyApplication.getInstance().interaction.user.token);
//                    String url = MyApplication.getInstance().getWebUrl() + item.Url;
//                    LogUtil.d("yjw", "WebUrl===" + url);
//                    if (url.indexOf("?") > 0) {
//                        url = url + "&account=" + MyApplication.getInstance().interaction.user.account + "&sig=" + sig;
//
//                    } else {
//                        url = url + "?account=" + MyApplication.getInstance().interaction.user.account + "&sig=" + sig;
//                    }
//                    bundle.putString("url", url);
//                    activity.startActivity(GameWebViewActivity.class, bundle);
                }
                REFRESH_RECENT_LIST = true;
            }
        });
        playedGameList = new ArrayList<GameBean>();

        headAdapter = new GameGridviewAdapter(activity, playedGameList, activity.mScreenWidth);
        head_gridview.setAdapter(headAdapter);

        game_recent_root_ll.setVisibility(View.GONE);

        gridview.addHeaderView(headerView, null, false);

        footerView = inflater.inflate(R.layout.xlistview_footer_sexting, null);
        gridview.addFooterView(footerView, null, false);
        gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        error_tv.setVisibility(View.GONE);

        red_dot_iv = (ImageView) view.findViewById(R.id.red_dot_iv);
    }

    /**
     * 下拉刷新
     */
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            if (mPullToRefreshLayout != null) {
//                mPullToRefreshLayout.refreshFinish();
//            }
            if (mPullToRefreshGridView != null) {
                mPullToRefreshGridView.setState(PullToRefreshBase.State.RESET);
            }
            initPage(1,1);
        }
    }

    // 隐藏桃心通话
    private class InitPullToRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPullToRefreshGridView.setState(PullToRefreshBase.State.RESET);
        }
    }

    private boolean loadfinish = true;     // 指示数据是否加载完成
    private boolean isRuning = false;    //线程是否已经启动

    private final class ScrollListener implements AbsListView.OnScrollListener {

        @SuppressLint("NewApi")
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > visibleItemCount && GameCount > 0) {// && messageCountEnd > 0
                int lastItemid = gridview.getLastVisiblePosition(); // 获取当前屏幕最后Item的ID
                if ((lastItemid + 1) == totalItemCount) { // 达到数据的最后一条记录
                    if (totalItemCount > 0 && !isRuning) {
                        // 当前页
                        if (allMembers.size() < GameCount && loadfinish) {
                            isRuning = true;
                            loadfinish = false;
                            //gridview.addFooterView(footerView, null, false);// 添加页脚（放在ListView最后）
                            footerView.setVisibility(View.VISIBLE);
                            // 开一个线程加载数据
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mType = 0;
                                    isRuning = false;
                                }
                            }).start();
                        }
                    }
                }
            } else {
                if (gridview.getFooterViewCount() > 0) { // 如果有底部视图
                    //gridview.removeFooterView(footerView);
                    footerView.setVisibility(View.GONE);
                    loadfinish = true; // 加载完成
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }
    }

    public void refreshChatList() {
        // 告诉ListView数据已经发生改变，要求ListView更新界面显示
        adapter.notifyDataSetChanged();
        //隐藏尾部界面
        if (gridview.getFooterViewCount() > 0) { // 如果有底部视图
            //gridview.removeFooterView(footerView);
            footerView.setVisibility(View.GONE);
            loadfinish = true; // 加载完成
        }
    }

    public void refreshPlayedGameList() {
        if(playedGameList.size() <= 0){
            game_recent_root_ll.setVisibility(View.GONE);
        }else{
            GameGridviewAdapter currentAdaper = (GameGridviewAdapter) head_gridview.getAdapter();
            if (currentAdaper == null) {
                return;
            }
            View listItem = currentAdaper.getView(0, null, head_gridview);
            listItem.measure(0, 0);
            int height = listItem.getMeasuredHeight();

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) head_gridview.getLayoutParams();
            layoutParams.height = height +10;
            head_gridview.setLayoutParams(layoutParams);

            game_recent_root_ll.setVisibility(View.VISIBLE);
        }

//        if (playedGameList.size() <= 0) {
//            game_recent_root_ll.setVisibility(View.GONE);
//        } else {
//            game_recent_root_ll.setVisibility(View.VISIBLE);
//        }
        headAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    /**
     * 当切换页面
     * 会执行这里
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        long currentModify = System.currentTimeMillis();
        if (!hidden && currentModify - refreshModify > REFRESH_TIME) {
            page = 0;
            if (allMembers != null) {
                allMembers.clear();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (REFRESH_RECENT_LIST) {
            REFRESH_RECENT_LIST = false;
            refreshPlayedGameList();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(activity);
    }

}
