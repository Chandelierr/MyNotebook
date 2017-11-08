package com.seewo.mynotebook.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.seewo.mynotebook.R;
import com.seewo.mynotebook.adapter.NoteAdapter;
import com.seewo.mynotebook.model.Note;
import com.seewo.mynotebook.model.NotebookDBOpenHelper;
import com.seewo.mynotebook.presenter.MainPresenter;
import com.seewo.mynotebook.utils.ShowToastUtil;
import com.seewo.mynotebook.view.AddNoteActivity;
import com.seewo.mynotebook.view.IMainView;

/**
 * Created by 王梦洁 on 2017/11/7.
 *
 * @module 主activity
 */
public class MainActivity extends AppCompatActivity implements IMainView,
        NavigationView.OnNavigationItemSelectedListener, NoteAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private static final int FROM_ADD = 1;
    public static final String OPEN_NOTE = "open_note";

    private MainPresenter mPresenter;

    private DrawerLayout mActivityMain;
    private NavigationView mSelectNv;
    private Toolbar mTopToolbar;
    private RecyclerView mNoteListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new MainPresenter(this);
        Log.d(TAG, "toolbar title: " + mTopToolbar.getTitle().toString());
        refreshView();
    }

    private void initView() {
        mActivityMain = (DrawerLayout)findViewById(R.id.activity_main);

        mSelectNv = (NavigationView)findViewById(R.id.select_nv);
        mSelectNv.setNavigationItemSelectedListener(this);

        mTopToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        mTopToolbar.setNavigationIcon(R.mipmap.menu_darkgrey);
        mTopToolbar.setTitle(R.string.unclassified);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FROM_ADD) {
            refreshView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshView() {
        if (mPresenter == null) {
            ShowToastUtil.show(this, "presenter is null.");
            Log.e(TAG, "presenter is null.");
            return;
        }
        mPresenter.getNotes(mTopToolbar.getTitle().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_search:
                break;
            case R.id.toolbar_add:
                ShowToastUtil.show(this, "add");
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(intent, FROM_ADD);
                break;
            case R.id.action_item1:
                break;
            case R.id.action_item2:
                break;
            default:break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unclassified:
                Log.d(TAG, "unclassified selected");
                break;
            case R.id.manage_groups:
                Log.d(TAG, "manage groups selected");
                break;
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
        intent.putExtra(OPEN_NOTE, note);
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
}
