package com.viciy.installtaskdownload.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viciy.installtaskdownload.R;
import com.viciy.installtaskdownload.downlaod.DownloadInfo;
import com.viciy.installtaskdownload.downlaod.DownloadManager;
import com.viciy.installtaskdownload.downlaod.DownloadState;
import com.viciy.installtaskdownload.holder.DownloadViewHolder;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import java.io.File;


/**
 * Created by bai-qiang.yang on 2017/2/17.
 */

public class ThirdActivity extends Activity {

    private DownloadManager mManager;
    private DownloadInfo mDownloadInfo;

    private final String url = "http://cdn1.utouu.com/apps/apk/com.utouu.4020.apk";
    private final String label = "viciy";
    private final String path = Environment.getExternalStorageDirectory()+"/com.viciy.installtaskdownload/files/";
    private String realPath = path + label + ".apk";
    private InstallTaskDownloadHolder mHolder;
    private Button mBtDownload;
    private int mProgress;
    private BroadcastReceiver myNetReceiver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mBtDownload = (Button) findViewById(R.id.bt_third);

        System.out.println("onCreate---->");

        /**注册网络监听，用于断网情况下下载续传*/
        registerNetworkReceiver();
        /**初始化下载信息*/
        downloadInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**初始化下载按钮的显示内容*/
        initBtDownloadText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myNetReceiver);
    }

    private void registerNetworkReceiver() {

        /**断网后再恢复网络情况下，续传*/
        myNetReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                    if (getNetWork()) {
                        DownloadInfo downloadInfo = mManager.getDownloadInfo(label, realPath);
                        if (downloadInfo!= null && downloadInfo.getState() == DownloadState.ERROR){
                            startDownload();
                        }
                        System.out.println("有网---->");
                    } else {
                        Toast.makeText(ThirdActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        } ;

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

    private boolean getNetWork() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()) {
            return true;
        }
        return false;
    }


    private void downloadInit() {
        mManager = DownloadManager.getInstance();
        if (mDownloadInfo != null) {
            mDownloadInfo = mManager.getDownloadInfo(label,realPath);
        } else {
            /**创建下载信息*/
            mDownloadInfo = new DownloadInfo();
            mDownloadInfo.setUrl(url);
            mDownloadInfo.setLabel(label);
            mDownloadInfo.setFileSavePath(realPath);
            mDownloadInfo.setState(DownloadState.STOPPED);
            mDownloadInfo.setAutoResume(true);
            mDownloadInfo.setAutoRename(false);
            /**在数据库中更新下载信息*/
            try {
                mManager.updateDownloadInfo(mDownloadInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        /**创建Holder,对下载状态进行回调*/
        mHolder = new InstallTaskDownloadHolder(mBtDownload, mDownloadInfo);

    }

    private void initBtDownloadText() {
        DownloadInfo downloadInfo =  mManager.getDownloadInfo(label, realPath);

        if (downloadInfo != null) {
            mProgress = downloadInfo.getProgress();
        } else {
            mProgress = 0;
        }

        if (mProgress == 0) {
            mBtDownload.setText("开始下载");
        } else if (mProgress > 0 && mProgress < 100) {
            mBtDownload.setText("下载" + mProgress + "%");
        } else {
            mBtDownload.setText("立即安装");
        }

        /**杀死进程后，再次进入界面下载继续*/
        if (downloadInfo != null && mProgress > 0 && mProgress < 100) {
            startDownload();
        }
    }

    private void startDownload() {

        if (getNetWork()) {
            try {
                mManager.startDownload(url, label, path + label + ".apk", true, false, mHolder);
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "没有网络", Toast.LENGTH_SHORT).show();
        }
    }


    private class InstallTaskDownloadHolder extends DownloadViewHolder implements View.OnClickListener {

        public InstallTaskDownloadHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
            view.setOnClickListener(this);
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
            installApk();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            refresh();
            System.out.println("error_msg---->"+ex.getMessage());
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_third:
                    if (getNetWork()) {
                        switchDownload();
                    } else {
                        Toast.makeText(ThirdActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        public void refresh() {
            System.out.println("state---->"+downloadInfo.getState()+"-pro---->"+downloadInfo.getProgress());
            if (downloadInfo.getProgress() == 100) {//安装前，删除安装包，重新下载的情况
                mBtDownload.setText("下载0%");
                return;
            }
            mBtDownload.setText("下载"+String.valueOf(downloadInfo.getProgress()+"%"));
        }

        private void installApk() {
            String s = path+label+".apk";
            File file = new File(s);
            if (file.exists()) {//安装下载的apk
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + s), "application/vnd.android.package-archive");
                startActivity(intent);
            } else {//用户安装前，删除掉apk的情况
                downloadInfo.setProgress(0);
                downloadInfo.setState(DownloadState.FINISHED);
                mBtDownload.setText("重新下载");
                try {
                    mManager.updateDownloadInfo(downloadInfo);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                DownloadState state = downloadInfo.getState();
                System.out.println("download-state---->"+state);

                Toast.makeText(ThirdActivity.this, "安装包已删除", Toast.LENGTH_SHORT).show();
            }


        }

        private void switchDownload() {
                DownloadState state = downloadInfo.getState();
                System.out.println("click----->"+state);
                switch (state) {
                    case ERROR:
                    case STOPPED:

                        if (mProgress == 100) {
                            installApk();
                        } else {
                            startDownload();
                        }
                        break;
                    case FINISHED:

                        if (mBtDownload.getText().equals("重新下载")) {
                            startDownload();
                        } else {
                            installApk();
                        }
                        break;
                    default:
                        break;
                }
        }
    }

}

