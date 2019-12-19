package com.example.xmlystudio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xmlystudio.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    private List<Album> mData = new ArrayList<>();
    private OnRecommendItemClickLinstener mOnRecommendItemClickLinstener;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //载入View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //这里是设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRecommendItemClickLinstener != null) {
                    mOnRecommendItemClickLinstener.onItemClick((Integer) v.getTag());
                }
            }
        });
        holder.setData(mData.get(position));
    }


    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);

        }
        //更新UI
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder  {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到控件设置数据
            //专辑头像，封面
            ImageView album_cover = itemView.findViewById(R.id.album_cover);
            //Title
            TextView album_title_tv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView album_description_tv = itemView.findViewById(R.id.album_description_tv);
            //播放次数
            TextView album_play_count = itemView.findViewById(R.id.album_play_count);
            //数量
            TextView album_content_size = itemView.findViewById(R.id.album_content_size);

            //设置title
            album_title_tv.setText(album.getAlbumTitle());
            //设置描述
            album_description_tv.setText(album.getAlbumIntro());
            //设置播放次数
            album_play_count.setText(album.getPlayCount() / 10000 + "万");
            // 设置专辑数量
            album_content_size.setText(album.getIncludeTrackCount() + "");

            //设置图片
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(album_cover);
        }


    }


    public void setOnRecommendItemClickLinstener(OnRecommendItemClickLinstener linstener){
        this.mOnRecommendItemClickLinstener = linstener;

    }

    public interface OnRecommendItemClickLinstener {
        void onItemClick(int position);
    }
}
