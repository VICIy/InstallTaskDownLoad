package com.viciy.installtaskdownload.holder;

import android.view.View;

import com.viciy.installtaskdownload.downlaod.DownloadInfo;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;

/**
 * Created by bai-qiang.yang on 2017/2/17.
 */
public abstract class DownloadViewHolder {

    protected DownloadInfo downloadInfo;

    public DownloadViewHolder(View view, DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
        x.view().inject(this, view);
    }

    public final DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void update(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public abstract void onWaiting();

    public abstract void onStarted();

    public abstract void onLoading(long total, long current);

    public abstract void onSuccess(File result);

    public abstract void onError(Throwable ex, boolean isOnCallback);

    public abstract void onCancelled(Callback.CancelledException cex);
}
