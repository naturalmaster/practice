package com.example.coney.calc_formula;

import android.app.Application;
import android.content.Context;

/**
 * Created by coney on 2018/11/12.
 */

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getmContext() {
        return mContext;
    }

}
