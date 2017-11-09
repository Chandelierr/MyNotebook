package com.seewo.mynotebook.model;

import android.content.ContentValues;
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
    public static final String NOTE_TABLE = "note_table";
    public static final String NOTE_ID = "note_id";
    //public static final String NOTE_GROUP_NAME = "note_group_name";
    public static final String NOTE_TITLE_NAME = "title_name";
    public static final String NOTE_TIME = "time";
    public static final String NOTE_CONTENT = "content";

    public static final String GROUP_TABLE = "group_table";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    //public static final String GROUP_CONTAINS_COUNT = "contains_count";

    public static final String GROUP_NOTE_TABLE = "group_note_table";
    public static final String GROUP_NOTE_ID = "group_note_id";
    public static final String GROUP_MAP_ID = "group_map_id";
    public static final String NOTE_MAP_ID = "note_map_id";

    public NotebookDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create table.");
        db.execSQL("create table " + NOTE_TABLE +
                " (" + NOTE_ID + " integer primary key autoincrement, " +
                //NOTE_GROUP_NAME + " text, " +
                NOTE_TITLE_NAME + " text, " +
                NOTE_CONTENT + " text, " +
                NOTE_TIME + " text not null)");
        db.execSQL("create table " + GROUP_TABLE +
                " (" + GROUP_ID + " integer primary key autoincrement, " +
                GROUP_NAME + " text unique)");
//                GROUP_NAME + " text, " +
//                GROUP_CONTAINS_COUNT + " integer)");
        db.execSQL("create table " + GROUP_NOTE_TABLE +
                " (" + GROUP_NOTE_ID + " integer primary key autoincrement, " +
                GROUP_MAP_ID + " integer, " +
                NOTE_MAP_ID + " integer, " +
                "foreign key(" + GROUP_MAP_ID + ") references " + GROUP_TABLE + "(" + GROUP_ID + "), " +
                "foreign key(" + NOTE_MAP_ID + ") references " + NOTE_TABLE + "(" + NOTE_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "update table.");
    }
}
