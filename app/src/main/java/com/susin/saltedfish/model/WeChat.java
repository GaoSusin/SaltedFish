package com.susin.saltedfish.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WeChat implements Serializable {

    public static final String URL_WEIXIN = "http://v.juhe.cn/weixin/query";
    private static final Object APPKEY = "958c21d2326505ff9f86de6b02aa5e73";

    private String id;
    private String title;
    private String source;
    private String firstImg;
    private String mark;
    private String url;

    public static String getRequestUrl(int page) {
        Map params = new HashMap();//请求参数
        params.put("pno",page);//当前页数，默认1
        params.put("ps","10");//每页返回条数，最大100，默认20
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json

        Map<String,Object> data = params;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return URL_WEIXIN + "?" + sb.toString();
    }


    public WeChat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
