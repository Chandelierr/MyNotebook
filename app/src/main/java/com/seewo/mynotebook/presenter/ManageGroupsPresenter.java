package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.adapter.GroupAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IManageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/9.
 */

public class ManageGroupsPresenter {
    private static final String TAG = "ManageGroupsPresenter";

    private IManageView mView;

    private List<Group> mGroups;
    private GroupAdapter mAdapter;
    
    public ManageGroupsPresenter(IManageView view) {
        mView = view;
    }

    public List<Group> loadGroups() {
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        mGroups.clear();
        mGroups.addAll(NotebookDB.getInstance(mView.getAppContext()).loadGroup());
        prepareAdapter();
        return mGroups;
    }

    private void prepareAdapter() {
        if (mAdapter == null) {
            mAdapter = new GroupAdapter(mView.getAppContext(), mGroups);
            mView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean insertGroup(String groupName) {
        Group group = new Group(groupName);
        return NotebookDB.getInstance(mView.getAppContext()).insertGroup(group);
    }

    public void deleteGroup(int position) {
        Group group = mGroups.get(position);
        Log.d(TAG, "delete group:" + group.getName());
        NotebookDB.getInstance(mView.getAppContext()).deleteGroup(group);
    }
}
