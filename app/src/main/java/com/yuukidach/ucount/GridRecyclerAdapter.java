package com.yuukidach.ucount;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuukidach.ucount.model.IOItem;

import java.util.List;

/**
 * 网格布局适配器
 */

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "GridRecyclerAdapter";

    private List<IOItem> mDatas;
    //item下标
    private int curIndex;
    //页面的最大Item数目
    private int pageSize;
    //接口
    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;
//内部类
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;

        public ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.item_grid_icon);
            itemTitle = (TextView) view.findViewById(R.id.item_grid_title);
        }
    }
//构造方法
    public GridRecyclerAdapter(List<IOItem> Datas, int curIndex, int pageSize) {
        this.mDatas = Datas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        //动态加载布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_io_item, parent, false);
        // 重新设计子项高度
        int height = parent.getHeight();
        view.getLayoutParams().height = height / 4 + 20;

        // 将创建的View注册点击事件
        view.setOnClickListener(this);
        return(new ViewHolder(view));
    }

//返回item真实位置
    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }
//返回item的数目
    @Override
    public int getItemCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: I am in here:" + position);
        //item的真实位置
        int realPositon = position + curIndex * pageSize;
        IOItem ioItem = mDatas.get(realPositon);
        //为item设置图片和text内容
        holder.itemImage.setImageResource(ioItem.getSrcId());
        holder.itemTitle.setText(ioItem.getName());
        // 将数据保存在itemView的Tag中，以便点击时进行获取   复用  一个View对象
        holder.itemView.setTag(realPositon);
    }
 //调用接口里的方法  将来会被重写
    public void onClick(View view) {
        if (mOnItemClickListener != null ) {
            mOnItemClickListener.onItemClick(view, (int)view.getTag());
        }
    }
   //设置监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
