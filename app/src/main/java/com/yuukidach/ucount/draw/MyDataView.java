package com.yuukidach.ucount.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MyDataView extends View {

    private Paint mPaint = new Paint();
    private Canvas mCanvas;

    private int mWidth,mHeight;
    private int mPx;

    private HashMap<String, Float> dataDegree;
    private HashMap<String, String> dataColor;

    /**
     * constructor
     * @param context
     * @param dataDegree
     * @param dataColor
     */
    public MyDataView(Context context,
                      HashMap<String, Float> dataDegree,
                      HashMap<String, String> dataColor) {
        super(context);
        this.dataDegree = dataDegree;
        this.dataColor = dataColor;
    }

    /**
     * constructor
     * @param context
     * @param dataDegree
     * @param dataColor
     * @param attrs
     */
    public MyDataView(Context context,
                      HashMap<String, Float> dataDegree,
                      HashMap<String, String> dataColor,
                      @Nullable AttributeSet attrs
                      ) {
        super(context, attrs);
        this.dataDegree = dataDegree;
        this.dataColor = dataColor;
    }

    /**
     * constructor
     * @param context
     * @param dataDegree
     * @param dataColor
     * @param attrs
     * @param defStyleAttr
     */
    public MyDataView(Context context,
                      HashMap<String, Float> dataDegree,
                      HashMap<String, String> dataColor,
                      @Nullable AttributeSet attrs,
                      int defStyleAttr
                      ) {
        super(context, attrs, defStyleAttr);
        this.dataDegree = dataDegree;
        this.dataColor = dataColor;
    }

    /**
     * constructor
     * @param context
     * @param dataDegree
     * @param dataColor
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public MyDataView(Context context, HashMap<String, Float> dataDegree,
                      HashMap<String, String> dataColor,
                      @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes
                      ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.dataDegree = dataDegree;
        this.dataColor = dataColor;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        mCanvas = canvas;
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        mPx = Math.max(mWidth/100, 10);

        //绘制 饼图
        darwPieChart();

        //绘制图例
        drawPieChartAnno();
    }

    /**
     * 绘制图例
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void drawPieChartAnno() {

        int left = mPx;
        int right = mPx * 7;
        int top = mPx;
        int rectHeight = mPx * 6;
        int bottom = top+rectHeight;
        int blank = mPx;
              //画左上角小矩形
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mPx * 4);
        for(String key : dataDegree.keySet()){
            mPaint.setColor(Color.parseColor(dataColor.get(key)));
            mCanvas.drawRect(left,top,right,bottom,mPaint);
            mPaint.setColor(Color.DKGRAY);
            mCanvas.drawText(key+":"+ new DecimalFormat("#.00").format((dataDegree.get(key))/360*100)+"%",right+blank, bottom ,mPaint);
            //更新边界位置
            top = bottom + blank;
            bottom  = top + rectHeight;
        }
    }

    /**
     * 绘制统计图
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void darwPieChart() {
//确定圆形百分比图的屏幕坐标
        int centerX = mWidth/2;
        int centerY = mHeight/2;
        int radius = mPx * 30;
        int left = centerX - radius;
        int right = centerX + radius;
        int top = centerY - radius ;
        int bottom  = centerY + radius;

        //根据颜色和度数绘制饼状图
        float startAngle = 0f;
        mPaint.setStyle(Paint.Style.FILL);
/**                    绘制圆弧
 * 【基本语法】public void drawArc (RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
 *
 * 参数说明
 *
 * oval：圆弧所在的椭圆对象。
 *
 * startAngle：圆弧的起始角度。
 *
 * sweepAngle：圆弧的角度。
 *
 * useCenter：是否显示半径连线，true表示显示圆弧与圆心的半径连线，false表示不显示。
 *
 * paint：绘制时所使用的画笔。
 *
 *
 */

        for(String key : dataDegree.keySet()){
            //画扇形图的区域
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(dataColor.get(key)));
            mCanvas.drawArc(left,top,right,bottom, startAngle,dataDegree.get(key),true,mPaint);
            //画扇形图区域的白线
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.WHITE);
            mCanvas.drawArc(left,top,right,bottom, startAngle,dataDegree.get(key),true,mPaint);
            //每次画完一个 更新开始角度
            startAngle += dataDegree.get(key);
        }
    }



}
