package hao.bk.com.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by T430 on 11/16/2015.
 */
public class DataStoreApp {

    // Shared Preferences
    SharedPreferences prefs;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    private String USER_NAME="USER_NAME";
    private String PASSWORDS="PASSWORDS";
    private String AVATAR_URL="AVATAR";
    private String CHAT_ACTIVITY_SHOW="CHAT_ACTIVITY_SHOW";
    private String VIP="VIP";

    public DataStoreApp(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }
    public void createUserName(String userName){
        editor = prefs.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }
    public String getUserName(){
        return prefs.getString(USER_NAME, "");
    }
    public void createPassword(String password){
        editor = prefs.edit();
        editor.putString(PASSWORDS, password);
        editor.commit();
    }

    public String getPassword(){
        return prefs.getString(PASSWORDS, "");
    }

    public void createAvatar(String password){
        editor = prefs.edit();
        editor.putString(AVATAR_URL, password);
        editor.commit();
    }

    public void createFriendAvatar(String friendName, String friendAvatar){
        editor = prefs.edit();
        editor.putString(AVATAR_URL+"_"+friendName, friendAvatar);
        editor.commit();
    }

    public String getAvatar(){
        return prefs.getString(AVATAR_URL, "");
    }

    public String getFriendAvatar(String friendName){
        return prefs.getString(AVATAR_URL+"_"+friendName, "");
    }

    public void createVip(boolean isVip){
        editor = prefs.edit();
        editor.putBoolean(VIP, isVip);
        editor.commit();
    }

    public boolean isVip(){
        return prefs.getBoolean(VIP, false);
    }
    public void setChatActivityShowing(boolean flag){
        editor = prefs.edit();
        editor.putBoolean(CHAT_ACTIVITY_SHOW, flag);
        editor.commit();
    }

    public boolean getChatActivityShowing(){
        return prefs.getBoolean(CHAT_ACTIVITY_SHOW, false);
    }

}
