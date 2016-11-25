package com.susin.saltedfish.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;
import com.susin.saltedfish.model.History;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 微信数据请求器
 */
public class Request4History extends Request<ArrayList<History>> {

	private Response.Listener<ArrayList<History>> mListener;

	public Request4History(String url, Response.Listener<ArrayList<History>> listener,
						   Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.mListener = listener;
	}

	@Override
	protected Response<ArrayList<History>> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			jsonStr = new JSONObject(jsonStr).getJSONArray("result").toString();

			return Response.success((ArrayList<History>) JSONParser.toObject(jsonStr,
					new TypeToken<ArrayList<History>>() {
					}.getType()), HttpHeaderParser.parseCacheHeaders(response));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(ArrayList<History> response) {
		mListener.onResponse(response);
	}

}
