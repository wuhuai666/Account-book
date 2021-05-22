package com.yuukidach.ucount.draw;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.R;
import com.yuukidach.ucount.model.IOItem;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ExpenseActivity extends AppCompatActivity {

    //随机生成颜色RGB值
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }

 //绘图 myDataView
    private MyDataView myDataView;
    private HashMap<String, Float> dataDegee = new HashMap<>();
    private HashMap<String, String> dataColor = new HashMap<>();
    private LinearLayout mLny;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        uodatePieChart();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uodatePieChart(){
        mLny = findViewById(R.id.expense);
        //获取总金额
        float total = 0;
                             //查询出第一本账本中的type=-1(支出)的money数  计算总金额
        List<IOItem> albumList = DataSupport.where("bookid = ? and type = ?",  String.valueOf(GlobalVariables.getmBookId()), "-1").find(IOItem.class);

        for (IOItem ioItem : albumList) {
            System.out.println(ioItem.getMoney());
            total += ioItem.getMoney();
        }

        if(total>0){
           //初始化哈希Map    置空
            dataColor.clear();
            dataDegee.clear();

            for (IOItem ioItem : albumList) {
                System.out.println(ioItem.getMoney());
                //如果该花费条目已经存在
                //getDegree按比例转换为所占圆形的角度
                if(dataDegee.get(ioItem.getName()) != null){
                    //把支出消费的  消费名:价格(价格+原来累计的价格)  放入 HashMap里存储
                    dataDegee.put(ioItem.getName(), getDegree((float) ioItem.getMoney(), total) + dataDegee.get(ioItem.getName()));
                }else{
                    //   消费项目:价格
                    dataDegee.put(ioItem.getName(), getDegree((float) ioItem.getMoney(), total));
                    dataColor.put(ioItem.getName(), getRandColor());
                }
            }

            System.out.println(dataDegee);
            if(myDataView != null){
                mLny.removeView(myDataView);
            }
            myDataView = new MyDataView(ExpenseActivity.this, dataDegee, dataColor);
            mLny.addView(myDataView);
            Log.i("test draw", "remove view ; add view");
            Toast.makeText(this,"哇，你居然在"+getMaxinumType()+"上花了这么多", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this,"账本无数据，请先录入数据！", Toast.LENGTH_SHORT).show();
        }
    }

    //按比例转换为所占圆形的角度
    private float getDegree(float number, float total){
        return  number/total * 360;
    }

    //获得花费最多的那个项目
    private String getMaxinumType(){
        String type="";
        float money = Float.MIN_VALUE;
        for(String key : dataDegee.keySet()){
            if(dataDegee.get(key) > money){
                type = key;
                money = dataDegee.get(key);
            }
        }
        return type;
    }
}
