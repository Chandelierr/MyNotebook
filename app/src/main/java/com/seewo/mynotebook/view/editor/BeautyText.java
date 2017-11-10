package com.seewo.mynotebook.view.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.seewo.mynotebook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王梦洁 on 2017/11/9.
 */

public class BeautyText extends EditText {
    private static final String TAG = "BeautyText";
    public static final int FORMAT_BOLD = 0x01;
    public static final int FORMAT_ITALIC = 0x02;
    public static final int FORMAT_UNDERLINE = 0x03;
    public static final int FORMAT_STRIKE_THROUGH = 0x04;

    public BeautyText(Context context) {
        super(context);
    }

    public BeautyText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeautyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //bold 和 italic=========================================================================

    public void bold(boolean valid) {
        if (valid) {
            styleValid(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        } else {
            styleInvalid(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
        }
    }

    public void italic(boolean valid) {
        if (valid) {
            styleValid(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        } else {
            styleInvalid(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
        }
    }

    protected void styleValid(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }

        if (start >= end) {
            return;
        }

        getEditableText().setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void styleInvalid(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return;
        }

        if (start >= end) {
            return;
        }

        StyleSpan[] spans = getEditableText().getSpans(start, end, StyleSpan.class);

        for (StyleSpan span : spans) {
            if (span.getStyle() == style) {
                getEditableText().removeSpan(span);
            }
        }
    }

    protected boolean containStyle(int style, int start, int end) {
        switch (style) {
            case Typeface.NORMAL:
            case Typeface.BOLD:
            case Typeface.ITALIC:
            case Typeface.BOLD_ITALIC:
                break;
            default:
                return false;
        }

        if (start > end) {
            return false;
        }
        StyleSpan[] spans = getEditableText().getSpans(start, end, StyleSpan.class);
        return spans.length > 0;
    }

    //underline===============================================================================

    public void underline(boolean valid) {
        if (valid) {
            underlineValid(getSelectionStart(), getSelectionEnd());
        } else {
            underlineInvalid(getSelectionStart(), getSelectionEnd());
        }
    }

    private void underlineValid(int start, int end) {
        if (start >= end) {
            return;
        }
        getEditableText().setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void underlineInvalid(int start, int end) {
        if (start >= end) {
            return;
        }
        UnderlineSpan[] spans = getEditableText().getSpans(start, end, UnderlineSpan.class);
        for (UnderlineSpan span : spans) {
            getEditableText().removeSpan(span);
        }
    }

    //StrikeThrough===========================================================================

    public void strikeThrough(boolean valid) {
        if (valid) {
            strikeThroughValid(getSelectionStart(), getSelectionEnd());
        } else {
            strikeThroughInvalid(getSelectionStart(), getSelectionEnd());
        }
    }

    private void strikeThroughValid(int start, int end) {
        if (start >= end) {
            return;
        }
        getEditableText().setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void strikeThroughInvalid(int start, int end) {
        if (start >= end) {
            return;
        }
        StrikethroughSpan[] spans = getEditableText().getSpans(start, end, StrikethroughSpan.class);

        for (StrikethroughSpan span : spans) {
            getEditableText().removeSpan(span);
        }
    }


    //help======================================================================================

    public boolean contains(int format) {
        switch (format) {
            case FORMAT_BOLD:
                return containStyle(Typeface.BOLD, getSelectionStart(), getSelectionEnd());
            case FORMAT_ITALIC:
                return containStyle(Typeface.ITALIC, getSelectionStart(), getSelectionEnd());
            case FORMAT_UNDERLINE:
                return getEditableText().getSpans(getSelectionStart(), getSelectionEnd(), UnderlineSpan.class).length > 0;
            case FORMAT_STRIKE_THROUGH:
                return getEditableText().getSpans(getSelectionStart(), getSelectionEnd(), StrikethroughSpan.class).length > 0;
            default:
                return false;
        }
    }

}
