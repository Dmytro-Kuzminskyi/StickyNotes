package com.example.android.stickynotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class EditTextWithListener extends android.support.v7.widget.AppCompatEditText {

    public EditTextWithListener(Context context) {
        super(context);
    }

    public EditTextWithListener(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EditTextWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface BackPressedListener {
        void onImeBack(EditTextWithListener editText);
    }

    private BackPressedListener mOnImeBack;

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setBackPressedListener(BackPressedListener listener) {
        mOnImeBack = listener;
    }
}
