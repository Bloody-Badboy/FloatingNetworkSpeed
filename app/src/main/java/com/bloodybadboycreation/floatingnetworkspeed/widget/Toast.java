package com.bloodybadboycreation.floatingnetworkspeed.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bloodybadboycreation.floatingnetworkspeed.R;

/**
 * Created by BloodyBadboyCreation on 10-03-2016.
 */
public class Toast extends android.widget.Toast {

    @SuppressLint("InflateParams")
    public Toast(Context context, String msg) {
        super(context);
        View mView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast, null);
        TextView mToastMsgView = (TextView) mView.findViewById(R.id.toast_msg);
        makeText(context, null, android.widget.Toast.LENGTH_SHORT);
        setView(mView);
        mToastMsgView.setText(msg);
    }

    @Override
    public void show() {
        super.show();
    }
}
