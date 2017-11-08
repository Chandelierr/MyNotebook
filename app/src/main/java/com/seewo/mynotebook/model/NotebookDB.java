package com.seewo.mynotebook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.seewo.mynotebook.model.NotebookDBOpenHelper.CONTENT;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_NAME;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.ID;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.TABLE_NAME;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.TIME;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.TITLE_NAME;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 数据库的工具
 */

public class NotebookDB {
    private static final String TAG = "NotebookDB";

    private static final String DB_NAME = TABLE_NAME;
    private static final int VERSION = 1;
    private SQLiteDatabase mDatabase;
    private static NotebookDB mNotebookDb;

    private NotebookDB(Context context) {
        NotebookDBOpenHelper openHelper = new NotebookDBOpenHelper(context, DB_NAME, null, VERSION);
        mDatabase = openHelper.getWritableDatabase();
    }

    public static NotebookDB getInstance(Context context) {
        if (mNotebookDb == null) {
            mNotebookDb = new NotebookDB(context);
        }
        return mNotebookDb;
    }

    //插入
    public void insertNote(Note note) {
        Log.d(TAG, "insert note.");
        if (note != null) {
            ContentValues cv = new ContentValues();
            cv.put(GROUP_NAME, note.getGroup().toString());
            cv.put(TITLE_NAME, note.getTitle());
            cv.put(TIME, note.getTime());
            Log.d("AddNote", note.getContent());
            cv.put(CONTENT, note.getContent());
            mDatabase.insert(TABLE_NAME, null, cv);
        }
    }

    public boolean deleteNote(Note note) {
        int raw = mDatabase.delete(TABLE_NAME, ID + "=?", new String[]{note.getId()+""});
        Log.d(TAG, "raw: " + raw);
        if (raw == 1) {
            return true;
        } else {
            return false;
        }
    }

    //加载
    public List<Note> loadNote(String group) {
        List<Note> list=new ArrayList<Note>();
        Cursor cursor=mDatabase.query(TABLE_NAME, null, GROUP_NAME + "=?",
                new String[] {group}, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                note.setGroup(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
                note.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                list.add(note);
            }while(cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    //更新
    public void updateNote(Note note) {
        if (note != null) {
            ContentValues cv = new ContentValues();
            cv.put(TITLE_NAME, note.getTitle());
            cv.put(CONTENT, note.getContent());
            int isSuccess = mDatabase.update(TABLE_NAME, cv, ID + "=?", new String[]{note.getId()+""});
            Log.d("AddNote", "is success: " + isSuccess);
        }
    }
}
