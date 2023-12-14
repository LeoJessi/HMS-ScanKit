/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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
package top.jessi.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;

import top.jessi.scan.utils.Functions;
import top.jessi.scan.utils.ScanManager;

public class HMSScanActivity extends Activity {
    private FrameLayout frameLayout;
    private RemoteView remoteView;
    private ImageView scan_area;
    int mScreenWidth;
    int mScreenHeight;
    //The width and height of scan_view_finder is both 240 dp.
    final int SCAN_FRAME_SIZE = 268;

    private static final String TAG = "DefinedActivity";

    //Declare the key. It is used to obtain the value returned from Scan Kit.
    public static final String SCAN_RESULT = "scanResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hms_scan);
        Functions.hideNavigationBar(getWindow().getDecorView());
        initView();


        scan_area = findViewById(R.id.scan_area);
        frameLayout = findViewById(R.id.rim);

        //1. Obtain the screen density to calculate the viewfinder's rectangle.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        //2. Obtain the screen size.


        mScreenWidth = Functions.getScreenWidth(this);
        mScreenHeight = Functions.getScreenHeight(this);


        // int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        int scanFrameSize = scan_area.getLayoutParams().width;

        //3. Calculate the viewfinder's rectangle, which in the middle of the layout.
        //Set the scanning area. (Optional. Rect can be null. If no settings are specified, it will be located in the
        // middle of the layout.)
        Rect rect = new Rect();
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                && Functions.isPortraitScreen(this)) {
            rect.left = mScreenWidth / 2;
            rect.right = mScreenWidth / 2 + scanFrameSize / 2;
            rect.top = (int) (mScreenHeight / 2 + (scanFrameSize / 2.5));
            rect.bottom = (int) (mScreenHeight / 2 + scanFrameSize / 2.5 * 2);
        } else {
            rect.left = mScreenWidth / 2 - scanFrameSize / 2;
            rect.right = mScreenWidth / 2 + scanFrameSize / 2;
            rect.top = mScreenHeight / 2 - scanFrameSize / 5;
            rect.bottom = (int) (mScreenHeight / 2 + scanFrameSize / 3 * 1.85);
        }

        //Initialize the RemoteView instance, and set callback for the scanning result.
        remoteView = new RemoteView.Builder()
                .setContext(this)
                .setBoundingBox(rect)
                .setFormat(HmsScan.QRCODE_SCAN_TYPE)
                .build();
        // Subscribe to the scanning result callback event.
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                //Check the result.
                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                    Intent intent = new Intent();
                    intent.putExtra(SCAN_RESULT, result[0]);
                    setResult(RESULT_OK, intent);

                    ScanManager.getInstance().getResultCallback().onSuccess(result[0].showResult);
                    HMSScanActivity.this.finish();
                }
            }
        });
        // Load the customized view to the activity.
        remoteView.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                && Functions.isPortraitScreen(this)) {
            /*为何在平板上会出现屏幕横向  但是旋转角度是0的问题?*/
            remoteView.setRotation(270);
            params.height = mScreenWidth;
            params.width = mScreenWidth;
        }
        frameLayout.addView(remoteView, params);
    }

    private void initView() {
        Button back = findViewById(R.id.scan_btn_back);
        Drawable backDrawable = AppCompatResources.getDrawable(this, R.drawable.arror);
        if (backDrawable != null) {
            backDrawable.setBounds(0, 0, pt2px(this, 20.1f),
                    pt2px(this, 35.8f));
            back.setCompoundDrawables(backDrawable, null, null, null);
        }
        back.setOnClickListener(v -> finish());
    }

    private int pt2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value,
                context.getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
    }
}
