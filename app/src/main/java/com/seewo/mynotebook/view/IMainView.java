package com.seewo.mynotebook.view;

import android.content.Context;

import com.seewo.mynotebook.adapter.NoteAdapter;

/**
 * Created by user on 2017/11/7.
 */

public interface IMainView {
    Context getAppContext();

    void setAdapter(NoteAdapter mAdapter);
}
