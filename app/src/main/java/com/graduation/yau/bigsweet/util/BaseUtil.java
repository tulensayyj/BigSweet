package com.graduation.yau.bigsweet.util;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by yyj on 2019/6/19.
 */

public class BaseUtil {
    public static Context getApplicationContext() {
        return Bmob.getApplicationContext();
    }
}
