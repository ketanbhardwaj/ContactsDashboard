package com.contactsdashboard_ketan.base;

import android.app.Application;

import com.contactsdashboard_ketan.helper.BLog;

/**
 * Created by Ketan on 3/17/17.
 */

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BLog.setShouldPrint(false);
    }
}
