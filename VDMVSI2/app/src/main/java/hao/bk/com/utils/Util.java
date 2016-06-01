package hao.bk.com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {

	public static boolean mDEBUG = true;// release = false

	public static void LOGI(String tag, String msg) {
		if (mDEBUG)
			android.util.Log.i(tag, msg!=null?msg:"");
	}

	public static void LOGE(String tag, String msg) {
		if (mDEBUG)
			android.util.Log.e(tag, msg != null ? msg : "");
	}

	public static void LOGD(String tag, String msg) {
		if (mDEBUG)
			android.util.Log.d(tag, msg != null ? msg : "");
	}

	public static void LOGW(String tag, String msg) {
		if (mDEBUG)
			android.util.Log.w(tag, msg != null ? msg : "");
	}

	public static void mToast(Context c, String msg) {
		Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
	}

	public static String convertTimestamp(long timeStamp) {
		Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(timeStamp);
		return mydate.get(Calendar.HOUR_OF_DAY) + "h" + mydate.get(Calendar.MINUTE) + "p - " + mydate.get(Calendar.DAY_OF_MONTH) + "/" + (mydate.get(Calendar.MONTH) + 1) + "/"
				+ mydate.get(Calendar.YEAR);
	}

	public static long returnDate(long timeStamp) {
		Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(timeStamp);
		//		mydate.set(Calendar.HOUR_OF_DAY, 0);
		//		mydate.set(Calendar.MINUTE, 0);
		//		mydate.set(Calendar.SECOND, 0);
		Calendar result = Calendar.getInstance();
		result.set(mydate.get(Calendar.YEAR), mydate.get(Calendar.MONTH), mydate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		result.set(Calendar.MILLISECOND, 0);
		//		Calendar result = Calendar.getInstance();
		//		result.set(mydate.get(Calendar.YEAR), mydate.get(Calendar.MONTH), mydate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return result.getTimeInMillis();
	}

	public static String convertTimestamp(long timeStamp, int flag) {
		Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(timeStamp);
		switch (flag) {
			case 1 :
				return new SimpleDateFormat("HH:mm").format(mydate.getTime());
			case 2 :
				return new SimpleDateFormat("dd/MM/yyyy").format(mydate.getTime());
			default :
				break;
		}
		return "";
	}

	public static String convertTimestampB(long timeStamp) {
		Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(timeStamp * 1000);
		return mydate.get(Calendar.DAY_OF_MONTH) + "/" + (mydate.get(Calendar.MONTH) + 1) + "/" + mydate.get(Calendar.YEAR);
	}

	public static boolean appInstalledOrNot(Context c, String uri) {
		PackageManager pm = c.getPackageManager();
		PackageInfo p;
		//String version_name = "";
		boolean app_installed = false;
		try {
			p = pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			//version_name = p.versionName;

			app_installed = true;

		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public static String zenMoney(String money) {
//		return String.format("%,d", Long.parseLong(money)).replace(",", ".");
		return String.format("%,d", Long.parseLong(money)).replace(".", ",");
	}


	public static boolean findBinary(String binaryName) {
		boolean found = false;
		if (!found) {
			String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
			for (String where : places) {
				if (new File(where + binaryName).exists()) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	public static boolean checkValidPhoneNumber(String phoneNumber) {

		if (!isNumeric(phoneNumber)) {
			return false;
		}

		if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
			return false;
		}

		if (phoneNumber.startsWith("09") && phoneNumber.length() != 10) {
			return false;
		}

		if (phoneNumber.startsWith("01") && phoneNumber.length() != 11) {
			return false;
		}

		if (parseTelephoneProvider(phoneNumber) == null) {
			return false;
		}

		return true;
	}

	public static String parseTelephoneProvider(String phoneNunber) {
		String phone = formatPhoneNumber(phoneNunber);
		String[] viettelPrefixes = new String[]{"096", "097", "098", "0162", "0163", "0164", "0165", "0166", "0167", "0168", "0169"};
		String[] mobifonePrefixes = new String[]{"090", "093", "0120", "0121", "0122", "0126", "0128"};
		String[] vinaphonePrefixes = new String[]{"091", "094", "0123", "0124", "0125", "0127", "0129"};
		String[] vietnamobilePrefixes = new String[]{"092", "0188", "0186"};
		String[] beelinePrefixes = new String[]{"099", "0199"};
		String[] sfonePrefixes = new String[]{"095"};

		for (String prefix : viettelPrefixes) {
			if (phone.startsWith(prefix)) {
				return "VIETTEL";
			}
		}
		for (String prefix : mobifonePrefixes) {
			if (phone.startsWith(prefix)) {
				return "MOBIFONE";
			}
		}
		for (String prefix : vinaphonePrefixes) {
			if (phone.startsWith(prefix)) {
				return "VINAPHONE";
			}
		}
		for (String prefix : vietnamobilePrefixes) {
			if (phone.startsWith(prefix)) {
				return "VIETNAMOBILE";
			}
		}
		for (String prefix : beelinePrefixes) {
			if (phone.startsWith(prefix)) {
				return "BEELINE";
			}
		}
		for (String prefix : sfonePrefixes) {
			if (phone.startsWith(prefix)) {
				return "SFONE";
			}
		}
		return null;
	}

	public static String formatPhoneNumber(String s) {
		if (s.startsWith("84")) {
			return "0" + s.substring(2);
		} else if (s.startsWith("+84")) {
			return "0" + s.substring(3);
		} else {
			return s;
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public static boolean isRooted() {
		return findBinary("su");
	}
	
	public static void dismisKeyBroad(Context c){
		if(c!=null)
			((Activity)c).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
	}
	
	public static String formatStyleCss(String text){
		
		return " <b>"+text+"</b>";
//		return " <font color=\"#ffffff\" style=\"bold\">"+text+"</font>";
	}
	
	public static String getStringUnicode(byte[] response) {
		try {
			return new String(response,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
			return new String(response);
		}
	}
	
	public static String getTextInEdt(EditText edt){
		return edt==null?"":edt.getText().toString().trim();
	}
	
	public static boolean changeLangDefault(Context context, String lang){
		if(context==null || TextUtils.isEmpty(lang)){
			return false;
		}
		Locale myLocale = new Locale(lang);
		Locale.setDefault(myLocale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = myLocale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
		return true;
	}
}
