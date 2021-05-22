package com.yuukidach.ucount;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.yuukidach.ucount.model.IOItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuukidach on 17-3-12.
 */

public class CostFragment extends Fragment {

    private String[] titles = {"一般", "用餐", "零食", "交通", "充值", "购物", "娱乐", "住房", "饮料", "网购",
            "鞋帽", "护肤", "化妆", "电影", "转账", "浪费", "健身", "医疗", "旅游", "教育", "香烟", "酒水", "数码", "捐献",
            "家庭", "宠物", "服装", "日用", "水果", "母婴", "信用卡", "理财", "工作", "家具", "通信"};
    /*
    *  ViewPager 作用:  实现了左右切换当前的视图 view, 实现滑动切换的效果
    *
     */
    private ViewPager mPager;

    private List<View> mPagerList;
    private List<IOItem> mDatas;
    //布局服务
    private LayoutInflater inflater;
    private ImageView itemImage;
    private TextView itemTitle;
    //相对布局服务
    private RelativeLayout itemLayout;
    //Extensible:可扩展的
    // Indicator:标志,指针,迹象
    //可扩展页面
    private ExtensiblePageIndicator extensiblePageIndicator;

    // 总的页数
    private int pageCount;

    // 每一页显示的个数
    private int pageSize = 18;

    // 当前显示的是第几页
    private int curIndex = 0;

    private static final String TAG = "CostFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");

        // 获得AddItemActivity对应的控件，用来提示已选择的项目类型
        getBannerId();

        View view = inflater.inflate(R.layout.cost_fragment, container, false);

        mPager = (ViewPager) view.findViewById(R.id.viewpager_1);
        extensiblePageIndicator = (ExtensiblePageIndicator) view.findViewById(R.id.ll_dot_1);


        int height = mPager.getHeight();
        int width = mPager.getWidth();

        // 初始化数据源
        initDatas();

        // 初始化上方banner
        changeBanner(mDatas.get(0));

        // 总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.item_recycler_grid, mPager ,false);
            MyGridLayoutManager layoutManager = new MyGridLayoutManager(getContext(), 6);
            recyclerView.setLayoutManager(layoutManager);
            GridRecyclerAdapter adaper = new GridRecyclerAdapter(mDatas, i, pageSize);
            recyclerView.setAdapter(adaper);

            mPagerList.add(recyclerView);

            adaper.setOnItemClickListener(new GridRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    changeBanner(mDatas.get(position));
                }
            });
        }
        // 设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        extensiblePageIndicator.initViewPager(mPager);

        return view;
    }

    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<IOItem>();
        for (int i = 1; i <= titles.length; i++) {
            mDatas.add(new IOItem("type_big_" + i, titles[i-1]));
        }
    }


    // 获得AddItemActivity对应的控件，用来提示已选择的项目类型
    public void getBannerId() {
        //对应第二个页面的第二个横幅   item text            price
        itemImage = (ImageView) getActivity().findViewById(R.id.chosen_image);
        itemTitle = (TextView) getActivity().findViewById(R.id.chosen_title);
        itemLayout = (RelativeLayout) getActivity().findViewById(R.id.have_chosen);
    }

    // 改变banner状态
    public void changeBanner(IOItem tmpItem) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), tmpItem.getSrcId());
        //Palette调色板 颜料
        Palette.Builder pb = new Palette.Builder(bm);
        //设置最大的颜色数量
        pb.maximumColorCount(1);


        itemImage.setImageResource(tmpItem.getSrcId());
        itemTitle.setText(tmpItem.getName());
        itemImage.setTag(-1);                        // 保留图片资源属性，-1表示支出
        itemTitle.setTag(tmpItem.getSrcName());      // 保留图片资源名称作为标签，方便以后调用
        // 获取图片颜色并改变上方banner的背景色
        //异步监听任务   分析的图片太大或颜色比较复杂的话,防止主线程卡死
        pb.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //Swatch样本对象  内部类   提供了一些获取最终颜色的方法
                Palette.Swatch swatch = palette.getSwatches().get(0);
                if (swatch != null) {
                    //得到颜色的grb值
                    itemLayout.setBackgroundColor(swatch.getRgb());
                } else {
                    Log.d(TAG, "changeBanner: ");
                }
            }
        });
    }
}