package com.libratone.v3.widget.dialog

import android.view.View

/**
 * create by Dawson.Gao
 * on 2018/9/3
 *
 */
interface OnBackPressListener {
    fun onBackPressed(smartDialog: SmartDialog)
}

interface OnCancelListener {
    fun onCancel(dialog: SmartDialog)
}

interface OnClickListener {
    fun onClick(dialog: SmartDialog, view: View)
}

interface OnDismissListener {
    fun onDismiss(dialog: SmartDialog)
}