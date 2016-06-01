package hao.bk.com.common;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * Created by T430 on 4/26/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i("PubnubService", "PubNub BootReceiver Starting");
        Intent intent = new Intent(context, PubnubService.class);
        context.startService(intent);
        Log.i("PubnubService", "PubNub BootReceiver Started");
    }

}