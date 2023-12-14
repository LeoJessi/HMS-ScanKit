package top.jessi.scan.utils;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

import top.jessi.scan.HMSScanActivity;

/**
 * Created by Bert on 2017/9/22.
 */

public class ScanManager {

    private static ScanManager instance;

    public OnScanResultCallback resultCallback;

    public synchronized static ScanManager getInstance() {
        if (instance == null)
            instance = new ScanManager();
        return instance;
    }

    public OnScanResultCallback getResultCallback() {
        return resultCallback;
    }

    public void startScan(final Activity activity, OnScanResultCallback resultCall) {

        PermissionUtils.permission(activity, PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        Intent intent = new Intent(activity, HMSScanActivity.class);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        resultCall.onFailure("Permission denied.");

                    }
                }).request();


        // 绑定图片接口回调函数事件
        resultCallback = resultCall;
    }


    public static abstract class OnScanResultCallback {
        public void onSuccess(String result){

        }

        public void onFailure(String result){

        }
    }
}
