package com.seewo.mynotebook.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDBOpenHelper;
import com.seewo.mynotebook.presenter.AddNotePresenter;
import com.seewo.mynotebook.utils.ShowToastUtil;
import com.seewo.mynotebook.view.editor.BeautyText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module 添加记事的activity
 */

public class AddNoteActivity extends AppCompatActivity implements IView {
    private static final String TAG = "AddNoteActivity";

    private Toolbar mAddToolbar;
    private Spinner mSelectGroupSpinner;
    private EditText mAddTitleEd;
    private TextView mGroupNameTv;
    private TextView mAddTimeTv;
    private BeautyText mBeautyText;

    private List<Group> mGroupList;
    private AddNotePresenter mPresenter;
    private boolean mIsNew;
    private Note mNote;
    private Group mCatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initPresenter();
        initData();
        initView();
    }

    //init=============================================================================

    private void initPresenter() {
        mPresenter = new AddNotePresenter(this);
    }

    private void initData() {
        int groupId = getIntent().getIntExtra(NotebookDBOpenHelper.GROUP_ID, -1);
        int noteId = getIntent().getIntExtra(NotebookDBOpenHelper.NOTE_ID, -1);
        if (groupId != -1 && noteId != -1) {
            mCatGroup = mPresenter.loadCurGroup(groupId);
            mNote = mPresenter.loadCurNote(noteId);
        }
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
        mAddToolbar.setTitleTextColor(getResources().getColor(R.color.colorDarkGrey));
        mAddTitleEd = (EditText) findViewById(R.id.add_title);
        mAddTimeTv = (TextView) findViewById(R.id.add_time);
        initBeautyView();

        mGroupNameTv = (TextView) findViewById(R.id.group_name);
        mSelectGroupSpinner = (Spinner) findViewById(R.id.select_group);

        judgeIsNew();
    }

    private void judgeIsNew() {
        if (mNote != null) {
            mIsNew = false;
            mGroupNameTv.setVisibility(View.VISIBLE);
            mSelectGroupSpinner.setVisibility(View.INVISIBLE);
            mGroupNameTv.setText(mCatGroup.getName());
            mAddTitleEd.setText(mNote.getTitle());
            mBeautyText.setText(mNote.getContent());
            mAddTimeTv.setText(mNote.getTime());
        } else {
            mIsNew = true;
            if (mGroupList == null) {
                mGroupList = new ArrayList<Group>();
            }
            mGroupList = mPresenter.loadGroups();
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
        boolean isSuccess;
        if (mIsNew) {
            isSuccess = mPresenter.insert(mSelectGroupSpinner.getSelectedItem().toString(),
                    mAddTitleEd.getText().toString(),
                    mBeautyText.getText().toString());
        } else {
            mNote.setTitle(mAddTitleEd.getText().toString());
            mNote.setContent(mBeautyText.getText().toString());
            isSuccess = mPresenter.update(mNote);
        }
        if (isSuccess) {
            finish();
        }
    }

    //rich editor==============================================================

    private void initBeautyView() {
        mBeautyText = (BeautyText) findViewById(R.id.beauty_text);
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikeThrough();
    }

    private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeautyText.bold(!mBeautyText.contains(BeautyText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowToastUtil.show(AddNoteActivity.this, getResources().getString(R.string.toast_bold));
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeautyText.italic(!mBeautyText.contains(BeautyText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowToastUtil.show(AddNoteActivity.this, getResources().getString(R.string.toast_italic));
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton  underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "underline: " + !mBeautyText.contains(BeautyText.FORMAT_UNDERLINE));
                mBeautyText.underline(!mBeautyText.contains(BeautyText.FORMAT_UNDERLINE));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowToastUtil.show(AddNoteActivity.this, getResources().getString(R.string.toast_underline));
                return false;
            }
        });
    }

    private void setupStrikeThrough() {
        ImageButton  strikeThrough = (ImageButton) findViewById(R.id.strike_through);

        strikeThrough.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "strikeThrough: " + !mBeautyText.contains(BeautyText.FORMAT_STRIKE_THROUGH));
                mBeautyText.strikeThrough(!mBeautyText.contains(BeautyText.FORMAT_STRIKE_THROUGH));
            }
        });

        strikeThrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowToastUtil.show(AddNoteActivity.this, getResources().getString(R.string.toast_strike_through));
                return false;
            }
        });
    }

    //implement IView=========================================================================

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter) {
    }
}
