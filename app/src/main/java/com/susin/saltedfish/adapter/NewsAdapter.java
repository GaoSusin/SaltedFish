package com.susin.saltedfish.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.susin.saltedfish.R;
import com.susin.saltedfish.WebviewActivity;
import com.susin.saltedfish.base.ConstantString;
import com.susin.saltedfish.base.JDApplication;
import com.susin.saltedfish.callback.LoadFinishCallBack;
import com.susin.saltedfish.callback.LoadResultCallBack;
import com.susin.saltedfish.model.News;
import com.susin.saltedfish.net.Request4News;
import com.susin.saltedfish.net.RequestManager;
import com.susin.saltedfish.utils.NetWorkUtil;
import com.susin.saltedfish.utils.ShareUtil;
import com.susin.saltedfish.utils.ShowToast;
import com.susin.saltedfish.utils.UtilText;
import com.susin.saltedfish.view.ImageLoadProxy;
import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private int page;
    private int lastPosition = -1;
    private ArrayList<News> mNews;
    private Activity mActivity;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinisCallBack;

    public NewsAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        mNews = new ArrayList<>();
    }

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(NewsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {

        final News news = mNews.get(position);

        holder.tvTitle.setText(news.getTitle());
        holder.tvRealtype.setText(news.getRealtype());
        holder.tvAuthor.setText(news.getAuthor_name());
        holder.tvTime.setText(news.getDate());
        ImageLoadProxy.displayImageList(news.getThumbnail_pic_s(),holder.ivPic1, R.drawable.ic_loading_large, null, null);
        ImageLoadProxy.displayImageList(news.getThumbnail_pic_s02(),holder.ivPic2, R.drawable.ic_loading_large, null, null);
        ImageLoadProxy.displayImageList(news.getThumbnail_pic_s03(),holder.ivPic3, R.drawable.ic_loading_large, null, null);
        holder.llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url",news.getUrl());
                Intent intent = new Intent(mActivity, WebviewActivity.class);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mActivity)
                        .items(R.array.joke_dialog)
                        .backgroundColor(mActivity.getResources().getColor(JDApplication.COLOR_OF_DIALOG))
                        .contentColor(JDApplication.COLOR_OF_DIALOG_CONTENT)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                switch (which) {
                                    //分享
                                    case 0:
                                        ShareUtil.shareText(mActivity, news.getTitle().trim());
                                        break;
                                    //复制
                                    case 1:
                                        UtilText.copy(mActivity, news.getTitle());
                                        break;
                                }

                            }
                        }).show();
            }
        });

        setAnimation(holder.card, position);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void loadFirst() {
        page = 1;
        loadDataByNetworkType();
    }

    public void loadNextPage() {
        page++;
        loadDataByNetworkType();
    }

    private void loadDataByNetworkType() {

        if (NetWorkUtil.isNetWorkConnected(mActivity)) {
            loadData();
        } else {
            loadCache();
        }

    }

    private void loadData() {
        RequestManager.addRequest(new Request4News(News.getRequestUrl(),
                new Response.Listener<ArrayList<News>>
                        () {
                    @Override
                    public void onResponse(ArrayList<News> response) {
                        getData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadFinisCallBack.loadFinish(null);
            }
        }), mActivity);
    }

    private void loadCache() {
        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
//        WeChatCache weChatCacheUtil = WeChatCache.getInstance(mActivity);
        if (page == 1) {
            mNews.clear();
            ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
        }
//        mNews.addAll(weChatCacheUtil.getCacheByPage(page));
        notifyDataSetChanged();
    }

    private void getData(final ArrayList<News> newses) {

        if (page == 1) {
            mNews.clear();
            //首次正常加载之后，清空之前的缓存
//                    WeChatCache.getInstance(mActivity).clearAllCache();
        }

        mNews.addAll(newses);
        notifyDataSetChanged();

        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.card)
        CardView card;
        @InjectView(R.id.ll_news)
        LinearLayout llNews;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_realtype)
        TextView tvRealtype;
        @InjectView(R.id.tv_author)
        TextView tvAuthor;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.iv_pic1)
        ImageView ivPic1;
        @InjectView(R.id.iv_pic2)
        ImageView ivPic2;
        @InjectView(R.id.iv_pic3)
        ImageView ivPic3;
        @InjectView(R.id.iv_share)
        ImageView ivShare;


        public NewsViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this,contentView);
        }
    }

}

