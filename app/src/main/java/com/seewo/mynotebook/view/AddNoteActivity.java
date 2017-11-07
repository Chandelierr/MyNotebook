package com.seewo.mynotebook.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.model.Group;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {
    private static final String TAG = "AddNoteActivity";

    private Toolbar mAddToolbar;
    private Spinner mSelectGroupSpinner;

    private List<Group> mGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
    }

    private void initView() {
        mAddToolbar = (Toolbar) findViewById(R.id.add_toolbar);
        mAddToolbar.setNavigationIcon(R.mipmap.add_toolbar_back);
        mAddToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAddToolbar.setTitle(R.string.add_a_note);

        if (mGroupList == null) {
            mGroupList = new ArrayList<Group>();
        }
        mGroupList.add(new Group("未分组"));
        mGroupList.add(new Group("学习"));
        mSelectGroupSpinner = (Spinner) findViewById(R.id.select_group);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.group_name, mGroupList);
        arrayAdapter.setDropDownViewResource(R.layout.group_name);
        mSelectGroupSpinner.setAdapter(arrayAdapter);
    }
}
