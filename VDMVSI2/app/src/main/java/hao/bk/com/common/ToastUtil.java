package hao.bk.com.common;

/**
 * Created by T430 on 3/18/2016.
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    Context context;
    Toast mToast;
    public ToastUtil(Context context) {
        this.context = context;
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            try {
                if(msg.what == 1){
                    String text = (String) msg.obj;

                    if(mToast!=null){
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    mToast.setGravity(Gravity.CENTER| Gravity.TOP, 0, 250);
                    LinearLayout layout = (LinearLayout) mToast.getView();
                    if (layout.getChildCount() > 0) {
                        TextView tv = (TextView) layout.getChildAt(0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    }
                    mToast.show();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        };
    };

    public void showToast(String text){
        if(TextUtils.isEmpty(text)) return;
        Message msg = handler.obtainMessage(1, text);
        handler.sendMessage(msg);
    }

}
