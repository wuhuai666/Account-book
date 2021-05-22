package com.yuukidach.ucount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.markushi.ui.CircleButton;

//添加备注页面
//
public class AddDescription extends AppCompatActivity {
    private EditText inputTxt;
    private TextView countTxt;
    //显示日期
    private TextView dateTxt;
    //保存
    private CircleButton doneBtn;

    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_descrpition);

        inputTxt = (EditText) findViewById(R.id.page3_edit);
        countTxt = (TextView) findViewById(R.id.page3_count);
        dateTxt = (TextView) findViewById(R.id.page3_date);
        doneBtn = (CircleButton) findViewById(R.id.page3_done);

        // 显示日期
        dateTxt.setText(formatItem.format(new Date()));

        // 获取焦点, 不是将光标移动到文本的开始或者结尾, 用来获取输入框中的所有内容
        inputTxt.setFocusable(true);

        inputTxt.setText(GlobalVariables.getmDescription());
        //设置光标的位置的默认位置为   输入内容的长度 即为 尾部
        inputTxt.setSelection(inputTxt.getText().length());
         //初始化显示的字数为 0/30
        countTxt.setText(String.valueOf(inputTxt.getText().length()) +"/30");

        // 设置输入文本监听，实时显示字数
        inputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTxt.setText(String.valueOf(s.length())+"/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将备注内容保存在全局变量中保存
                GlobalVariables.setmDescription(inputTxt.getText().toString());
                finish();
            }
        });
    }
}
