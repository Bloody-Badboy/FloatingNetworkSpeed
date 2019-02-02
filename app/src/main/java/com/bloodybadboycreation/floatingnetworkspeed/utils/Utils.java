package com.bloodybadboycreation.floatingnetworkspeed.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodybadboycreation.floatingnetworkspeed.R;
import com.bloodybadboycreation.floatingnetworkspeed.service.FloatingService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Arpan Bloody Badboy on 03-03-2016.
 */
public class Utils {

    public static boolean isDataEnabled(Context context) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityMgr.getActiveNetworkInfo() != null && connectivityMgr.getActiveNetworkInfo().isAvailable() && connectivityMgr.getActiveNetworkInfo().isConnected();
    }


    public static void checkAndStartService(Context context) {
        Intent mIntent = new Intent(context, FloatingService.class);
        if (isDataEnabled(context) && !isServiceRunning(context)) {
            Log.d("FloatingNetworkSpeed", "Stopping FloatingNetworkService");
            context.startService(mIntent);
        } else if (!isDataEnabled(context) && isServiceRunning(context)) {
            Log.d("FloatingNetworkSpeed", "Starting FloatingNetworkService");
            context.stopService(mIntent);
        }
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo mRunningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (mRunningServiceInfo.service.getClassName().equals(FloatingService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void Toast(Context context, CharSequence msg) {

        View mView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast, null);
        TextView mToastMsgView = (TextView) mView.findViewById(R.id.toast_msg);
        Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        mToast.setView(mView);
        mToastMsgView.setText(msg);
        mToast.show();
    }
}
