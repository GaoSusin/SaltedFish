package com.susin.saltedfish.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;
import com.susin.saltedfish.model.WeChat;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 微信数据请求器
 */
public class Request4WeChat extends Request<ArrayList<WeChat>> {

	private Response.Listener<ArrayList<WeChat>> mListener;

	public Request4WeChat(String url, Response.Listener<ArrayList<WeChat>> listener,
                          Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.mListener = listener;
	}

	@Override
	protected Response<ArrayList<WeChat>> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			jsonStr = new JSONObject(jsonStr).getJSONObject("result").getJSONArray("list").toString();

			return Response.success((ArrayList<WeChat>) JSONParser.toObject(jsonStr,
					new TypeToken<ArrayList<WeChat>>() {
					}.getType()), HttpHeaderParser.parseCacheHeaders(response));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<WeChat> response) {
		mListener.onResponse(response);
	}

}
