package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.adapter.NoteAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module MainActivity的presenter
 */

public class MainPresenter {
    private static final String TAG = "MainPresenter";

    private IMainView mView;
    private NoteAdapter mAdapter;

    public List<Note> mNotes;
    public List<Group> mGroups;

    public MainPresenter(IMainView view) {
        mView = view;
    }

    public void getNotes(Group group) {
        if (mNotes == null) {
            mNotes = new ArrayList<>();
        }
        mNotes.clear();
        List<Note> data = NotebookDB.getInstance(mView.getAppContext()).loadNote(group);
        if (data == null) {
            Log.d(TAG, "data == null ");
        } else {
            mNotes.addAll(data);
        }
        for (Note note: mNotes) {
            Log.d(TAG, "id: " + note.getId() + "\n" +
                    "title name: " + note.getTitle() + "\n");
        }
        prepareAdapter();
    }

    private void prepareAdapter() {
        if (mAdapter == null) {
            mAdapter = new NoteAdapter(mView.getAppContext(), mNotes);
            mView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public Note getNote(int position) {
        return mNotes.get(position);
    }

    public boolean delete(int position) {
        return NotebookDB.getInstance(mView.getAppContext()).deleteNote(mNotes.get(position));
    }

    public Group loadDefaultGroup() {
        return NotebookDB.getInstance(mView.getAppContext()).loadDefaultGroup();
    }

    public void queryByTitle(int groupId, String query) {
        List<Note> notes = new ArrayList<>();
        notes = NotebookDB.getInstance(mView.getAppContext()).fuzzyQueryNote(groupId, query);
        mNotes.clear();
        mNotes.addAll(notes);
        mAdapter.notifyDataSetChanged();
    }

    public List<Group> loadGroups() {
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        mGroups.clear();
        mGroups.addAll(NotebookDB.getInstance(mView.getAppContext()).loadGroup());
        for (Group group : mGroups) {
            Log.d(TAG, "group id: " + group.getId() + "\n group name: " + group.getName());
        }
        return mGroups;
    }
}
