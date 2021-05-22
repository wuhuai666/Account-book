package com.yuukidach.ucount;

import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 *
 *    1.这里是没有写完的功能:
 *            extends PagerAdapter
 *            可实现:
 *
 *    2.要想做到viewpage滑动的效果， 还需要再加上三个方法destroyItem,instantiateItem,getPageTitle
 *    //viewpage的适配器
 *    class MyViewPageAdapter extends PagerAdapter{
 *        @Override
 *        public int getCount() {
 *            return mainTitlesArray.length;
 *        }
 *
 *        //判断是否是否为同一张图片，这里返回方法中的两个参数做比较就可以
 *        @Override
 *        public boolean isViewFromObject(View view, Object object) {
 *            return view==object;
 *        }
 * //设置viewpage内部东西的方法，如果viewpage内没有子空间滑动产生不了动画效果
 *        @Override
 *        public Object instantiateItem(ViewGroup container, int position) {
 *            TextView textView = new TextView(MainActivity.this);
 *            textView.setText(mainTitlesArray[position]);
 *            textView.setGravity(Gravity.CENTER);
 *            container.addView(textView);
 *            //最后要返回的是控件本身
 *            return textView;
 *        }
 * //因为它默认是看三张图片，第四张图片的时候就会报错，还有就是不要返回父类的作用
 *        @Override
 *        public void destroyItem(ViewGroup container, int position, Object object) {
 *                 container.removeView((View) object);
 *   //         super.destroyItem(container, position, object);
 *        }
 * //目的是展示title上的文字，
 *        @Override
 *        public CharSequence getPageTitle(int position) {
 *            return mainTitlesArray[position];
 *        }
 *    }
 *PagerAdapter主要是viewpager的适配器，
 * 而viewPager则也是在android.support.v4扩展包中新添加的一个强大的控件，
 * 可以实现控件的滑动效果
 *
 *
 */
public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    
    private List<View> mViewList;

    public ViewPagerAdapter(List<View> mViewList) {
        Log.d(TAG, "ViewPagerAdapter: ");
        this.mViewList = mViewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem: ");
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: ");
        container.addView(mViewList.get(position));
        return (mViewList.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    //主要方法 :返回viewpager页面的个数
    @Override
    public int getCount() {
        Log.d(TAG, "getCount: ");
        if (mViewList == null)
            return 0;
        return mViewList.size();
    }
//主要方法 :判断是否为同一张图片
    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.d(TAG, "isViewFromObject: ");
        return view == object;
    }
}