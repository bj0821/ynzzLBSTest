package com.ynzz.carmanager.ui.dao;

import android.content.ContentValues;

import com.ynzz.carmanager.ui.bean.CarManagerItem;

import java.util.List;
import java.util.Map;



public interface CarInfoDaoInface {
    public boolean addCache(CarManagerItem item);

    public boolean deleteCache(String whereClause, String[] whereArgs);

    public boolean queryCache(String whereClause, String[] whereArgs);

    public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs);

    public Map<String, String> viewCache(String selection,
                                         String[] selectionArgs);

    public List<Map<String, String>> listCache(String selection,
                                               String[] selectionArgs);

    public void clearFeedTable();

    public int queryCount(String selection, String[] selectionArgs);

}
