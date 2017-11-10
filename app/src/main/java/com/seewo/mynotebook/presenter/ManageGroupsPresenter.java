package com.seewo.mynotebook.presenter;

import android.util.Log;

import com.seewo.mynotebook.adapter.GroupAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.NotebookDB;
import com.seewo.mynotebook.view.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/9.
 */

public class ManageGroupsPresenter {
    private static final String TAG = "ManageGroupsPresenter";
    public static final int WARNING_CAN_NOT_DELETE = -13;

    private IView mView;

    private List<Group> mGroups;
    private GroupAdapter mAdapter;
    
    public ManageGroupsPresenter(IView view) {
        mView = view;
    }

    public boolean insertGroup(String groupName) {
        Group group = new Group(groupName);
        return NotebookDB.getInstance(mView.getAppContext()).insertGroup(group);
    }

    public int deleteGroup(int position) {
        Group group = mGroups.get(position);
        Log.d(TAG, "delete group:" + group.getName());
        if (group.getId() == 1) {
            return WARNING_CAN_NOT_DELETE;
        }
        NotebookDB.getInstance(mView.getAppContext()).deleteGroup(group);
        return 0;
    }

    public List<Group> loadAllGroups() {
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
}
