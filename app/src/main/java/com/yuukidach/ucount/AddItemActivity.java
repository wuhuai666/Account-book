package com.yuukidach.ucount;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.IOItem;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = "AddItemActivity";
//碎片
    private FragmentManager manager;
    private FragmentTransaction transaction;
 /*
      支出
  */
    private Button addCostBtn;
    /*
      收入
  */
    private Button addEarnBtn;
    /*
    清零
     */
    private Button clearBtn;
    /*
       完成
     */
    private ImageButton addFinishBtn;
    /*
    点击图片记事
     */
    private ImageButton addDescription;

//横幅的图片  和  横幅的文本  以及金额
    private ImageView bannerImage;
    private TextView bannerText;
    private TextView moneyText;

    private TextView words;
//格式化日期
    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private SimpleDateFormat formatSum  = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addCostBtn = (Button) findViewById(R.id.add_cost_button);
        addEarnBtn = (Button) findViewById(R.id.add_earn_button);
        addFinishBtn   = (ImageButton) findViewById(R.id.add_finish);
        addDescription = (ImageButton) findViewById(R.id.add_description);
        clearBtn = (Button) findViewById(R.id.clear);
        words = (TextView) findViewById(R.id.anime_words);
        // 设置字体颜色
        //根据路径得到Typeface
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/chinese_character.ttf");
       //给按钮文本设置该字体
        clearBtn.setTypeface(typeface);
        words.setTypeface(typeface);
        // 设置按钮监听
        addCostBtn.setOnClickListener(new ButtonListener());
        addEarnBtn.setOnClickListener(new ButtonListener());
        addFinishBtn.setOnClickListener(new ButtonListener());
        addDescription.setOnClickListener(new ButtonListener());
        clearBtn.setOnClickListener(new ButtonListener());


        bannerText = (TextView) findViewById(R.id.chosen_title);
        bannerImage = (ImageView) findViewById(R.id.chosen_image);

        moneyText = (TextView) findViewById(R.id.input_money_text);
        // 及时清零  初始化
        moneyText.setText("0.00");

        manager = getSupportFragmentManager();
       //碎片的方式
        //初始化显示  为支出页面
        transaction = manager.beginTransaction();
        transaction.replace(R.id.item_fragment, new CostFragment());
        transaction.commit();

    }



    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            transaction = manager.beginTransaction();

            switch (view.getId()) {
                //支出按钮
                case R.id.add_cost_button:
                    addCostBtn.setTextColor(0xffff8c00); // 设置“支出“按钮为灰色
                    addEarnBtn.setTextColor(0xff908070); // 设置“收入”按钮为橙色
                    //切换视图(支出列表碎片)
                    transaction.replace(R.id.item_fragment, new CostFragment());
                    Log.d(TAG, "onClick: add_cost_button");

                    break;
                //收入按钮
                case R.id.add_earn_button:
                    addEarnBtn.setTextColor(0xffff8c00); // 设置“收入“按钮为灰色
                    addCostBtn.setTextColor(0xff908070); // 设置“支出”按钮为橙色
                    transaction.replace(R.id.item_fragment, new EarnFragment());
                    Log.d(TAG, "onClick: add_earn_button");

                    break;
                 //右下角添加完成按钮
                case R.id.add_finish:
                    String moneyString =  moneyText.getText().toString();
                    if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals(""))
                        Toast.makeText(getApplicationContext(),"emmm，你还没输入金额",Toast.LENGTH_SHORT).show();
                    else {
                        putItemInData(Double.parseDouble(moneyText.getText().toString()));
                        calculatorClear();
                        finish();
                    }
                    break;
                case R.id.clear:
                    calculatorClear();
                    moneyText.setText("0.00");
                    break;
                case R.id.add_description:
                    Intent intent = new Intent(AddItemActivity.this, AddDescription.class);
                    startActivity(intent);
            }

            transaction.commit();
        }
    }
  //把添加好的item放入相应的数据中
    public void putItemInData(double money) {
        IOItem ioItem = new IOItem();
        //默认第一本账本
        BookItem bookItem = DataSupport.find(BookItem.class, GlobalVariables.getmBookId());
        //得到横幅的文本内容
        String tagName = (String) bannerText.getTag();
        //得到横幅图片的收入支出类型  -1 或者 1
        int tagType = (int) bannerImage.getTag();
           //支出
        if (tagType < 0) {
            ioItem.setType(ioItem.TYPE_COST);
        } else ioItem.setType(ioItem.TYPE_EARN);
        //支出收入
        ioItem.setName(bannerText.getText().toString());
        //item名字
        ioItem.setSrcName(tagName);
        //设置的金额
        ioItem.setMoney(money);
        // 存储记账时间
        ioItem.setTimeStamp(formatItem.format(new Date()));
        //设置描述
        ioItem.setDescription(GlobalVariables.getmDescription());
        //设置账本   默认第一本账本  1
        ioItem.setBookId(GlobalVariables.getmBookId());
        //保存
        ioItem.save();

        // 将收支存储在对应账本下
        bookItem.getIoItemList().add(ioItem);
        //设置 余额总额
        bookItem.setSumAll(bookItem.getSumAll() + money*ioItem.getType());
        bookItem.save();

        calculateMonthlyMoney(bookItem, ioItem.getType(), ioItem);

        // 存储完之后及时清空备注
        GlobalVariables.setmDescription("");
    }

    // 计算月收支
    public void calculateMonthlyMoney(BookItem bookItem, int money_type, IOItem ioItem) {
        String sumDate = formatSum.format(new Date());

        // 求取月收支类型      money_type 收支类型
        //如果item上面有时间戳   就把money+MonthlyMoney   次数>1
        if (bookItem.getDate().equals(ioItem.getTimeStamp().substring(0, 8))) {
            if (money_type == 1) {
                bookItem.setSumMonthlyEarn(bookItem.getSumMonthlyEarn() + ioItem.getMoney());
            } else {
                bookItem.setSumMonthlyCost(bookItem.getSumMonthlyCost() + ioItem.getMoney());
            }
        } else {
            //如果item上面没有时间戳   直接添加      次数==1
            if (money_type == 1) {
                //设置当月的收入   并初始化当月的支出
                bookItem.setSumMonthlyEarn(ioItem.getMoney());
                bookItem.setSumMonthlyCost(0.0);
            } else {
                //设置当月的支出  并初始化当月的收入
                bookItem.setSumMonthlyCost(ioItem.getMoney());
                bookItem.setSumMonthlyEarn(0.0);
            }
            bookItem.setDate(sumDate);
        }
         //保存到数据库
        bookItem.save();
    }

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        //向下转型
        Button view = (Button) v;
        String digit = view.getText().toString();
        String money = GlobalVariables.getmInputMoney();
        //存在小数点 &位数恒大于2    0.00
        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length()>2) {
            String dot = money.substring(money.length() - 3, money.length() - 2);
            Log.d(TAG, "calculatorNumOnclick: " + dot);
                //小数点后限制为两位
            if (dot.equals(".")) {
                Toast.makeText(getApplicationContext(), "已经不能继续输入了", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //存储到全局变量
        GlobalVariables.setmInputMoney(money+digit);
        //强转 double类型
        moneyText.setText(decimalFormat.format(Double.valueOf(GlobalVariables.getmInputMoney())));
    }

    // 清零按钮         清零
    public void calculatorClear() {
        GlobalVariables.setmInputMoney("");
        GlobalVariables.setHasDot(false);
    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        //不可重复输入小数点      <=1
        if (GlobalVariables.getmHasDot()) {
            Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
        } else {
            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney()+".");
            GlobalVariables.setHasDot(true);
        }
    }

}
