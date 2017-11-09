package com.seewo.mynotebook.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.adapter.NoteAdapter;
import com.seewo.mynotebook.model.Group;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDBOpenHelper;
import com.seewo.mynotebook.presenter.MainPresenter;
import com.seewo.mynotebook.utils.ShowToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module 主activity
 */
public class MainActivity extends AppCompatActivity implements IMainView,
        NavigationView.OnNavigationItemSelectedListener,
        NoteAdapter.OnItemClickListener,
        SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivity";
    private static final int FROM_ADD = 1;
    public static final int FROM_MANAGE = 2;

    private MainPresenter mPresenter;
    private Group mCurGroup;
    private List<Group> mGroups;

    private DrawerLayout mActivityMain;
    private NavigationView mSelectNv;
    private Toolbar mTopToolbar;
    private RecyclerView mNoteListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        initData();
        initView();
    }

    private void initData() {
        mCurGroup = mPresenter.loadDefaultGroup();
        if (mGroups == null) {
            mGroups = new ArrayList<>();
        }
        mGroups = mPresenter.loadGroups();
    }

    private void initPresenter() {
        mPresenter = new MainPresenter(this);
    }

    private void initView() {
        mActivityMain = (DrawerLayout) findViewById(R.id.activity_main);

        mSelectNv = (NavigationView) findViewById(R.id.select_nv);
        mSelectNv.setNavigationItemSelectedListener(this);
        loadNavigationGroups();

        mTopToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        mTopToolbar.setNavigationIcon(R.mipmap.menu_darkgrey);
        mTopToolbar.setTitle(mCurGroup.getName());
        mTopToolbar.setTitleTextColor(getResources().getColor(R.color.colorDarkGrey));
        mTopToolbar.setOnCreateContextMenuListener(this);

        mNoteListRv = (RecyclerView) findViewById(R.id.note_list_rv);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mNoteListRv.setLayoutManager(manager);
        mNoteListRv.setItemAnimator(new DefaultItemAnimator());

        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mActivityMain, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivityMain.setDrawerListener(toggle);
        toggle.syncState();
        refreshView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "result code: " + resultCode);
        if (requestCode == FROM_ADD) {
            refreshView();
        } else if (requestCode == FROM_MANAGE && resultCode == 0) {
            mGroups = mPresenter.loadGroups();
            Log.d(TAG, "神经病");
            for (Group group : mGroups) {
                Log.d(TAG, "group id: " + group.getId() + "\n group name: " + group.getName());
            }
            loadNavigationGroups();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshView() {
        Log.d(TAG, "refresh view.");
        if (mPresenter == null) {
            ShowToastUtil.show(this, "presenter is null.");
            Log.e(TAG, "presenter is null.");
            return;
        }
        mPresenter.getNotes(mCurGroup);
    }

    private void loadNavigationGroups() {
        mSelectNv.getMenu().removeGroup(R.id.all_groups);
        int i;
        for (i = 0; i < mGroups.size(); i++) {
            if (mGroups.get(i).getId() == 1) {
                mSelectNv.getMenu().add(R.id.all_groups, mGroups.get(i).getId(), 1, mGroups.get(i).getName());
            } else {
                mSelectNv.getMenu().add(R.id.all_groups, mGroups.get(i).getId(), 2, mGroups.get(i).getName());
            }
            mSelectNv.getMenu().getItem(i).setIcon(R.mipmap.unclassified);
        }
        if (i == mGroups.size()) {
            mSelectNv.getMenu().getItem(i).setIcon(R.mipmap.unclassified);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getResources().getString(R.string.find));
        searchView.setIconifiedByDefault(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_add:
                ShowToastUtil.show(this, "add");
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(intent, FROM_ADD);
                break;
            case R.id.action_item1:
                break;
            case R.id.action_item2:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id ==  R.id.manage_groups){
                Log.d(TAG, "manage groups selected");
                Intent intent = new Intent(this, ManageGroupsActivity.class);
                startActivityForResult(intent, FROM_MANAGE);
        } else {
            Group group = new Group();
            group.setId(item.getItemId());
            group.setName(item.getTitle().toString());
            mPresenter.getNotes(group);
            //更新标题
            mCurGroup = group;
            mTopToolbar.setTitle(group.getName());
            if (mActivityMain.isDrawerOpen(GravityCompat.START)) {
                mActivityMain.closeDrawer(GravityCompat.START);
            }
        }
        return false;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void setAdapter(NoteAdapter adapter) {
        mNoteListRv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "click " + position);
        Intent intent = new Intent(this, AddNoteActivity.class);
        Note note = mPresenter.getNote(position);
        intent.putExtra(NotebookDBOpenHelper.GROUP_ID, mCurGroup.getId());
        intent.putExtra(NotebookDBOpenHelper.NOTE_ID, note.getId());
        startActivityForResult(intent, FROM_ADD);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        Log.d(TAG, "long click " + position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.delete))
                .setMessage(getResources().getString(R.string.confirm_delete))
                .setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        })
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (mPresenter.delete(position)) {
                                    refreshView();
                                    ShowToastUtil.show(getAppContext(),
                                            getResources().getString(R.string.delete_success));
                                } else {
                                    ShowToastUtil.show(getAppContext(),
                                            getResources().getString(R.string.delete_failed));
                                }
                            }
                        });
        builder.create().show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: " + query);
        mPresenter.queryByTitle(mCurGroup.getId(), query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: " + newText);
        if (TextUtils.isEmpty(newText)) {
            refreshView();
        }
        return false;
    }
}
