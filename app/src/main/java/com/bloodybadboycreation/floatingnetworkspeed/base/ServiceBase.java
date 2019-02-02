package com.bloodybadboycreation.floatingnetworkspeed.base;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by BloodyBadboyCreation on 10-03-2016.
 */
public class ServiceBase extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public WindowManager getWindowManager(){
        return (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
    }

    public WindowManager.LayoutParams getViewParams(View view) {
        return (WindowManager.LayoutParams) view.getLayoutParams();
    }

}
