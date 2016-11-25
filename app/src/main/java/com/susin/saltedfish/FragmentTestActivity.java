package com.susin.saltedfish;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.susin.saltedfish.fragment.HistoryFragment;
import com.susin.saltedfish.fragment.JokeFragment;
import com.susin.saltedfish.fragment.NewsFragment;
import com.susin.saltedfish.fragment.PictureFragment;
import com.susin.saltedfish.fragment.WeChatFragment;
import com.susin.saltedfish.model.History;
import com.susin.saltedfish.widget.NavigationTabBar;

import java.util.ArrayList;

/**
 * 说明：
 *
 * @作者 Susin
 * @创建时间 2016/8/16 20:02
 * @版本
 * @------修改记录-------
 * @修改人
 * @版本
 * @修改内容
 */
public class FragmentTestActivity extends FragmentActivity {

    private PictureFragment boringPicFragment;
    private JokeFragment jokeFragment;
    private WeChatFragment weChatFragment;
    private NewsFragment newsFragment;
    private HistoryFragment historyFragment;
    private MyFragmentPagerAdapter adapter;
    private ArrayList<Fragment> fragmentsList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题栏
        setContentView(R.layout.activity_fragment_test);
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        int picSize = intent.getIntExtra("picSize", 0);

        fragmentsList = new ArrayList<Fragment>();

        boringPicFragment = new PictureFragment();
        fragmentsList.add(boringPicFragment);

        jokeFragment = new JokeFragment();
        fragmentsList.add(jokeFragment);

        weChatFragment = new WeChatFragment();
        fragmentsList.add(weChatFragment);

        newsFragment = new NewsFragment();
        fragmentsList.add(newsFragment);

        historyFragment = new HistoryFragment();
        fragmentsList.add(historyFragment);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                fragmentsList);
        viewPager.setAdapter(adapter);

        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_mood_white_24dp),
                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("无聊图")
                        .badgeTitle(picSize + "")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_chat_white_24dp),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("段子")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Diploma")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Flag")
                        .badgeTitle("icon")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Medal")
                        .badgeTitle("777")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;

        public MyFragmentPagerAdapter(FragmentManager fm,
                                      ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}
