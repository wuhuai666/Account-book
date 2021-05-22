package com.yuukidach.ucount;

/**
 * Created by yuukidach on 17-3-21.
 */

//存放一些全局使用的变量

public class GlobalVariables {
    //日期
    private static String mDate = "";
    //小数点
    private static boolean mHasDot = false;
    //money
    private static String mInputMoney = "";
    //描述
    private static String mDescription = "";
    //账本id
    private static int mBookId = 1;                         // 初始状态选择第一本账本
    private static int mBookPos = 0;

    public static void setmDate(String date)      { mDate = date;     }
    public static void setHasDot(boolean hasDot)  { mHasDot = hasDot; }
    public static void setmInputMoney(String a)   { mInputMoney = a;  }
    public static void setmDescription(String a ) { mDescription = a; }
    public static void setmBookId(int id)         { mBookId = id;     }
    public static void setmBookPos(int pos)       { mBookPos = pos;   }

    public static String getmDate()        { return mDate;        }
    public static boolean getmHasDot()     { return mHasDot;      }
    public static String getmInputMoney()  { return mInputMoney;  }
    public static String getmDescription() { return mDescription; }
    public static int getmBookId()         { return mBookId;      }
    public static int getmBookPos()        { return mBookPos;     }
}
