package com.viciy.installtaskdownload.downlaod;

import android.database.Cursor;

import org.xutils.db.converter.ColumnConverter;
import org.xutils.db.sqlite.ColumnDbType;

/**
 * Created by bai-qiang.yang on 2017/2/17.
 */
public class DownloadStateConverter implements ColumnConverter<DownloadState> {

    @Override
    public DownloadState getFieldValue(Cursor cursor, int index) {
        int dbValue = cursor.getInt(index);
        return DownloadState.valueOf(dbValue);
    }

    @Override
    public Object fieldValue2DbValue(DownloadState fieldValue) {
        return fieldValue.value();
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
