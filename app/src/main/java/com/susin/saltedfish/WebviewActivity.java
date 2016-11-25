package com.susin.saltedfish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 说明：
 * 
 * @作者 Susin
 * @创建时间 2016/11/23 21:46  
 * @版本 
 * @------修改记录-------
 * @修改人 
 * @版本 
 * @修改内容 
 */

public class WebviewActivity extends Activity {

    @InjectView(R.id.wv_webview)
    WebView webview;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        String url = getIntent().getExtras().getString("url");
        webview.loadUrl(url);
        // 防止用浏览器跳转
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                return true;
            }
        });


    }

}