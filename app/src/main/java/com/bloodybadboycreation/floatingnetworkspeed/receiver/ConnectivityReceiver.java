package com.bloodybadboycreation.floatingnetworkspeed.receiver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.bloodybadboycreation.floatingnetworkspeed.base.BroadcastReceiverBase;
import com.bloodybadboycreation.floatingnetworkspeed.utils.Utils;

/**
 * Created by BloodyBadboyCreation on 10-03-2016.
 */
public class ConnectivityReceiver extends BroadcastReceiverBase {

    @Override
    protected void onReceive(Context context, String action) {
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d("FloatingNetworkSpeed", "Received Intent: android.net.conn.CONNECTIVITY_CHANGE");
            Utils.checkAndStartService(context);
        }
    }
}
