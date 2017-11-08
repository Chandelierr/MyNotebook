package com.seewo.mynotebook.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module 自定义数据库OpenHelper
 */

public class NotebookDBOpenHelper extends SQLiteOpenHelper{
    private static final String TAG = "NotebookDBOpenHelper";
    public static final String TABLE_NAME = "notebook_db";
    public static final String ID = "id";
    public static final String GROUP_NAME = "group_name";
    public static final String TITLE_NAME = "title_name";
    public static final String TIME = "time";
    public static final String CONTENT = "content";

    public NotebookDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create table.");
        db.execSQL("create table " + TABLE_NAME +
                " (" + ID + " integer primary key autoincrement, " +
                GROUP_NAME + " text, " +
                TITLE_NAME + " text, " +
                CONTENT + " text, " +
                TIME + " text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "update table.");
    }
}
