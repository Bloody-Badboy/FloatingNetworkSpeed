package com.bloodybadboycreation.floatingnetworkspeed.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BloodyBadboyCreation on 10-03-2016.
 */
public abstract class BroadcastReceiverBase extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(context,intent.getAction());
    }

    protected abstract void onReceive(Context context,String action);
}
