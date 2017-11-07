package com.seewo.mynotebook;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.seewo.mynotebook.presenter.MainPresenter;
import com.seewo.mynotebook.utils.ShowToastUtil;
import com.seewo.mynotebook.view.AddNoteActivity;
import com.seewo.mynotebook.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private MainPresenter mPresenter;

    private DrawerLayout mActivityMain;
    private NavigationView mSelectNv;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new MainPresenter(this);
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

        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mActivityMain, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActivityMain.setDrawerListener(toggle);
        toggle.syncState();
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
                ShowToastUtil.show(this, "点击了查询");
                break;
            case R.id.toolbar_add:
                ShowToastUtil.show(this, "添加");
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.action_item1:
                ShowToastUtil.show(this, "点击了菜单1");
                break;
            case R.id.action_item2:
                ShowToastUtil.show(this, "点击了菜单2");
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
}
