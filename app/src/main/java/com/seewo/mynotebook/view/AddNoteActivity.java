package com.seewo.mynotebook.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDBOpenHelper;
import com.seewo.mynotebook.presenter.AddNotePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module 添加记事的activity
 */

public class AddNoteActivity extends AppCompatActivity implements IAddNoteView {
    private static final String TAG = "AddNoteActivity";

    private Toolbar mAddToolbar;
    private Spinner mSelectGroupSpinner;
    private EditText mAddTitleEd;
    private EditText mAddContentEd;
    private TextView mGroupNameTv;
    private TextView mAddTimeTv;

    private List<Group> mGroupList;
    private AddNotePresenter mPresenter;
    private boolean mIsNew;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new AddNotePresenter(this);
    }

    private void initView() {
        mAddToolbar = (Toolbar) findViewById(R.id.add_toolbar);
        mAddToolbar.setNavigationIcon(R.mipmap.add_toolbar_back);
        mAddToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealNote();
            }
        });
        mAddToolbar.setTitle(R.string.add_a_note);

        mAddTitleEd = (EditText) findViewById(R.id.add_title);
        mAddContentEd = (EditText) findViewById(R.id.add_content);
        mAddTimeTv = (TextView) findViewById(R.id.add_time);

        //区分 分组名称 新建是spinner， 编辑是textview


        mGroupNameTv = (TextView)findViewById(R.id.group_name);
        mSelectGroupSpinner = (Spinner) findViewById(R.id.select_group);

        judgeIsNew();
    }

    private void judgeIsNew() {
        mNote = (Note) getIntent().getSerializableExtra(MainActivity.OPEN_NOTE);
        if (mNote != null) {
            mIsNew = false;
            mGroupNameTv.setVisibility(View.VISIBLE);
            mSelectGroupSpinner.setVisibility(View.INVISIBLE);
            mGroupNameTv.setText(mNote.getGroup());
            mAddTitleEd.setText(mNote.getTitle());
            mAddContentEd.setText(mNote.getContent());
            mAddTimeTv.setText(mNote.getTime());
        } else {
            mIsNew = true;
            if (mGroupList == null) {
                mGroupList = new ArrayList<Group>();
            }
            mGroupList.add(new Group("未分组"));
            mGroupList.add(new Group("学习"));
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.group_name, mGroupList);
            arrayAdapter.setDropDownViewResource(R.layout.group_name);
            mSelectGroupSpinner.setAdapter(arrayAdapter);
            mGroupNameTv.setVisibility(View.INVISIBLE);
            mSelectGroupSpinner.setVisibility(View.VISIBLE);
            mAddTimeTv.setVisibility(View.INVISIBLE);
        }
    }

    private void dealNote() {
        Log.d(TAG, "deal note.");
        if (mIsNew) {
            mPresenter.insert(mSelectGroupSpinner.getSelectedItem().toString(),
                    mAddTitleEd.getText().toString(),
                    mAddContentEd.getText().toString());
        } else {
            mNote.setTitle(mAddTitleEd.getText().toString());
            mNote.setContent(mAddContentEd.getText().toString());
            mPresenter.update(mNote);
        }
    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public void backToMain() {
        finish();
    }
}
