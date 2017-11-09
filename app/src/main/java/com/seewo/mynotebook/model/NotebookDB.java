package com.seewo.mynotebook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_CONTENT;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_ID;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_MAP_ID;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_TABLE;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_NOTE_TABLE;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_ID;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_MAP_ID;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_NAME;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.GROUP_TABLE;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_TIME;
import static com.seewo.mynotebook.model.NotebookDBOpenHelper.NOTE_TITLE_NAME;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 数据库的工具
 */

public class NotebookDB {
    private static final String TAG = "NotebookDB";

    private static final String DB_NAME = NOTE_TABLE;
    private static final int VERSION = 1;
    private SQLiteDatabase mDatabase;
    private static NotebookDB mNotebookDb;

    private NotebookDB(Context context) {
        NotebookDBOpenHelper openHelper = new NotebookDBOpenHelper(context, DB_NAME, null, VERSION);
        mDatabase = openHelper.getWritableDatabase();

        Group defaultGroup = new Group("未分组");
        insertGroup(defaultGroup);
    }

    public static NotebookDB getInstance(Context context) {
        if (mNotebookDb == null) {
            mNotebookDb = new NotebookDB(context);
        }
        return mNotebookDb;
    }

    /**
     * 插入note ok
     *
     * @param note
     */
    public void insertNote(String group, Note note) {
        Log.d(TAG, "insert note.");
        if (note != null) {
            //添加记事
            ContentValues cv = new ContentValues();
            //cv.put(NOTE_GROUP_NAME, note.getGroup());
            cv.put(NOTE_TITLE_NAME, note.getTitle());
            cv.put(NOTE_TIME, note.getTime());
            cv.put(NOTE_CONTENT, note.getContent());
            mDatabase.insert(NOTE_TABLE, null, cv);

            //添加映射
            ContentValues cv1 = new ContentValues();
            cv1.put(GROUP_MAP_ID, queryGroupIdByGroupName(group));
            cv1.put(NOTE_MAP_ID, queryNoteIdByNote(note.getTitle(), note.getTime()));
            mDatabase.insert(GROUP_NOTE_TABLE, null, cv1);
        }
    }

    /**
     * 插入group ok
     *
     * @param group
     */
    public boolean insertGroup(Group group) {
        Log.d(TAG, "insert a group.");
        boolean isSuccess = false;
        if (group != null) {
            ContentValues cv = new ContentValues();
            cv.put(GROUP_NAME, group.getName());
            return mDatabase.insert(GROUP_TABLE, null, cv) != -1;
        }
        return isSuccess;
    }

    /**
     * 删除note  ok
     * @param note
     * @return
     */
    public boolean deleteNote(Note note) {
        int rawInMap = mDatabase.delete(GROUP_NOTE_TABLE, NOTE_MAP_ID + "=?", new String[]{note.getId() + ""});
        Log.d(TAG, "rawInMap: " + rawInMap);
        int rawInNote = mDatabase.delete(NOTE_TABLE, NOTE_ID + "=?", new String[]{note.getId() + ""});
        //decreaseCountInGroup(note.getGroup(), 1);
        Log.d(TAG, "rawInNote: " + rawInNote);
        return  rawInMap == 1 && rawInNote == 1;
    }


    /**
     * 删除group
     * @param group
     */
    public void deleteGroup(Group group) {
        //GROUP_TABLE中删除
        int rawInGroup = mDatabase.delete(GROUP_TABLE, GROUP_ID + "=?", new String[]{group.getId() + ""});

        //GROUP_NOTE_TABLE中更新GROUP_ID为未分组的
        ContentValues cv = new ContentValues();
        cv.put(GROUP_MAP_ID, queryGroupIdByGroupName("未分组"));
        int isSuccess = mDatabase.update(GROUP_NOTE_TABLE, cv, GROUP_MAP_ID + "=?", new String[]{group.getId() + ""});
        Log.d(TAG, "(delete group) update map_table id success: " + isSuccess);
    }

//    private void decreaseCountInGroup(String group, int count) {
//        mDatabase.execSQL("update " + GROUP_TABLE + " set " +
//                GROUP_CONTAINS_COUNT + "=" + GROUP_CONTAINS_COUNT + "-" + count +
//                " where " + GROUP_NAME + "=" + group);
//    }

    /**
     * 加载note  ok
     *
     * @param group 根据组名
     * @return 返回一个note集合
     */
    public List<Note> loadNote(Group group) {
        //获取这个组的所有note
        Log.d(TAG, "load notes.");
        List<Note> listNote = new ArrayList<Note>();

        Cursor cursorInMap = mDatabase.query(GROUP_NOTE_TABLE, null, GROUP_MAP_ID + "=?",
                new String[]{group.getId()+""}, null, null, null);
        if (cursorInMap != null && cursorInMap.moveToFirst()) {
            do {
                int id = cursorInMap.getInt(cursorInMap.getColumnIndex(NOTE_MAP_ID));
                Log.d(TAG, "id:" + id);
                Cursor cursor = mDatabase.query(NOTE_TABLE, null, NOTE_ID + "=?",
                        new String[]{id+""}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Note note = new Note();
                        note.setId(cursor.getInt(cursor.getColumnIndex(NOTE_ID)));
                        note.setTime(cursor.getString(cursor.getColumnIndex(NOTE_TIME)));
                        note.setTitle(cursor.getString(cursor.getColumnIndex(NOTE_TITLE_NAME)));
                        note.setContent(cursor.getString(cursor.getColumnIndex(NOTE_CONTENT)));
                        Log.d(TAG, "note: " + note.getTitle());
                        listNote.add(note);
                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            } while (cursorInMap.moveToNext());
        }
        if (cursorInMap != null) {
            cursorInMap.close();
        }

        return listNote;
    }

    /**
     * 加载所有group  ok
     * @return
     */
    public List<Group> loadGroup() {
        List<Group> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(GROUP_TABLE, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setId(cursor.getInt(cursor.getColumnIndex(GROUP_ID)));
                group.setName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
                Log.d(TAG, "load group |||| group name: " + group.getName());
                list.add(group);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 更新note  ok
     *
     * @param note
     */
    public void updateNote(Note note) {
        if (note != null) {
            ContentValues cv = new ContentValues();
            cv.put(NOTE_TITLE_NAME, note.getTitle());
            cv.put(NOTE_CONTENT, note.getContent());
            int isSuccess = mDatabase.update(NOTE_TABLE, cv, NOTE_ID + "=?", new String[]{note.getId() + ""});
            Log.d("AddNote", "is success: " + isSuccess);
        }
    }

    /**
     * 更新group
     *
     * @param group
     */
    public void updateGroup(Group group) {
        if (group != null) {
            ContentValues cv = new ContentValues();
            cv.put(GROUP_NAME, group.getName());
            //cv.put(GROUP_CONTAINS_COUNT, group.getCount());
            int isSuccess = mDatabase.update(GROUP_TABLE, cv, GROUP_ID + "=?", new String[]{group.getId() + ""});
            Log.d(TAG, "update group is success: " + isSuccess);
        }
    }

    /**
     * 通过group name获取id  ok
     * @param group
     * @return
     */
    private int queryGroupIdByGroupName(String group) {
        Log.d(TAG, "queryGroupIdByGroupName: group: " + group);
        int id = -1;
        Cursor cursor = mDatabase.query(GROUP_TABLE, null, GROUP_NAME + "=?",
                new String[]{group}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(GROUP_ID));
                Log.d(TAG, "group id: " + id);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "queryGroupIdByGroupName: id: " + id);
        return id;
    }

    /**
     * 通过note的title和time获取id  ok
     * @param title
     * @param time
     * @return
     */
    private int queryNoteIdByNote(String title, String time) {
        Log.d(TAG, "queryNoteIdByNoteName: note: " + time);
        int id = -1;
        Cursor cursor = mDatabase.query(NOTE_TABLE, null, NOTE_TITLE_NAME + "=? and " + NOTE_TIME + "=?",
                new String[]{title, time}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(NOTE_ID));
                Log.d(TAG, "note id: " + id);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "queryNoteIdByNoteName: id: " + id);
        return id;
    }

    /**
     * 获取默认分组 ok
     * @return
     */
    public Group loadDefaultGroup() {
        Group group = new Group();
        Cursor cursor = mDatabase.query(GROUP_TABLE, null, GROUP_NAME + "=?",
                new String[]{"未分组"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                group.setId(cursor.getInt(cursor.getColumnIndex(GROUP_ID)));
                group.setName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
                Log.d(TAG, "group name: " + group.getName());
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return group;
    }

    /**
     * 根据id获取Group  ok
     * @param id
     * @return
     */
    public Group loadGroupById(int id) {
        Group group = new Group();
        Cursor cursor = mDatabase.query(GROUP_TABLE, null, GROUP_ID + "=?",
                new String[]{id + ""}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                group.setId(id);
                group.setName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
                Log.d(TAG, "group name: " + group.getName());
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return group;
    }

    /**
     * 通过id加载Note  ok
     * @param id
     * @return
     */
    public Note loadNoteById(int id) {
        Note note = new Note();
        Cursor cursor = mDatabase.query(NOTE_TABLE, null, NOTE_ID + "=?",
                new String[]{id + ""}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                note.setId(id);
                note.setTime(cursor.getString(cursor.getColumnIndex(NOTE_TIME)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NOTE_TITLE_NAME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NOTE_CONTENT)));
                Log.d(TAG, "note name: " + note.getTitle());
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return note;
    }

    /**
     * 通过title模糊查询Note
     * @param query
     * @return
     */
    public List<Note> fuzzyQueryNote(int groupId, String query) {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = mDatabase.query(NOTE_TABLE, null, NOTE_TITLE_NAME + " like ?",
                new String[]{query+"%"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(NOTE_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NOTE_TITLE_NAME)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NOTE_TIME)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NOTE_CONTENT)));
                Log.d(TAG, "note: " + note.getTitle());
                notes.add(note);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        Cursor cursorNoteIdByGroupId = mDatabase.query(GROUP_NOTE_TABLE, null, GROUP_MAP_ID + "!=?",
                new String[]{groupId + ""}, null, null, null);
        if (cursorNoteIdByGroupId != null && cursorNoteIdByGroupId.moveToFirst()) {
            do {
                int id = cursorNoteIdByGroupId.getInt(cursorNoteIdByGroupId.getColumnIndex(NOTE_MAP_ID));
                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getId() == id) {
                        notes.remove(i);
                    }
                }
            } while (cursorNoteIdByGroupId.moveToNext());
        }
        if (cursorNoteIdByGroupId != null) {
            cursorNoteIdByGroupId.close();
        }
        return notes;
    }
}
