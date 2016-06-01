package hao.bk.com.common;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import hao.bk.com.vdmvsi.R;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MposRestClient {
	private static MposRestClient myClient;
	private static AsyncHttpClient client;// = new AsyncHttpClient();
	private static PersistentCookieStore cookie;
	private static final int TIME_OUT = 60000;

	public static MposRestClient getInstance(Context context) {
		if(myClient==null){
			myClient = new MposRestClient();
		}
		if(client==null){
			client = new AsyncHttpClient();
		}
		if(cookie==null){
			cookie = new PersistentCookieStore(context); 
		}
		client.setCookieStore(cookie);
		client.setTimeout(TIME_OUT);
		return myClient;
	}
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT);
		client.get(url, params, responseHandler);
	}
	public void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT);
		client.get(url, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT);
		client.post(url, params, responseHandler);
	}

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(TIME_OUT);
		client.post(url, responseHandler);
	}

	public void post(Context c, String url, StringEntity entity, String cType, AsyncHttpResponseHandler responseHandler) {
		if(UtilNetwork.checkInternet(c, c.getString(R.string.error_no_network))){
			client.post(c, url, entity, cType, responseHandler);
		}
	}

	public static void setCookieStore(PersistentCookieStore cookieStore) {
		client.setCookieStore(cookieStore);
	}

	public static void cancelRQ() {
		client.cancelAllRequests(true);
	}
}
