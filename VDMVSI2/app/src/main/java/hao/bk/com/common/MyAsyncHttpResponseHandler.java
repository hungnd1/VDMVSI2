package hao.bk.com.common;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONObject;
import hao.bk.com.utils.Util;

public abstract class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
	String tag = this.getClass().getName();
	Context context;
	public MyAsyncHttpResponseHandler(Context context) {
		this.context = context;
	}

	@Override
	public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

	}

	@Override
	public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

	}

	//	@Override
//	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//		Util.LOGD(tag, "--on success in MyAsyncHttpResponseHandler--------" + new String(arg2));
//		// parse check time out
//		try {
//			JSONObject jroot = new JSONObject(new String(arg2));
//			if(jroot.has("error")){
//				JSONObject jerror = jroot.getJSONObject("error");
//				//String code = JsonParser.getDataJson(jerror, "code");
//			}
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
//
//		onSuccessOk(arg0, arg1, arg2);
//	}

	public abstract void onSuccessOk(int arg0, cz.msebera.android.httpclient.entity.mime.Header[] arg1, byte[] arg2);
	public abstract void onFailTimeout();

//	@Override
//	public void setCharset(String charset) {
//		super.setCharset(charset);
//	}c

	private void showDialogTimeOut(final Context context) {

//		final MyDialog mdialog = new MyDialog(context, R.style.DialogRadius);
//		mdialog.setCancelable(false);
//		mdialog.setTitle(context.getString(R.string.txt_note));
//		mdialog.setBodyText(context.getString(R.string.error_timeout_content));
//		mdialog.setTextOk(context.getString(R.string.btn_ok));
//		mdialog.setOnClickListenerOk(new OnClickListener() {
//			public void onClick(View v) {
//				mdialog.dismiss();
//			}
//		});
//		mdialog.setOnClickListenerCancel(null);
//		mdialog.show();
	}
}
