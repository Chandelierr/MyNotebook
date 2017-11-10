package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.adapter.NoteAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module MainActivity的presenter
 */

public class MainPresenter {
    private static final String TAG = "MainPresenter";

    private IView mView;
    private NoteAdapter mAdapter;

    public List<Note> mNotes;
    public List<Note> mStoreNotes;
    public List<Group> mGroups;

    public MainPresenter(IView view) {
        mView = view;
    }

    //delete=====================================================================================

    public boolean delete(int position) {
        return NotebookDB.getInstance(mView.getAppContext()).deleteNote(mNotes.get(position));
    }

    //load======================================================================================

    public Group loadDefaultGroup() {
        return NotebookDB.getInstance(mView.getAppContext()).loadDefaultGroup();
    }

    public List<Group> loadAllGroup() {
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

    public void loadNotesByGroup(Group group) {
        if (mNotes == null) {
            mNotes = new ArrayList<>();
        }
        List<Note> data = NotebookDB.getInstance(mView.getAppContext()).loadNote(group);
        if (data == null) {
            Log.d(TAG, "data == null ");
        } else {
            mNotes.clear();
            mNotes.addAll(data);
        }
        prepareAdapter();
    }

    private void prepareAdapter() {
        if (mAdapter == null) {
            mAdapter = new NoteAdapter(mView.getAppContext(), mNotes);
            mView.setAdapter(mAdapter);
        } else {
            //TODO 尝试其他方法，提高效率
            mAdapter.notifyDataSetChanged();
        }
    }

    public Note loadNoteByIndex(int position) {
        return mNotes.get(position);
    }

    public void loadStoreNotes() {
        if (mStoreNotes != null) {
            mNotes.clear();
            mNotes.addAll(mStoreNotes);
            //TODO 尝试其他方法，提高效率
            mAdapter.notifyDataSetChanged();
        }
    }

    public void queryByTitle(int groupId, String query) {
        List<Note> notes = new ArrayList<>();
        //notes = NotebookDB.getInstance(mView.getAppContext()).fuzzyQueryNote(groupId, query);

        for (Note note : mNotes) {
            if (note.getTitle().contains(query)) {
                notes.add(note);
            }
        }
        mStoreNotes = null;
        mStoreNotes = new ArrayList<>(mNotes);
        mNotes.clear();
        mNotes.addAll(notes);
        //TODO 尝试其他方法，提高效率
        mAdapter.notifyDataSetChanged();
    }
}
