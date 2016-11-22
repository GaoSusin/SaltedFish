package com.susin.saltedfish;/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Intent;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.susin.saltedfish.base.BaseActivity;
import com.susin.saltedfish.presenter.Presenter;
import com.susin.saltedfish.presenter.impl.SplashPresenterImpl;
import com.susin.saltedfish.view.SplashView;

import butterknife.InjectView;

/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    2015/3/9.
 * Description:
 */
public class SplashActivity extends BaseActivity implements SplashView {

    @InjectView(R.id.splash_image)
    ImageView mSplashImage;

    @InjectView(R.id.splash_version_name)
    TextView mVersionName;

    @InjectView(R.id.splash_copyright)
    TextView mCopyright;

    private Presenter mSplashPresenter = null;

    @Override
    protected void initView() {
        initViewsAndEvents();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    protected void initViewsAndEvents() {
        mSplashPresenter = new SplashPresenterImpl(this, this);
        mSplashPresenter.initialized();
    }

    @Override
    public void animateBackgroundImage(Animation animation) {
        mSplashImage.startAnimation(animation);
    }

    @Override
    public void initializeViews(String versionName, String copyright, int backgroundResId) {
        mCopyright.setText(copyright);
        mVersionName.setText(versionName);
        mSplashImage.setImageResource(backgroundResId);
    }

    @Override
    public void initializeUmengConfig() {
    }

    @Override
    public void navigateToHomePage(int size) {
        Intent intent = new Intent(this, FragmentTestActivity.class);
        intent.putExtra("picSize", size);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
