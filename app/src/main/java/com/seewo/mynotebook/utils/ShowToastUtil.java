package com.seewo.mynotebook.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.seewo.mynotebook.R;

/**
 * Created by 王梦洁 on 2017/11/7.
 */

public class ShowToastUtil {

    public static void show(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
