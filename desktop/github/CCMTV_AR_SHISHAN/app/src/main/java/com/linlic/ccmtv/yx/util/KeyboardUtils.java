package com.linlic.ccmtv.yx.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Niklaus
 * Date:2016/5/5
 * Time:9:30
 */
public final class KeyboardUtils {
    private KeyboardUtils() {
    }

    public static void showKeyboardWithFocusAlways(Activity activity, View view) {
        if (activity != null) {
//            if (view != null) {
//                view.requestFocus();
//            }
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
//                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
//                imm.showSoftInput(view,0);
                imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
            }

        }
    }

    public static void showKeyboardWithFocus(Activity activity, View view) {
        if (activity != null) {
//            if (view != null) {
//                view.requestFocus();
//            }
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        if (activity != null) {
            if (view != null) {
                view.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                activity.getCurrentFocus().clearFocus();
            }
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        if (activity != null) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)
                        activity.getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } else {
                hideKeyboard(activity);
            }
        }
    }
}