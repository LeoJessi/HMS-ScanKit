package top.jessi.scankitdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;


import top.jessi.scan.utils.ScanManager;

/**
 * Created by Jessi on 2023/12/12 20:06
 * Email：17324719944@189.cn
 * Describe：
 */
public class DemoActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Button button = findViewById(R.id.scan);
        button.setOnClickListener(v -> {
            ScanManager.getInstance().startScan(this, new ScanManager.OnScanResultCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("TAG", "onSuccess: ~~~~~~~~~~" + result );
                }
            });
        });

        Button main = findViewById(R.id.main);
        main.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}
