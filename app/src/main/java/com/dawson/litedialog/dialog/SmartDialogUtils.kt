package com.libratone.v3.widget.dialog

import android.content.Context

/**
 * create by Dawson.Gao
 * on 2018/9/3
 */
object SmartDialogUtils {

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}