package hao.bk.com.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

public class UtilNetwork {

	public static boolean checkInternet(Context c) {
		ConnectivityManager connec = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi!=null && wifi.isConnected()) {
			return true;
		}
		android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobile!=null && mobile.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static boolean checkInternet(final Context c, final String msgError){
		boolean result = checkInternet(c);
		if(!result && !TextUtils.isEmpty(msgError)){
			try {
//				((Activity) c).runOnUiThread(new Runnable() {
//					public void run() {
//						Toast.makeText(c, msgError, Toast.LENGTH_LONG).show();
//					}
//				});
				ToastUtil toastUtil = new ToastUtil(c);
				toastUtil.showToast(msgError);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return result;
	}
}
