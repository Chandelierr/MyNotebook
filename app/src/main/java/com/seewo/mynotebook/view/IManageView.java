package com.seewo.mynotebook.view;

import android.content.Context;

import com.seewo.mynotebook.adapter.GroupAdapter;

/**
 * Created by 王梦洁 on 2017/11/9.
 */

public interface IManageView {
    Context getAppContext();

    void setAdapter(GroupAdapter mAdapter);
}
