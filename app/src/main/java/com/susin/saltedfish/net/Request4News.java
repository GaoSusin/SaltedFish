package com.susin.saltedfish.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;
import com.susin.saltedfish.model.News;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 新闻头条数据请求器
 */
public class Request4News extends Request<ArrayList<News>> {

	private Response.Listener<ArrayList<News>> mListener;

	public Request4News(String url, Response.Listener<ArrayList<News>> listener,
						Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.mListener = listener;
	}

	@Override
	protected Response<ArrayList<News>> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			jsonStr = new JSONObject(jsonStr).getJSONObject("result").getJSONArray("data").toString();

			return Response.success((ArrayList<News>) JSONParser.toObject(jsonStr,
					new TypeToken<ArrayList<News>>() {
					}.getType()), HttpHeaderParser.parseCacheHeaders(response));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<News> response) {
		mListener.onResponse(response);
	}

}
