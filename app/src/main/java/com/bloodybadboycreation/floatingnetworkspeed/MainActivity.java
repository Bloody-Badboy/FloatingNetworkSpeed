package com.bloodybadboycreation.floatingnetworkspeed;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.bloodybadboycreation.floatingnetworkspeed.service.FloatingService;
import com.bloodybadboycreation.floatingnetworkspeed.utils.Utils;

public class MainActivity extends Activity implements View.OnClickListener {

    private final int REQUEST_CODE = 1001;
    Button mStartStopServiceBtn;
    ImageView mRotationIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartStopServiceBtn = (Button) findViewById(R.id.start_stop_button);
        mRotationIndicator = (ImageView) findViewById(R.id.indicator);
        mStartStopServiceBtn.setOnClickListener(this);

        /*WallpaperManager mWallpaperManager = WallpaperManager.getInstance(this);
        Drawable mCurrentWallPaper = mWallpaperManager.getDrawable();
        Bitmap mWallPaperBitmap = BitmapUtils.getBitmapFromDrawable(mCurrentWallPaper);

        new BlurTask(mWallPaperBitmap, 15, new BlurTask.OnBlurCompleteListener() {
            @Override
            public void OnBlurComplete(Bitmap blurBitmap) {
                getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(),blurBitmap));
            }
        }).execute();*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showDrawOverlayPermissionAskDialog();
            } else {
                Utils.checkAndStartService(this);
            }
        } else {
            Utils.checkAndStartService(this);
        }

        if (Utils.isServiceRunning(this)) {
            mStartStopServiceBtn.setText(R.string.btn_stop_service);
            mRotationIndicator.startAnimation(getRotateAnimation());
        } else {
            mStartStopServiceBtn.setText(R.string.btn_start_service);
            mRotationIndicator.clearAnimation();
        }
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent(this, FloatingService.class);
        if (v.getId() == R.id.start_stop_button) {
            if (Utils.isServiceRunning(this)) {
                stopService(mIntent);
                mStartStopServiceBtn.setText(R.string.btn_start_service);
                mRotationIndicator.clearAnimation();
            } else {
                startService(mIntent);
                mStartStopServiceBtn.setText(R.string.btn_stop_service);
                mRotationIndicator.startAnimation(getRotateAnimation());
            }
        }
    }

    public Animation getRotateAnimation() {
        Animation mRotateAnimation = new RotateAnimation(0, -359.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setFillAfter(false);
        mRotateAnimation.setFillEnabled(true);
        mRotateAnimation.setDuration(3000);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        return mRotateAnimation;
    }


    private void showDrawOverlayPermissionAskDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(R.string.permission_dialog_title);
        mBuilder.setMessage(R.string.permission_dialog_msg);
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.permission_dialog_btn_proceed, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showDrawOverlayPermissionAskDialog();
                } else {
                    Utils.checkAndStartService(this);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
