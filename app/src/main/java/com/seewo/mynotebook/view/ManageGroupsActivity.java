package com.seewo.mynotebook.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.adapter.GroupAdapter;
import com.seewo.mynotebook.adapter.NoteAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.presenter.ManageGroupsPresenter;
import com.seewo.mynotebook.utils.ShowToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupsActivity extends AppCompatActivity
        implements IView,
        NoteAdapter.OnItemClickListener {
    private static final String TAG = "ManageGroupsActivity";

    private ManageGroupsPresenter mPresenter;
    private List<Group> mGroups;

    private Toolbar mManageToolbar;
    private RecyclerView mGroupsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        initPresenter();
        initView();
        initData();
    }

    //init================================================================================

    private void initPresenter() {
        mPresenter = new ManageGroupsPresenter(this);
    }

    private void initData() {
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        mGroups.clear();
        mGroups.addAll(mPresenter.loadAllGroups());
    }

    private void initView() {
        mManageToolbar = (Toolbar) findViewById(R.id.manage_toolbar);
        mManageToolbar.setTitle(R.string.manage_groups);
        mManageToolbar.setTitleTextColor(getResources().getColor(R.color.colorDarkGrey));
        mManageToolbar.setOnCreateContextMenuListener(this);
        setSupportActionBar(mManageToolbar);

        mGroupsRv = (RecyclerView) findViewById(R.id.groups_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mGroupsRv.setLayoutManager(manager);
    }

    //about toolbar========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manage_toolbar_add:
                ShowToastUtil.show(this, "add");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.please_input_group_name);
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
                builder.setView(view);
                final EditText groupNameEt = (EditText) view.findViewById(R.id.input_group_name);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = groupNameEt.getText().toString().trim();
                        if (mPresenter.insertGroup(groupName)) {
                            ShowToastUtil.show(getAppContext(),
                                    getResources().getString(R.string.insert_success));
                            refreshView();
                        } else {
                            ShowToastUtil.show(getAppContext(),
                                    getResources().getString(R.string.delete_failed));
                        }
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return true;
    }

    //recycler view click================================================================

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "click " + position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "long click " + position);
        if (mPresenter.deleteGroup(position) == ManageGroupsPresenter.WARNING_CAN_NOT_DELETE) {
            ShowToastUtil.show(this, getResources().getString(R.string.can_not_delete_default_group));
        } else {
            refreshView();
        }
    }

    //refresh ui=========================================================================

    private void refreshView() {
        Log.d(TAG, "refresh view.");
        if (mPresenter == null) {
            ShowToastUtil.show(this, "presenter is null.");
            Log.e(TAG, "presenter is null.");
            return;
        }
        mPresenter.loadAllGroups();
    }

    //implement IView====================================================================

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mGroupsRv.setAdapter(adapter);
        ((GroupAdapter) adapter).setOnItemClickListener(this);
    }
}
