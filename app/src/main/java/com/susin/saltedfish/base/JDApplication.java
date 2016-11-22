package com.susin.saltedfish.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.facebook.stetho.Stetho;
import com.susin.saltedfish.R;
import com.susin.saltedfish.utils.StrictModeUtil;
import com.susin.saltedfish.view.ImageLoadProxy;


public class JDApplication extends Application {

    public static int COLOR_OF_DIALOG = R.color.primary;
    public static int COLOR_OF_DIALOG_CONTENT = Color.WHITE;

    private static Context mContext;
    @Override
    public void onCreate() {
        StrictModeUtil.init();
        super.onCreate();
        mContext = this;
        ImageLoadProxy.initImageLoader(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    public static Context getContext() {
        return mContext;
    }



}