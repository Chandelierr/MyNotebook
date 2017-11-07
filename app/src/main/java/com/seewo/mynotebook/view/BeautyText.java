package com.seewo.mynotebook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.seewo.mynotebook.R;

/**
 * Created by user on 2017/11/7.
 */

public class BeautyText extends EditText {

    public BeautyText(Context context) {
        this(context, null);
    }

    public BeautyText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeautyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BeautyText);

    }
}
