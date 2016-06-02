package hao.bk.com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by T430 on 4/21/2016.
 */
public class TextUtils {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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


}
