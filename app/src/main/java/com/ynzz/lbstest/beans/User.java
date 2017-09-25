package com.ynzz.lbstest.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TextInputEditText;

import com.ynzz.lbstest.UserDbAdapter;

/**
 * Created by Administrator on 2016/5/13.
 */
public class User {
    //一些 字段
    private String User_Tel = "tel";
    private String User_Password="password";
    private String User_NickName="username";
    public static final String User_ROWID = "_id";
    private Boolean User_Sex;
    private String User_Birthday="birthday";
    public User(String User_Tel, String User_Password,
                String User_NickName, Boolean User_Sex, String User_Birthday) {

        this.User_Tel = User_Tel;
        this.User_Password = User_Password;
        this.User_NickName = User_NickName;
        this.User_Sex = User_Sex;
        this.User_Birthday = User_Birthday;
    }
    private User.DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table user (_id integer primary key autoincrement, "
            + "username text not null, password text not null);";

    private static final String DATABASE_NAME = "database2";
    private static final String DATABASE_TABLE = "user2";
    private static final int DATABASE_VERSION = 1;

    private Context mCtx11;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS user");
            onCreate(db);
        }
    }

    public User(Context ctx) {
        this.mCtx11 = ctx;
    }

    public User open() throws SQLException {
        mDbHelper = new User.DatabaseHelper(mCtx11);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void closeclose() {
        mDbHelper.close();
    }

    public long createUser(String username, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(User_NickName, username);
        initialValues.put(User_Password, password);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] { User_ROWID, User_NickName,
                User_Password }, null, null, null, null, null);
    }

    public Cursor getDiary(String username) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { User_ROWID, User_NickName,
                                User_Password }, User_NickName + "='" + username+"'", null, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    //get
    public String getUser_Tel() {
        return this.User_Tel;
    }

    public String getUser_Password() {
        return this.User_Password;
    }

    public String getUser_NickName() {
        return this.User_NickName;
    }

    public String getUser_Birthday() {
        return this.User_Birthday;
    }

    public Boolean getUser_Sex() {
        return this.User_Sex;
    }

    //set
    public void setUser_Password(String User_Password) {
        this.User_Password = User_Password;
    }

    public void setUser_NickName(String User_NickName) {
        this.User_NickName = User_NickName;
    }

    public void setUser_Sex(Boolean User_Sex) {
        this.User_Sex = User_Sex;
    }

    public void setUser_Birthday(String User_Birthday) {
        this.User_Birthday = User_Birthday;
    }
}
