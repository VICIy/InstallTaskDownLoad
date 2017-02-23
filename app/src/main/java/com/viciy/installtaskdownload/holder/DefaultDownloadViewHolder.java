package com.viciy.installtaskdownload.holder;

import android.view.View;
import android.widget.Toast;

import com.viciy.installtaskdownload.downlaod.DownloadInfo;
import com.viciy.installtaskdownload.holder.DownloadViewHolder;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;

/**
 * Created by bai-qiang.yang on 2017/2/17.
 */
public class DefaultDownloadViewHolder extends DownloadViewHolder {

    public DefaultDownloadViewHolder(View view, DownloadInfo downloadInfo) {
        super(view, downloadInfo);
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current) {

    }

    @Override
    public void onSuccess(File result) {
        Toast.makeText(x.app(), "下载完成", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Toast.makeText(x.app(), "下载失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {
    }
}

