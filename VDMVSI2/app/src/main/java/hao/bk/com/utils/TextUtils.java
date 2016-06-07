package hao.bk.com.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by T430 on 4/21/2016.
 */
public class TextUtils {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM");

    public static boolean isEmpty(String str){
        if(str == null || str.equals(""))
            return true;
        return false;
    }

    public static boolean equalTime(String date1, String date2){
        return date1.startsWith(date2.substring(0,16));
    }

    public static String dateToString(Date date){
        return sdf.format(date);
    }

    public static Date stringToDate(String s){
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }


    public static String toSimpleDate(String cdate) {
        Log.d("track",cdate);
        Date d = stringToDate(cdate);
        Date now = new Date();
        long distance = now.getTime() - d.getTime();
        if (distance < TimeUnit.MINUTES.toMillis(1)){
            return "Vừa xong";
        }
        if (distance < TimeUnit.HOURS.toMillis(1)){
            return (distance/TimeUnit.MINUTES.toMillis(1))+" phút";
        }
        if (distance < TimeUnit.DAYS.toMillis(1)){
            return (distance/TimeUnit.HOURS.toMillis(1))+" giờ";
        }
        if (distance < TimeUnit.DAYS.toMillis(5)){
            return (distance/TimeUnit.DAYS.toMillis(1))+" ngày";
        }
        return sdf2.format(stringToDate(cdate));
    }

}
