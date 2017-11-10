package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 添加记事的presenter
 */

public class AddNotePresenter {
    private static final String TAG = "AddNotePresenter";
    private IView mView;

    public AddNotePresenter(IView view) {
        mView = view;
    }

    public boolean insert(String groupName, String title, String content) {
        Log.d(TAG, "insert a note.");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        Log.d(TAG, "time is " + time);

        if (title.equals("")) {
            title = mView.getAppContext().getResources().getString(R.string.new_note);
        }
        Note note = new Note(title, time, content);
        NotebookDB.getInstance(mView.getAppContext()).insertNote(groupName, note);
        return true;
    }

    public boolean update(Note note) {
        Log.d(TAG, "update a note.");
        NotebookDB.getInstance(mView.getAppContext()).updateNote(note);
        return true;
    }

    public List<Group> loadGroups() {
        return NotebookDB.getInstance(mView.getAppContext()).loadGroup();
    }

    public Group loadCurGroup(int id) {
        return NotebookDB.getInstance(mView.getAppContext()).loadGroupById(id);
    }

    public Note loadCurNote(int noteId) {
        return NotebookDB.getInstance(mView.getAppContext()).loadNoteById(noteId);
    }
}