package com.susin.saltedfish.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class History implements Serializable {

    public static final String URL_HISTORY= "http://api.juheapi.com/japi/toh";
    private static final Object APPKEY = "0945b46d9d55f9af6176695be06aac74";


    private String des;
    private String id;
    private String lunar;
    private String pic;
    private String title;
    private String year;

    public static String getRequestUrl() {

        Calendar c = Calendar.getInstance();
        int month =  c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);

        Map params = new HashMap();// 请求参数
        params.put("key",APPKEY);// 应用APPKEY(应用详细页查询)
        params.put("v","1.0");//应用版本
        params.put("month",month);//应用APPKEY(应用详细页查询)
        params.put("day",day);//返回数据的格式,xml或json，默认json

        Map<String,Object> data = params;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return URL_HISTORY + "?" + sb.toString();
    }


    public History() {
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
