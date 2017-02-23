package com.viciy.installtaskdownload.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.viciy.installtaskdownload.R;
import com.viciy.installtaskdownload.downlaod.DownloadManager;

import org.xutils.ex.DbException;

import static android.R.attr.button;

/**
 * Created by bai-qiang.yang on 2017/2/17.
 */

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btStartThird = (Button) findViewById(R.id.bt_start_third);
        btStartThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}
