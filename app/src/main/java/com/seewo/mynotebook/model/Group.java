package com.seewo.mynotebook.model;

/**
 * Created by 王梦洁 on 2017/11/7.
 */

public class Group {
    private int mId;
    private String mName;

    public Group(){}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            Group group = (Group)obj;
            return group.getId() == this.getId() && group.getName().equals(this.getName());
        }
        return false;
    }

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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
