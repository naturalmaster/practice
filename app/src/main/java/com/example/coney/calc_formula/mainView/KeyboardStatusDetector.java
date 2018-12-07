package com.example.coney.calc_formula.mainView;

/**
 * Created by coney on 2018/12/3.
 */
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * @author s_hfj
 */
public class KeyboardStatusDetector {
    private int screenHeight;
    private int keyboardHeight = 100;
    private KeyboardVisibilityListener mVisibilityListener;

    boolean keyboardVisible = false;

    public KeyboardStatusDetector registerFragment(Fragment f) {
        return registerView(f.getView());
    }

    public KeyboardStatusDetector registerActivity(Activity a) {
        screenHeight = a.getWindowManager().getDefaultDisplay().getHeight();
        keyboardHeight = screenHeight/3;
        return registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public KeyboardStatusDetector registerView(final View v) {
        v.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);
                int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > keyboardHeight) {
                    if (!keyboardVisible) {
                        keyboardVisible = true;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(true);
                        }
                    }
                } else {
                    if (keyboardVisible) {
                        keyboardVisible = false;
                        if (mVisibilityListener != null) {
                            mVisibilityListener.onVisibilityChanged(false);
                        }
                    }
                }
            }
        });

        return this;
    }

    public KeyboardStatusDetector setVisibilityListener(KeyboardVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface KeyboardVisibilityListener {
        /**
         * 当键盘弹出或者收回时，调用该方法
         * @param keyboardVisible
         */
        void onVisibilityChanged(boolean keyboardVisible);
    }

}