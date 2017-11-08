package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IAddNoteView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 添加记事的presenter
 */

public class AddNotePresenter {
    private static final String TAG = "AddNotePresenter";
    private IAddNoteView mView;

    public AddNotePresenter(IAddNoteView view) {
        mView = view;
    }

    public void insert(String groupName, String title, String content) {
        Log.d(TAG, "insert a note.");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        Log.d(TAG, "time is " + time);

        if (title.equals("")) {
            title = mView.getAppContext().getResources().getString(R.string.new_note);
        }
        Note note = new Note(groupName, title, time, content);
        NotebookDB.getInstance(mView.getAppContext()).insertNote(note);
        mView.backToMain();
    }

    public void update(Note note) {
        Log.d(TAG, "update a note.");
        NotebookDB.getInstance(mView.getAppContext()).updateNote(note);
        mView.backToMain();
    }
}