package hao.bk.com.customview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import hao.bk.com.vdmvsi.R;

public class MyProgressDialog {

	ProgressDialog pgdl;
	Context c;
	public MyProgressDialog(Context c) {
		this.c = c;
		pgdl = new ProgressDialog(c);
	}
	
	public void showLoading(String msg) {
		if(TextUtils.isEmpty(msg)){
			pgdl.setMessage(c.getString(R.string.txt_loading));
		}
		else {
			pgdl.setMessage(msg);
		}
		pgdl.show();
		pgdl.setCancelable(false);
	}

	public void hideLoading() {
		try {
			if(Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2){
				if(pgdl!=null && pgdl.isShowing() && !((Activity)c).isDestroyed()) pgdl.dismiss();
			}
			else{
				if(pgdl!=null && pgdl.isShowing()) pgdl.dismiss();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
