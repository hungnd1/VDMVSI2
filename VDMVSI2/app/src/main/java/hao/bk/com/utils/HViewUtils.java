package hao.bk.com.utils;

import android.os.SystemClock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by T430 on 4/24/2016.
 */
public class HViewUtils {

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 500) { //0,5s
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String getTimeViaMiliseconds(long miliseconds){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(miliseconds*1000);
    }
}
