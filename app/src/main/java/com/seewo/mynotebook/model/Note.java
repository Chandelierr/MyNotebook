package com.seewo.mynotebook.model;

import java.io.Serializable;

/**
 * Created by 王梦洁 on 2017/11/8.
 *
 * @module 记事本类
 */

public class Note implements Serializable{

    private int mId;
    private String mGroup;
    private String mTitle;
    private String mTime;
    private String mContent;

    public Note() {
    }

    public Note(String group, String title, String time, String content) {
        mGroup = group;
        mTitle = title;
        mTime = time;
        mContent = content;
    }

    public String getGroup() {
        return mGroup.toString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getTime() {
        return mTime;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
