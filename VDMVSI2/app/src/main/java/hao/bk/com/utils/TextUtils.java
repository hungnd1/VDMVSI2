package hao.bk.com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return sdf2.format(stringToDate(cdate));
    }
}
