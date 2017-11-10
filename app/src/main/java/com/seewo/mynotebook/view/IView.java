package com.seewo.mynotebook.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.seewo.mynotebook.adapter.NoteAdapter;

/**
 * Created by user on 2017/11/10.
 */

public interface IView {
    Context getAppContext();

    void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter);
}
