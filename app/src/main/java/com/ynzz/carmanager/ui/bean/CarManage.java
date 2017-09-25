package com.ynzz.carmanager.ui.bean;

import android.content.ContentValues;
import android.database.SQLException;
import android.util.Log;

import com.ynzz.carmanager.ui.dao.CarInfoDao;
import com.ynzz.carmanager.ui.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CarManage {
    public static CarManage channelManage;
    /**
     * 默认的用户选择频道列表
     */
    public static List<CarManagerItem> defaultUserChannels;

    static {
        defaultUserChannels = new ArrayList<CarManagerItem>();
    }

    private CarInfoDao channelDao;
    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    private CarManage(SQLHelper paramDBHelper) throws SQLException {
        if (channelDao == null)
            channelDao = new CarInfoDao(paramDBHelper.getContext());
        return;
    }

    /**
     * 初始化频道管理类
     *
     * @param
     * @throws SQLException
     */
    public static CarManage getManage(SQLHelper dbHelper) throws SQLException {
        if (channelManage == null)
            channelManage = new CarManage(dbHelper);
        return channelManage;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        channelDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<CarManagerItem> getUserChannel() {
        List<CarManagerItem> lists = new ArrayList<CarManagerItem>();
        Object cacheList = channelDao.listCache(null, null);
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<CarManagerItem> list = new ArrayList<CarManagerItem>();
            for (int i = 0; i < count; i++) {
                CarManagerItem navigate = new CarManagerItem();
                navigate.setPkid(maplist.get(i).get(SQLHelper.ID));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setPass(maplist.get(i).get(SQLHelper.SELECTED));
                list.add(navigate);
            }
            return list;
        }
        return lists;
    }

    /**
     * 获取更多的数据
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<CarManagerItem> QueryCarByName(String name) {
        List<CarManagerItem> lists = new ArrayList<CarManagerItem>();
        Object cacheList = channelDao.listCache(SQLHelper.NAME + "= ?", new String[]{name});
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<CarManagerItem> list = new ArrayList<CarManagerItem>();
            for (int i = 0; i < count; i++) {
                CarManagerItem navigate = new CarManagerItem();
                navigate.setPkid(maplist.get(i).get(SQLHelper.ID));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setPass(maplist.get(i).get(SQLHelper.SELECTED));
                list.add(navigate);
            }
            return list;
        }
        return lists;
    }

    /**
     * 保存用户频道到数据库
     *
     * @param channelItem
     */
    public void saveUserChannel(CarManagerItem channelItem) {
        channelDao.addCache(channelItem);
    }

    public boolean isInfoExists(String name) {
        boolean flag = false;
        if (name == null && name.equals("")) {
            return true;
        }
        flag = channelDao.queryCache(SQLHelper.NAME + "= ?", new String[]{name});
        return flag;
    }

    public int getCount(String time, String userid) {
        return channelDao.queryCount(SQLHelper.ID + "= ?",new String[]{time, userid});
    }

    public void updateChannel(String id,String name,String state){
        ContentValues values = new ContentValues();
        values.put(SQLHelper.NAME, name);
        values.put(SQLHelper.SELECTED, state);
        channelDao.updateCache(values,SQLHelper.ID + "= ?", new String[]{id});
    }

    public void deleteChannelById(String id){
        channelDao.deleteCache(SQLHelper.ID + "= ?", new String[]{id});
    }

    /**
     * 初始化数据库内的数据
     */
    public void initDefaultChannel() {
        Log.d("deleteAll", "deleteAll");
    }
}
