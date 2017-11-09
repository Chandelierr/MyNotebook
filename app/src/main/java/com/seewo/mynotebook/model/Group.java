package com.seewo.mynotebook.model;

/**
 * Created by user on 2017/11/7.
 */

public class Group {
    private int mId;
    private String mName;
    //private int mCount;

    public Group(){}

    public Group(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

//    public int getCount() {
//        return mCount;
//    }
//
//    public void setCount(int count) {
//        mCount = count;
//    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
