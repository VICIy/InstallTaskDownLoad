package com.viciy.installtaskdownload.app;

import android.app.Application;

import com.viciy.installtaskdownload.BuildConfig;

import org.xutils.x;

/**
 * Created by bai-qiang.yang on 2017/2/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能

        // 信任所有https域名
        /*HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });*/
    }
}
