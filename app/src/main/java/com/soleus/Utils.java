package com.soleus;

import android.content.Context;
import android.widget.Toast;

public final class Utils {
    private Utils() {
    }

    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}