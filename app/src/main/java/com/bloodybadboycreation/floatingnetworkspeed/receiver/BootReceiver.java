package com.bloodybadboycreation.floatingnetworkspeed.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bloodybadboycreation.floatingnetworkspeed.base.BroadcastReceiverBase;
import com.bloodybadboycreation.floatingnetworkspeed.utils.Utils;

/**
 * Created by Flawless Badboy on 28-02-2016.
 */
public class BootReceiver extends BroadcastReceiverBase {

    @Override
    protected void onReceive(Context context, String action) {
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d( "FloatingNetworkSpeed","Received Intent: android.intent.action.BOOT_COMPLETED");
            Utils.checkAndStartService(context);
        }
    }
}
