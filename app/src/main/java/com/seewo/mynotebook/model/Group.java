package com.seewo.mynotebook.model;

/**
 * Created by user on 2017/11/7.
 */

public class Group {
    private String mName;

    public Group(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
