package com.android.ocasa.util;

import android.widget.TextView;

/**
 * Created by leandro on 5/6/17.
 */

public class TextUtils {

    public static boolean isEllipsized(TextView v) {
        android.text.Layout layout = v.getLayout();
        int lines = layout.getLineCount();
        if(lines > 0) {
            int ellipsisCount = layout.getEllipsisCount(lines-1);
            if ( ellipsisCount > 0)
                return true;
        }
        return false;
    }
}
