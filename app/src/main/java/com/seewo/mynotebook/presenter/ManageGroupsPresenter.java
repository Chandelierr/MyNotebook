package com.seewo.mynotebook.presenter;

import com.seewo.mynotebook.adapter.GroupAdapter;
import com.seewo.mynotebook.model.Group;
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
