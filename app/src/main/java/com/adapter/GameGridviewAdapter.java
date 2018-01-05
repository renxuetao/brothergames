package com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.GameBean;
import com.brother.games.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class GameGridviewAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<GameBean> mInfos;
    private DisplayImageOptions options;
    private LayoutInflater layoutInflator;
    private int mScreenWidth;

    public GameGridviewAdapter(Context context, ArrayList<GameBean> infos, int mScreenWidth) {
        mContext = context;
        mInfos = infos;
        this.mScreenWidth = mScreenWidth;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon72)
                .showImageForEmptyUri(R.drawable.icon72)//下载失败
                .showImageOnFail(R.drawable.icon72)//解码失败
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        layoutInflator = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameGridviewAdapter.ViewHolder holder = null;
        final GameBean item = mInfos.get(position);
        if (convertView == null) {
            holder = new GameGridviewAdapter.ViewHolder();
            convertView = layoutInflator.inflate(R.layout.game_home_gridview_item, null,false);
            holder.root_ll = (LinearLayout) convertView.findViewById(R.id.root_ll);
            holder.play_time = (TextView) convertView.findViewById(R.id.play_time);
            holder.game_name = (TextView) convertView.findViewById(R.id.game_name);
            holder.car_picture_iv = (ImageView) convertView.findViewById(R.id.car_picture_iv);
            holder.new_tips = (TextView) convertView.findViewById(R.id.new_tips);
            holder.icon_rl = (RelativeLayout) convertView.findViewById(R.id.icon_rl);
            holder.play_game_describe=(TextView) convertView.findViewById(R.id.play_game_describe);
            convertView.setTag(holder);
        } else {
            holder = (GameGridviewAdapter.ViewHolder) convertView.getTag();
        }
        holder.game_name.setText(item.GameName);
        holder.play_time.setText(item.Playing + " played");
//        ImageLoader.getInstance().displayImage(item.GameCover, holder.car_picture_iv, options);
        if(item.IsNew == 0){
            holder.new_tips.setVisibility(View.GONE);
        }else{
            holder.new_tips.setVisibility(View.VISIBLE);
        }

//        int w = DensityUtil.dip2px(mContext, 25);
//        int h = DensityUtil.dip2px(mContext, 20);
//        int ww = (mScreenWidth - (w * 4))/3;
//        LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(ww, ViewGroup.LayoutParams.WRAP_CONTENT);
//        holder.root_ll.setLayoutParams(rootLayoutParams);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ww, ww);
//        if(position > 2){
//            layoutParams.setMargins(0,h,0,0);
//        }
//        holder.icon_rl.setLayoutParams(layoutParams);
        holder.play_game_describe.setText(item.Introduction);
        return convertView;
    }

    class ViewHolder {
        LinearLayout root_ll;
        RelativeLayout icon_rl;
        TextView play_time;
        TextView game_name;
        ImageView car_picture_iv;
        TextView new_tips;
        TextView play_game_describe;
    }
}
