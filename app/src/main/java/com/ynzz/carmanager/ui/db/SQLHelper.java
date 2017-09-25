package com.ynzz.carmanager.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "mycardatabase.db";// 数据库名称
    public static final int VERSION = 1;//版本1

    public static final String TABLE_CAR = "car";// 车辆信息数据表

    // 信息栏目频道
    public static final String ID = "id";//
    public static final String NAME = "name";
    public static final String SELECTED = "selected";


    private Context context;

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        String sql = "create table if not exists " + TABLE_CAR
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
                + " INTEGER , " + NAME + " TEXT , " + SELECTED + " TEXT);";
        db.execSQL(sql);
    }

    public void clearTable(SQLiteDatabase db) {
        String sql1 = "delete from " + TABLE_CAR;
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        clearTable(db);
        onCreate(db);
    }

}
