package com.bloodybadboycreation.floatingnetworkspeed.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.bloodybadboycreation.floatingnetworkspeed.MainActivity;
import com.bloodybadboycreation.floatingnetworkspeed.R;

/**
 * Created by BloodyBadboyCreation on 22-10-2016.
 */

public class HideNotificationService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        super.onCreate();
        Log.d("HideNotificationService", "onCreate()");
        startForeground(FloatingService.NOTIFICATION_ID, new Notification.Builder(this)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(new RemoteViews(getPackageName(), R.layout.remote_view))
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), Intent.FILL_IN_DATA))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN).build());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HideNotificationService.this.stopSelf();
                Log.d("HideNotificationService", "stopSelf()");
            }
        }, 30);
    }
}