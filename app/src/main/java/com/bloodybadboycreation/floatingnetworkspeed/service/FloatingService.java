package com.bloodybadboycreation.floatingnetworkspeed.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.bloodybadboycreation.floatingnetworkspeed.MainActivity;
import com.bloodybadboycreation.floatingnetworkspeed.R;
import com.bloodybadboycreation.floatingnetworkspeed.base.ServiceBase;

public class FloatingService extends ServiceBase implements View.OnTouchListener {

    public final static int NOTIFICATION_ID = 71;
    MoveAnimator mAnimator = new MoveAnimator();
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
   /* private int mStatusBarHeight;*/
    private Point mDisplaySize = new Point();

    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        super.onCreate();

        mView = LayoutInflater.from(this).inflate(R.layout.monitor_layout, null);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("FloatingNetwork","onTouch()");
                return false;
            }
        });

        getWindowManager().getDefaultDisplay().getSize(mDisplaySize);

       /* mStatusBarHeight = getStatusBarHeight();*/

        mLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 100;

        getWindowManager().addView(mView, mLayoutParams);


        stopForeground(true);
        startService(new Intent(this, HideNotificationService.class));
        startForeground(NOTIFICATION_ID, new Notification.Builder(this)
                .setContentTitle("").setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(new RemoteViews(getPackageName(), R.layout.remote_view))
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), Intent.FILL_IN_DATA))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN)
                .build()
        );


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        if (mView != null)
            getWindowManager().removeView(mView);
    }

    /*private int getStatusBarHeight() {
        return (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        WindowManager.LayoutParams mNewLayoutParams = mLayoutParams;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                initialTouchX = (int) event.getRawX();
                initialTouchY = (int) event.getRawY();

                initialX = mNewLayoutParams.x;
                initialY = mNewLayoutParams.y;

                if (mAnimator.isAnimationRunning()) {
                    mAnimator.stop();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                int moveDeltaX = (int) (event.getRawX() - initialTouchX);
                int moveDeltaY = (int) (event.getRawY() - initialTouchY);

                int finalX = initialX + moveDeltaX;
                int finalY = initialY + moveDeltaY;

                /*if (finalY < 0) {
                    finalY = 0;
                } else if (finalY + (mView.getHeight() + mStatusBarHeight) > mDisplaySize.y) {
                    finalY = mDisplaySize.y - (mView.getHeight() + mStatusBarHeight);
                }*/

                mNewLayoutParams.x = finalX;
                mNewLayoutParams.y = finalY;

                getWindowManager().updateViewLayout(mView, mNewLayoutParams);
                break;

            case MotionEvent.ACTION_UP:
                /**
                 int upDeltaX = (int) (event.getRawX() - initialTouchX);
                 int upDeltaY = (int) (event.getRawY() - initialTouchY);

                 finalX = initialX + upDeltaX;
                 finalY = initialY + upDeltaY;

                 if (finalY < 0) {
                 finalY = 0;
                 } else if (finalY + (mView.getHeight() + mStatusBarHeight) > mDisplaySize.y) {
                 finalY = mDisplaySize.y - (mView.getHeight() + mStatusBarHeight);
                 }
                 mNewLayoutParams.y = finalY;*/
                goToWall();
                break;
            default:
                break;
        }
        return false;
    }

    public void goToWall() {
        int width = mDisplaySize.x - mView.getWidth();
        int middle = width / 2;
        float nearestXWall = getViewParams(mView).x >= middle ? width : 0;
        mAnimator.start(nearestXWall, getViewParams(mView).y);
    }

    private void move(float deltaX, float deltaY) {
        getViewParams(mView).x += deltaX;
        getViewParams(mView).y += deltaY;
        getWindowManager().updateViewLayout(mView, getViewParams(mView));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindowManager().getDefaultDisplay().getSize(mDisplaySize);

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (getViewParams(mView).y + (mView.getHeight()/* + getStatusBarHeight()*/) > mDisplaySize.y) {
                    getViewParams(mView).y = mDisplaySize.y - (mView.getHeight() /*+ getStatusBarHeight()*/);
                    getWindowManager().updateViewLayout(mView, getViewParams(mView));
                }
                if ((getViewParams(mView).x != 0) && (getViewParams(mView).x < mDisplaySize.x)) {
                    goToWall();
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (getViewParams(mView).x > mDisplaySize.x) {
                    goToWall();
                }
                break;
            default:
                break;
        }
    }

    private class MoveAnimator implements Runnable {
        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;
        private boolean running = false;

        private void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
            running = true;
        }

        boolean isAnimationRunning() {
            return running;
        }

        @Override
        public void run() {
            if (mView.getRootView() != null && mView.getRootView().getParent() != null) {
                float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 500f);
                float deltaX = (destinationX - getViewParams(mView).x) * progress;
                float deltaY = (destinationY - getViewParams(mView).y) * progress;
                move(deltaX, deltaY);
                if (progress < 1) {
                    handler.post(this);
                }
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
            running = false;
        }
    }
}
