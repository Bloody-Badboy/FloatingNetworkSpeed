/*
 * Copyright (C) 2015 BloodyBadboyCreation, Inc. (Arpan Bloody Badboy)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bloodybadboycreation.floatingnetworkspeed.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bloodybadboycreation.floatingnetworkspeed.R;

import java.text.DecimalFormat;

/**
 * Created by Arpan Bloody Badboy on 24-12-2015.
 */
public class TrafficView extends LinearLayout {

    private final String TAG = "TrafficLayout";
    private boolean mAttached = false;
    private long mTimeDuration = 0;
    private long mTotalTxBytes = 0;
    private long mTotalRxBytes = 0;
    private long mRxSpeed = 0;
    private long mTxSpeed = 0;
    private long mLastUpdateTime = 0;
    private Runnable mRunnable = null;
    private Handler mHandler = null;
    private TextView mTxSpeedView = null;
    private TextView mRxTextView = null;
    private TextView mTxSpeedUnitView = null;
    private TextView mRxTextUnitView = null;
    private Context mContext = null;


    public TrafficView(Context context) {
        super(context);
        mContext = context;
    }

    public TrafficView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TrafficView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            initLayout();
            mHandler = new Handler();
            triggerAutoUpdate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            mAttached = false;
        }
    }

    private void initLayout() {
        mTxSpeedView = (TextView) getRootView().findViewById(R.id.uploadSpeed);
        mRxTextView = (TextView) getRootView().findViewById(R.id.downloadSpeed);
        mTxSpeedUnitView = (TextView) getRootView().findViewById(R.id.uploadUnit);
        mRxTextUnitView = (TextView) getRootView().findViewById(R.id.downloadUnit);
        TextView mTxPrefixView = (TextView) getRootView().findViewById(R.id.uploadPrefix);
        TextView mRxPrefixView = (TextView) getRootView().findViewById(R.id.downloadPrefix);

        Typeface mUnitFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/arrow_mod.ttf");
        mTxPrefixView.setTypeface(mUnitFace);
        mRxPrefixView.setTypeface(mUnitFace);

        mTxPrefixView.setText("a");
        mRxPrefixView.setText("b");

       /*mRxTextView = new TextView(mContext);
        LayoutParams mRxTextViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRxTextView.setLayoutParams(mRxTextViewParams);
        mRxTextView.setGravity(Gravity.RIGHT);
        mRxTextView.setTextSize(13 * mDisplayDensity);
        mRxTextView.setTextColor(Color.WHITE);

        mTxSpeedView = new TextView(mContext);
        LayoutParams mTxTextViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTxSpeedView.setLayoutParams(mTxTextViewParams);
        mTxSpeedView.setGravity(Gravity.RIGHT);
        mTxSpeedView.setTextSize(13 * mDisplayDensity);
        mTxSpeedView.setTextColor(Color.WHITE);

        setOrientation(VERTICAL);
        addView(mTxSpeedView);
        addView(mRxTextView);*/
    }


    private String[] convertSize(long trafficSpeed) {

        String unit;
        float KB = 1024f;
        float MB = KB * 1024f;
        float value = 0;
        unit = "B/s";
        if (trafficSpeed > 0) {
            if ((float) trafficSpeed / MB >= 1) {
                value = (float) trafficSpeed / MB;
                unit = "MB/s";
            } else if ((float) trafficSpeed / KB >= 1) {
                value = (float) trafficSpeed / KB;
                unit = "KB/s";
            } else {
                value = (float) trafficSpeed;
                unit = "B/s";
            }
        }
        String ret[] = new String[2];
        ret[0] = new DecimalFormat("##0.0").format(value);
        ret[1] = unit;
        return ret;
    }

    private void triggerAutoUpdate() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTimeDuration = SystemClock.elapsedRealtime() - mLastUpdateTime;
                if (mTimeDuration == 0) {
                    Log.d(TAG, "TimeDuration: " + mTimeDuration);
                } else {

                    mRxSpeed = (TrafficStats.getTotalRxBytes() - mTotalRxBytes) * 1000 / mTimeDuration;
                    mTxSpeed = (TrafficStats.getTotalTxBytes() - mTotalTxBytes) * 1000 / mTimeDuration;

                    mTotalTxBytes = TrafficStats.getTotalTxBytes();
                    mTotalRxBytes = TrafficStats.getTotalRxBytes();

                    mLastUpdateTime = SystemClock.elapsedRealtime();

                    mTxSpeedView.setText(convertSize(mTxSpeed)[0]);
                    mRxTextView.setText(convertSize(mRxSpeed)[0]);

                    mTxSpeedUnitView.setText(convertSize(mTxSpeed)[1]);
                    mRxTextUnitView.setText(convertSize(mRxSpeed)[1]);
                }
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 0);
    }

}
