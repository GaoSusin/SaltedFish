package com.susin.saltedfish.adapter;

import android.app.Activity;
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
import com.susin.saltedfish.base.ConstantString;
import com.susin.saltedfish.base.JDApplication;
import com.susin.saltedfish.callback.LoadFinishCallBack;
import com.susin.saltedfish.callback.LoadResultCallBack;
import com.susin.saltedfish.model.WeChat;
import com.susin.saltedfish.net.Request4WeChat;
import com.susin.saltedfish.net.RequestManager;
import com.susin.saltedfish.utils.NetWorkUtil;
import com.susin.saltedfish.utils.ShareUtil;
import com.susin.saltedfish.utils.ShowToast;
import com.susin.saltedfish.utils.UtilText;

import java.util.ArrayList;

public class WeChatAdapter extends RecyclerView.Adapter<WeChatAdapter.WeChatViewHolder> {

    private int page;
    private int lastPosition = -1;
    private ArrayList<WeChat> mWeChats;
    private Activity mActivity;
    private LoadResultCallBack mLoadResultCallBack;
    private LoadFinishCallBack mLoadFinisCallBack;

    public WeChatAdapter(Activity activity, LoadFinishCallBack loadFinisCallBack, LoadResultCallBack loadResultCallBack) {
        mActivity = activity;
        mLoadFinisCallBack = loadFinisCallBack;
        mLoadResultCallBack = loadResultCallBack;
        mWeChats = new ArrayList<>();
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
    public void onViewDetachedFromWindow(WeChatViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.card.clearAnimation();
    }

    @Override
    public WeChatViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_joke, parent, false);
        return new WeChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WeChatViewHolder holder, final int position) {

        final WeChat weChat = mWeChats.get(position);
        holder.tv_content.setText(weChat.getTitle());

        holder.img_share.setOnClickListener(new View.OnClickListener() {
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
                                        ShareUtil.shareText(mActivity, weChat.getTitle().trim());
                                        break;
                                    //复制
                                    case 1:
                                        UtilText.copy(mActivity, weChat.getTitle());
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
        return mWeChats.size();
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
        RequestManager.addRequest(new Request4WeChat(WeChat.getRequestUrl(page),
                new Response.Listener<ArrayList<WeChat>>
                        () {
                    @Override
                    public void onResponse(ArrayList<WeChat> response) {
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
            mWeChats.clear();
            ShowToast.Short(ConstantString.LOAD_NO_NETWORK);
        }
//        mWeChats.addAll(weChatCacheUtil.getCacheByPage(page));
        notifyDataSetChanged();
    }

    private void getData(final ArrayList<WeChat> weChats) {

        if (page == 1) {
            mWeChats.clear();
            //首次正常加载之后，清空之前的缓存
//                    WeChatCache.getInstance(mActivity).clearAllCache();
        }

        mWeChats.addAll(weChats);
        notifyDataSetChanged();

        mLoadFinisCallBack.loadFinish(null);
        mLoadResultCallBack.onSuccess(LoadResultCallBack.SUCCESS_OK, null);
    }

    public static class WeChatViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_author;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_like;
        private TextView tv_unlike;
        private TextView tv_comment_count;

        private ImageView img_share;
        private CardView card;
        private LinearLayout ll_comment;

        public WeChatViewHolder(View contentView) {
            super(contentView);

            tv_author = (TextView) contentView.findViewById(R.id.tv_author);
            tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            tv_like = (TextView) contentView.findViewById(R.id.tv_like);
            tv_unlike = (TextView) contentView.findViewById(R.id.tv_unlike);
            tv_comment_count = (TextView) contentView.findViewById(R.id.tv_comment_count);

            img_share = (ImageView) contentView.findViewById(R.id.img_share);
            card = (CardView) contentView.findViewById(R.id.card);
            ll_comment = (LinearLayout) contentView.findViewById(R.id.ll_comment);
        }
    }

}

