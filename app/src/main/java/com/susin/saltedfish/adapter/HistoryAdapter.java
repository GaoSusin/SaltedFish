package com.susin.saltedfish.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.susin.saltedfish.R;
import com.susin.saltedfish.base.ConstantString;
import com.susin.saltedfish.base.JDApplication;
import com.susin.saltedfish.callback.LoadFinishCallBack;
import com.susin.saltedfish.callback.LoadResultCallBack;
import com.susin.saltedfish.model.History;
import com.susin.saltedfish.net.Request4History;
import com.susin.saltedfish.net.RequestManager;
import com.susin.saltedfish.utils.NetWorkUtil;
import com.susin.saltedfish.utils.ShareUtil;
import com.susin.saltedfish.utils.ShowToast;
import com.susin.saltedfish.utils.UtilText;
import com.susin.saltedfish.view.ImageLoadProxy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private int page;
    private int lastPosition = -1;
    private ArrayList<History>  mHistory;
    private Activity mActivity;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinisCallBack;

    public HistoryAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        mHistory = new ArrayList<>();
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
    public void onViewDetachedFromWindow(HistoryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position) {

        final History history = mHistory.get(position);
        holder.tvDate.setText(history.getYear());
        holder.tvTitle.setText(history.getTitle());
        ImageLoadProxy.displayImageList(history.getPic(), holder.ivPic,  R.drawable.ic_loading_large, null, null);
        holder.tvDes.setText(history.getDes());
        setAnimation(holder.card, position);
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
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
        RequestManager.addRequest(new Request4History(History.getRequestUrl(),
                new Response.Listener<ArrayList<History>>
                        () {
                    @Override
                    public void onResponse(ArrayList<History> response) {
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
//        HistoryCache historyCacheUtil = HistoryCache.getInstance(mActivity);
        if (page == 1) {
            mHistory.clear();
            ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
        }
//        mHistorys.addAll(historyCacheUtil.getCacheByPage(page));
        notifyDataSetChanged();
    }

    private void getData(final ArrayList<History> historys) {

        if (page == 1) {
            mHistory.clear();
            //首次正常加载之后，清空之前的缓存
//                    HistoryCache.getInstance(mActivity).clearAllCache();
        }

        mHistory.addAll(historys);
        notifyDataSetChanged();

        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.card)
        CardView card;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.iv_pic)
        ImageView ivPic;
        @InjectView(R.id.tv_des)
        TextView tvDes;


        public HistoryViewHolder(View contentView) {
            super(contentView);
            ButterKnife.inject(this, contentView);

        }
    }

}

