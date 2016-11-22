package com.susin.saltedfish.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.susin.saltedfish.net.RequestManager;
import com.susin.saltedfish.utils.UtilText;

import butterknife.ButterKnife;

/**
 * 说明：
 *
 * @作者 Susin
 * @创建时间 2016/6/27 21:11
 * @版本
 * @------修改记录-------
 * @修改人
 * @版本
 * @修改内容
 */
public abstract class BaseActivity extends AppCompatActivity implements ConstantString{

    protected BaseActivity mContext = this;
    /**
     * set layout of this activity
     *
     * @return the id of layout
     */

    protected abstract void initView();

    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.inject(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * UI绑定
     *
     * @作者 huangssh
     * @创建时间 2015-8-6 上午11:20:48
     * @param id
     * @return
     */
    public <T extends View> T findView(int id) {
        T view = null;
        View genericView = findViewById(id);
        try {
            view = (T) (genericView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * UI绑定(通过父级view)
     *
     * @作者 huangssh
     * @创建时间 2015-8-6 上午11:23:21
     * @param parentView 父级view
     * @param id
     * @return
     */
    public <T extends View> T findView(View parentView, int id) {
        T view = null;
        View genericView = parentView.findViewById(id);
        try {
            view = (T) (genericView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 短时提示
     *
     * @作者 huangssh
     * @创建时间 2015-8-6 上午9:05:26
     * @param text 显示的文本
     */
    public void showToastShort(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时提示
     *
     * @作者 huangssh
     * @创建时间 2015-8-6 上午9:05:26
     * @param text 显示的文本
     */
    public void showToastLong(String text){
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

//    /**
//     * 通讯失败提醒
//     *
//     * @作者 huangssh
//     * @创建时间 2015-8-12 上午10:59:56
//     * @param obj 继承Response的对象,用于获取失败信息
//     */
//    public void showTaskFailToast(Object obj){
//        if(obj==null){
//            showToastLong("网络不给力啊，请稍后再试~");
//        }else{
//            try{
//                String desc = ((Response)obj).getResultDesc();
//                if(!UtilText.isEmptyOrNull(desc)){
//                    showToastLong(desc);
//                }
//            }catch(Exception e){
//            }
//        }
//    }

    public void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }
}
