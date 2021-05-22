package com.yuukidach.ucount;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 网格布局管理                        第二个页面的第三栏 左右滑菜单
 */

public class MyGridLayoutManager extends GridLayoutManager {
    private static final String TAG = "MyGridLayoutManager";

    //AttributeSet xml文件中元素属性的一个集合   ,从编译好的xml文件中获取属性值
    public MyGridLayoutManager(Context context, AttributeSet attrs,
                                 int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount){
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount,
                                 int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
//使Recyclerview的高度适应内容的高度
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        setMeasuredDimension(widthSpec, heightSpec);
        Log.d(TAG, "onMeasure: ");
    }
}
