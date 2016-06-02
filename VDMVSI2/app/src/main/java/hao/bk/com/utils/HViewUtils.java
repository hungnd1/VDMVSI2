package hao.bk.com.utils;

import android.os.SystemClock;

import java.text.DateFormat;

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
        return DateFormat.getDateInstance(DateFormat.SHORT).format(miliseconds*1000);
    }
}
