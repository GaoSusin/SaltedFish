package com.susin.saltedfish.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.susin.saltedfish.BuildConfig;
import com.susin.saltedfish.base.JDApplication;
import com.susin.saltedfish.utils.logger.Logger;

public class RequestManager {

    public static final int OUT_TIME = 10000;
    public static final int TIMES_OF_RETRY = 1;

    public static RequestQueue mRequestQueue = Volley.newRequestQueue(JDApplication.getContext());

    private RequestManager() {
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        //给每个请求重设超时、重试次数
        request.setRetryPolicy(new DefaultRetryPolicy(
                OUT_TIME,
                TIMES_OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);

        if (BuildConfig.DEBUG) {
            Logger.d(request.getUrl());
        }

    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}